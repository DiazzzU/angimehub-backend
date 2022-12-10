package ru.angimehub.dev.controller;

import org.springframework.web.multipart.MultipartFile;
import ru.angimehub.dev.dto.FullArtistDto;
import ru.angimehub.dev.dto.SmallArtistDto;
import ru.angimehub.dev.dto.SmallVideoDto;
import ru.angimehub.dev.dto.UserDto;
import ru.angimehub.dev.service.ArtistService;
import ru.angimehub.dev.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
public class ArtistController {
    private final ArtistService artistService;
    private final String getPopular = "/artist/getPopular";
    private final String getInfo = "artist/getInfo";

    @GetMapping(getPopular)
    private ResponseEntity<List<SmallArtistDto>> getPopular() {
        return ok(artistService.getPopular());
    }

    @GetMapping(getInfo)
    private ResponseEntity<FullArtistDto> getInfo(@RequestParam("artistId") Integer id) {
        return ok(artistService.artistInfo(id));
    }
}
