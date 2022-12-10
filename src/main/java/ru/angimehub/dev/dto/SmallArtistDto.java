package ru.angimehub.dev.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Time;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SmallArtistDto {
    @JsonProperty
    private Integer artistId;
    @JsonProperty
    private String artistFirstName;
    @JsonProperty
    private String artistSecondName;
    @JsonProperty
    private String profileImage;

    public static SmallArtistDto buildFrom(Integer artistId, String artistFirstName, String artistSecondName, String profileImage) {
        SmallArtistDto smallArtistDto = new SmallArtistDto();
        smallArtistDto.setArtistId(artistId);
        smallArtistDto.setProfileImage(profileImage);
        smallArtistDto.setArtistFirstName(artistFirstName);
        smallArtistDto.setArtistSecondName(artistSecondName);
        return smallArtistDto;
    }
}
