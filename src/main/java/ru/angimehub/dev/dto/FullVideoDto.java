package ru.angimehub.dev.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FullVideoDto {
    @JsonProperty
    private String urlVideo;
    @JsonProperty
    private String urlImage;
    @JsonProperty
    private String mediaName;
    @JsonProperty
    private Integer idVideo;
    @JsonProperty
    private Integer artistId;
    @JsonProperty
    private String artistFirstName;
    @JsonProperty
    private String artistSecondName;
    @JsonProperty
    private String artistImage;
    @JsonProperty
    private Integer type;
    @JsonProperty
    private Integer views;
}
