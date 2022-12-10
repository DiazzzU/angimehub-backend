package ru.angimehub.dev.service;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.angimehub.dev.dto.FullArtistDto;
import ru.angimehub.dev.dto.FullVideoDto;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArtistService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserTokenRepository userTokenRepository;
    @Autowired
    private MediaRepository mediaRepository;

    public List<SmallArtistDto> getPopular() {
        List<UserEntity> popularArtists = userRepository.findTop100ByRoleOrderByPopularnessDesc("artist");
        List<SmallArtistDto> smallPopularArtists = new ArrayList<>();
        for (int i = 0; i < popularArtists.size(); i++) {
            smallPopularArtists.add(SmallArtistDto.buildFrom(
                    popularArtists.get(i).getId(),
                    popularArtists.get(i).getFirstName(),
                    popularArtists.get(i).getSecondName(),
                    popularArtists.get(i).getImageUrl()
            ));
        }
        return smallPopularArtists;
    }

    public FullArtistDto artistInfo(Integer artistId) {
        Optional<UserEntity> optionalUserEntity = userRepository.findById(artistId);
        if(optionalUserEntity.isEmpty()) {
            throw new NotFoundException("User does't exist");
        }
        UserEntity userEntity = optionalUserEntity.get();

        if(!userEntity.getRole().equals("artist")) {
            throw new ForbiddenException("User is not artist");
        }

        List<MediaEntity> userMedias = userEntity.getMedias();
        List<SmallVideoDto> userStandups = new ArrayList<>();
        List<SmallVideoDto> userPodcasts = new ArrayList<>();
        for (int i = 0; i < userMedias.size(); i++) {
            if (userMedias.get(i).getType() == 0) {
                userStandups.add(userMedias.get(i).toSmallVideoDto());
            }
            else {
                userPodcasts.add(userMedias.get(i).toSmallVideoDto());
            }
        }
        return FullArtistDto.buildFrom(
                artistId,
                userEntity.getFirstName(),
                userEntity.getSecondName(),
                userEntity.getImageUrl(),
                userStandups,
                userPodcasts
        );
    }
}
