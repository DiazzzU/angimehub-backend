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
public class SmallVideoDto {
    @JsonProperty
    private String urlImage;
    @JsonProperty
    private String urlVideo;
    @JsonProperty
    private String mediaName;
    @JsonProperty
    private Integer idVideo;
    @JsonProperty
    private String artistFirstName;
    @JsonProperty
    private String artistSecondName;
    @JsonProperty
    private Integer type;

    public static SmallVideoDto buildFrom(String urlImage, String urlVideo, String mediaName, Integer idVideo, String artistFirstName, String artistSecondName, Integer type) {
        SmallVideoDto smallVideoDto = new SmallVideoDto();
        smallVideoDto.setUrlImage(urlImage);
        smallVideoDto.setMediaName(mediaName);
        smallVideoDto.setArtistFirstName(artistFirstName);
        smallVideoDto.setArtistSecondName(artistSecondName);
        smallVideoDto.setIdVideo(idVideo);
        smallVideoDto.setUrlVideo(urlVideo);
        smallVideoDto.setType(type);
        return smallVideoDto;
    }
}
