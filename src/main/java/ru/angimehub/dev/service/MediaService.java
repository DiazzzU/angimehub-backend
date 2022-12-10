package ru.angimehub.dev.service;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.angimehub.dev.dto.FullVideoDto;
import ru.angimehub.dev.dto.SearchDto;
import ru.angimehub.dev.dto.SmallArtistDto;
import ru.angimehub.dev.dto.SmallVideoDto;
import ru.angimehub.dev.entity.MediaEntity;
import ru.angimehub.dev.entity.UserEntity;
import ru.angimehub.dev.entity.UserTokenEntity;
import ru.angimehub.dev.exceptions.ForbiddenException;
import ru.angimehub.dev.exceptions.NotFoundException;
import ru.angimehub.dev.repository.MediaRepository;
import ru.angimehub.dev.repository.UserRepository;
import ru.angimehub.dev.repository.UserTokenRepository;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URISyntaxException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MediaService {
    @Autowired
    private UserTokenRepository userTokenRepository;
    @Autowired
    private MediaRepository mediaRepository;
    @Autowired
    private UserRepository userRepository;

    public List<SmallVideoDto> getPopularUrls(Integer type) {
        List<MediaEntity> lstMediaEntities = mediaRepository.findTop10ByTypeOrderByViewsDesc(type);
        List<SmallVideoDto> lstUrls = new ArrayList<>();
        for(int i = 0; i < lstMediaEntities.size(); i++) {
            lstUrls.add(lstMediaEntities.get(i).toSmallVideoDto());
        }
        return lstUrls;
    }

    public FullVideoDto videoInfo(Integer id) {
        Optional<MediaEntity> optionalMediaEntity = mediaRepository.findById(id);
        if (optionalMediaEntity.isEmpty()) {
            throw new NotFoundException("Video does not exist");
        }
        MediaEntity mediaEntity = optionalMediaEntity.get();
        mediaEntity.setViews(mediaEntity.getViews() + 1);
        mediaEntity.getArtist().setPopularness(mediaEntity.getArtist().getPopularness() + 1);
        userRepository.save(mediaEntity.getArtist());
        return mediaEntity.toFullVideoDto();
    }

    public void uploadVideo(String name, MultipartFile video, MultipartFile splashScreen, Integer type, String token) throws URISyntaxException, IOException, InterruptedException {

        Optional<UserTokenEntity> optionalUserTokenEntity = userTokenRepository.findOneByToken(token);
        if(!optionalUserTokenEntity.isPresent()) {
            throw new NotFoundException("User token doesn't exist");
        }

        UserEntity userEntity = optionalUserTokenEntity.get().getUser();

        if (!userEntity.getRole().equals("artist")) {
            throw new ForbiddenException("User is not artist");
        }

        String extensionVideo = video.getContentType().split("/")[1];
        String extensionSplashScreen = splashScreen.getContentType().split("/")[1];
        MediaEntity mediaEntity = MediaEntity.buildFrom("", "", name, userEntity, type);
        mediaRepository.save(mediaEntity);

        String pathVideo = "/" + Integer.toString(userEntity.getId()) + "/" +
                           (type == 0 ? "standups" : "podcasts") + "/" +
                           Integer.toString(mediaEntity.getId()) + "_video" + "." + extensionVideo;
        String pathSplashScreen = "/" + Integer.toString(userEntity.getId()) + "/" +
                (type == 0 ? "standups" : "podcasts") + "/" +
                Integer.toString(mediaEntity.getId()) + "_screen" + "." + extensionSplashScreen;

        HttpClient client = HttpClient.newHttpClient();
        String videoUrl = "https://storage.yandexcloud.net/angimehub" + pathVideo;
        HttpRequest requestVideo = HttpRequest.newBuilder()
                .uri(new URI(videoUrl))
                .headers("Content-Type", video.getContentType())
                .PUT(HttpRequest.BodyPublishers.ofByteArray(video.getBytes()))
                .build();
        HttpResponse<String> responseVideo = client.send(requestVideo, HttpResponse.BodyHandlers.ofString());

        String splashScreenUrl = "https://storage.yandexcloud.net/angimehub" + pathSplashScreen;
        HttpRequest requestScreen = HttpRequest.newBuilder()
                .uri(new URI(splashScreenUrl))
                .headers("Content-Type", splashScreen.getContentType())
                .PUT(HttpRequest.BodyPublishers.ofByteArray(splashScreen.getBytes()))
                .build();
        HttpResponse<String> responseScreen = client.send(requestScreen, HttpResponse.BodyHandlers.ofString());

        mediaEntity.setUrlVideo(videoUrl);
        mediaEntity.setUrlSplashScreen(splashScreenUrl);
        mediaRepository.save(mediaEntity);
    }

    public void deleteMedia(Integer videoId) throws URISyntaxException, IOException, InterruptedException {
        Optional<MediaEntity> optionalMediaEntity = mediaRepository.findById(videoId);
        if (optionalMediaEntity.isEmpty()) {
            throw new NotFoundException("Media doesn't found");
        }

        MediaEntity mediaEntity = optionalMediaEntity.get();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest requestVideo = HttpRequest.newBuilder()
                .uri(new URI(mediaEntity.getUrlVideo()))
                .DELETE()
                .build();
        HttpResponse<String> responseVideo = client.send(requestVideo, HttpResponse.BodyHandlers.ofString());

        HttpRequest requestScreen = HttpRequest.newBuilder()
                .uri(new URI(mediaEntity.getUrlVideo()))
                .DELETE()
                .build();
        HttpResponse<String> responseScreen = client.send(requestScreen, HttpResponse.BodyHandlers.ofString());

        mediaRepository.delete(mediaEntity);
    }

    public SearchDto searchByName(String searchValue) {
        String[] words = searchValue.split(" ");
        List<MediaEntity> allStandups = mediaRepository.findByType(0);

        Comparator<MediaEntity> comparatorMedia = new Comparator<MediaEntity>(){
            @Override
            public int compare(final MediaEntity lhs,MediaEntity rhs) {
                int countl = 0, countr = 0;
                for (String word: words) {
                    if (lhs.getMediaName().contains(word)) countl ++;
                    if (rhs.getMediaName().contains(word)) countr ++;
                }
                if (countl > countr) return -1;
                if (countl < countr) return 1;
                return 0;
            }
        };

        Collections.sort(allStandups, comparatorMedia);
        List <SmallVideoDto> standups = new ArrayList<>();
        for (int i = 0; i < allStandups.size(); i++) {
            int count = 0;
            for (String word: words) {
                if (allStandups.get(i).getMediaName().contains(word)) count ++;
            }
            if (count == 0) break;
            standups.add(allStandups.get(i).toSmallVideoDto());
        }

        List<MediaEntity> allPodcasts = mediaRepository.findByType(1);
        Collections.sort(allPodcasts, comparatorMedia);
        List <SmallVideoDto> podcasts = new ArrayList<>();
        for (int i = 0; i < allPodcasts.size(); i++) {
            int count = 0;
            for (String word: words) {
                if (allPodcasts.get(i).getMediaName().contains(word)) count ++;
            }
            if (count == 0) break;
            podcasts.add(allPodcasts.get(i).toSmallVideoDto());
        }

        List<UserEntity> allArtists = userRepository.findByRole("artist");
        List<SmallArtistDto> artists = new ArrayList<>();
        for (int i = 0; i < allArtists.size(); i++) {
            if (searchValue.contains(allArtists.get(i).getFirstName())
                    || searchValue.contains(allArtists.get(i).getSecondName())) {
                artists.add(allArtists.get(i).toSmallArtistDto());
            }
        }

        SearchDto searchDto = new SearchDto();
        searchDto.setStandups(standups);
        searchDto.setPodcasts(podcasts);
        searchDto.setArtists(artists);
        return searchDto;
    }
}
