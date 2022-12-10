package ru.angimehub.dev.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Time;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FullArtistDto {
    @JsonProperty
    private Integer artistId;
    @JsonProperty
    private String artistFirstName;
    @JsonProperty
    private String artistSecondName;
    @JsonProperty
    private String profileImage;
    @JsonProperty
    private List<SmallVideoDto> standups;
    @JsonProperty
    private List<SmallVideoDto> podcasts;

    public static FullArtistDto buildFrom(Integer artistId, String artistFirstName, String artistSecondName, String profileImage, List<SmallVideoDto> standups, List<SmallVideoDto> podcasts) {
        FullArtistDto fullArtistDto = new FullArtistDto();
        fullArtistDto.setArtistId(artistId);
        fullArtistDto.setArtistFirstName(artistFirstName);
        fullArtistDto.setArtistSecondName(artistSecondName);
        fullArtistDto.setProfileImage(profileImage);
        fullArtistDto.setPodcasts(podcasts);
        fullArtistDto.setStandups(standups);

        return fullArtistDto;
    }
}
