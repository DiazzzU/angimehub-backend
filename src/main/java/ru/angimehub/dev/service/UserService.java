package ru.angimehub.dev.service;

import org.springframework.web.multipart.MultipartFile;
import ru.angimehub.dev.dto.SmallVideoDto;
import ru.angimehub.dev.dto.UserDto;
import ru.angimehub.dev.entity.MediaEntity;
import ru.angimehub.dev.entity.UserEntity;
import ru.angimehub.dev.entity.UserTokenEntity;
import ru.angimehub.dev.exceptions.ForbiddenException;
import ru.angimehub.dev.exceptions.NotFoundException;
import ru.angimehub.dev.exceptions.UnauthorizedException;
import ru.angimehub.dev.repository.UserRepository;
import ru.angimehub.dev.repository.UserTokenRepository;
import ru.angimehub.dev.repository.MediaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserTokenRepository userTokenRepository;
    @Autowired
    private MediaRepository mediaRepository;

    public void register(UserDto userDto){
        UserEntity userEntity = UserEntity.buildFrom(userDto);

        Optional<UserEntity> optionalUserEntity = userRepository.findOneByEmail(userEntity.getEmail());

        if(optionalUserEntity.isPresent()){
            throw new ForbiddenException("User already exist");
        }
        userRepository.save(userEntity);
    }

    public String login(String email, String password) {
        Optional<UserEntity> optionalUserEntity = userRepository.findOneByEmail(email);
        if(!optionalUserEntity.isPresent()){
            throw new NotFoundException("User doesn't exist");
        }
        UserEntity userEntity = optionalUserEntity.get();
        if(!userEntity.getPassword().equals(password)){
            throw new UnauthorizedException("Incorrect password");
        }
        Optional<UserTokenEntity> optionalUserTokenEntity = userTokenRepository.findOneByUser(userEntity);
        if (optionalUserTokenEntity.isPresent()) {
            return optionalUserTokenEntity.get().getToken();
        }
        String token = UUID.randomUUID().toString();

        UserTokenEntity userTokenEntity = UserTokenEntity.buildFrom(token, userEntity);
        userTokenRepository.save(userTokenEntity);
        return token;
    }

    public Map<String, String> info(String token) {
        HashMap<String, String> information = new HashMap<>();
        Optional<UserTokenEntity> optionalUserTokenEntity = userTokenRepository.findOneByToken(token);
        if(!optionalUserTokenEntity.isPresent()){
            throw new NotFoundException("User token doesn't exist");
        }
        UserEntity userEntity = optionalUserTokenEntity.get().getUser();
        information.put("firstName", userEntity.getFirstName());
        information.put("secondName", userEntity.getSecondName());
        information.put("email", userEntity.getEmail());
        information.put("role", userEntity.getRole());
        return information;
    }

    public void becomeArtist(String token) {
        Optional<UserTokenEntity> optionalUserTokenEntity = userTokenRepository.findOneByToken(token);
        if(!optionalUserTokenEntity.isPresent()){
            throw new NotFoundException("User token doesn't exist");
        }
        UserEntity userEntity = optionalUserTokenEntity.get().getUser();
        userEntity.setRole("artist");
        userRepository.save(userEntity);
    }

    public void becomeSimple(String token) {
        Optional<UserTokenEntity> optionalUserTokenEntity = userTokenRepository.findOneByToken(token);
        if(!optionalUserTokenEntity.isPresent()){
            throw new NotFoundException("User token doesn't exist");
        }
        UserEntity userEntity = optionalUserTokenEntity.get().getUser();
        userEntity.setRole("simple user");
        userRepository.save(userEntity);
    }

    public void signout(String token) {
        Optional<UserTokenEntity> optionalUserTokenEntity = userTokenRepository.findOneByToken(token);
        if(!optionalUserTokenEntity.isPresent()){
            throw new NotFoundException("User token doesn't exist");
        }
        userTokenRepository.delete(optionalUserTokenEntity.get());
    }

    public void favouriteAdd(String token, Integer videoId) {
        Optional<UserTokenEntity> optionalUserTokenEntity = userTokenRepository.findOneByToken(token);
        if(!optionalUserTokenEntity.isPresent()){
            throw new NotFoundException("User token doesn't exist");
        }
        UserEntity userEntity = optionalUserTokenEntity.get().getUser();

        Optional<MediaEntity> optionalMediaEntity = mediaRepository.findById(videoId);
        if(!optionalMediaEntity.isPresent()){
            throw new NotFoundException("Video with this id does not exist");
        }
        MediaEntity mediaEntity = optionalMediaEntity.get();

        if(!userEntity.getLikedMedias().contains(mediaEntity)) {
            userEntity.getLikedMedias().add(mediaEntity);
        }
        userRepository.save(userEntity);
    }

    public void favouriteDelete(String token, Integer videoId) {
        Optional<UserTokenEntity> optionalUserTokenEntity = userTokenRepository.findOneByToken(token);
        if(!optionalUserTokenEntity.isPresent()){
            throw new NotFoundException("User token doesn't exist");
        }
        UserEntity userEntity = optionalUserTokenEntity.get().getUser();

        Optional<MediaEntity> optionalMediaEntity = mediaRepository.findById(videoId);
        if(!optionalMediaEntity.isPresent()){
            throw new NotFoundException("Video with this id does not exist");
        }
        MediaEntity mediaEntity = optionalMediaEntity.get();

        if (!userEntity.getLikedMedias().contains(mediaEntity)) {
            throw new ForbiddenException("user does not like this video");
        }

        userEntity.getLikedMedias().remove(mediaEntity);
        userRepository.save(userEntity);
    }

    public List<SmallVideoDto> favouriteGet(String token) {
        Optional<UserTokenEntity> optionalUserTokenEntity = userTokenRepository.findOneByToken(token);
        if(!optionalUserTokenEntity.isPresent()){
            throw new NotFoundException("User token doesn't exist");
        }
        UserEntity userEntity = optionalUserTokenEntity.get().getUser();
        List<MediaEntity> likedMedias = userEntity.getLikedMedias();
        List<SmallVideoDto> favourites = new ArrayList<>();
        for (int i = 0; i < likedMedias.size(); i++) {
            favourites.add(SmallVideoDto.buildFrom(
                    likedMedias.get(i).getUrlSplashScreen(),
                    likedMedias.get(i).getUrlVideo(),
                    likedMedias.get(i).getMediaName(),
                    likedMedias.get(i).getId(),
                    likedMedias.get(i).getArtist().getFirstName(),
                    likedMedias.get(i).getArtist().getSecondName(),
                    likedMedias.get(i).getType())
            );
        }
        return favourites;
    }

    public List<SmallVideoDto> recordings(String token) {
        Optional<UserTokenEntity> optionalUserTokenEntity = userTokenRepository.findOneByToken(token);
        if(!optionalUserTokenEntity.isPresent()){
            throw new NotFoundException("User token doesn't exist");
        }
        UserEntity userEntity = optionalUserTokenEntity.get().getUser();
        List<MediaEntity> medias = userEntity.getMedias();
        List<SmallVideoDto> recordings = new ArrayList<>();
        for (int i = 0; i < medias.size(); i++) {
            recordings.add(SmallVideoDto.buildFrom(
                    medias.get(i).getUrlSplashScreen(),
                    medias.get(i).getUrlVideo(),
                    medias.get(i).getMediaName(),
                    medias.get(i).getId(),
                    medias.get(i).getArtist().getFirstName(),
                    medias.get(i).getArtist().getSecondName(),
                    medias.get(i).getType()
            ));
        }
        return recordings;
    }

    public void uploadPhoto(String token, MultipartFile photo) throws Exception {
        Optional<UserTokenEntity> optionalUserTokenEntity = userTokenRepository.findOneByToken(token);
        if(!optionalUserTokenEntity.isPresent()){
            throw new NotFoundException("User token doesn't exist");
        }
        UserEntity userEntity = optionalUserTokenEntity.get().getUser();

        String extensionImage = photo.getContentType().split("/")[1];
        String pathImage = "/" + Integer.toString(userEntity.getId()) + "/" +
                           "user_image" + "." + extensionImage;

        HttpClient client = HttpClient.newHttpClient();
        String imageUrl = "https://storage.yandexcloud.net/angimehub" + pathImage;
        HttpRequest requestScreen = HttpRequest.newBuilder()
                .uri(new URI(imageUrl))
                .headers("Content-Type", photo.getContentType())
                .PUT(HttpRequest.BodyPublishers.ofByteArray(photo.getBytes()))
                .build();
        HttpResponse<String> responseScreen = client.send(requestScreen, HttpResponse.BodyHandlers.ofString());

        userEntity.setImageUrl(imageUrl);
        userRepository.save(userEntity);
    }

    public Integer pop(Integer id) {
        Optional<UserEntity> optionalUserEntity = userRepository.findById(id);
        return optionalUserEntity.get().getPopularness();
    }
}