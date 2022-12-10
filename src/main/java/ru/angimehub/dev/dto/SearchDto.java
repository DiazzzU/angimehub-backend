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
public class SearchDto {
    @JsonProperty
    private List<SmallArtistDto> artists;
    @JsonProperty
    private List<SmallVideoDto> standups;
    @JsonProperty
    private List<SmallVideoDto> podcasts;
}
