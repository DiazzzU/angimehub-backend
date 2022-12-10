package ru.angimehub.dev.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.angimehub.dev.dto.FullVideoDto;
import ru.angimehub.dev.dto.SearchDto;
import ru.angimehub.dev.dto.SmallVideoDto;
import ru.angimehub.dev.service.MediaService;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
public class MediaController {
    private final MediaService mediaService;
    private final static String getPopularPodcasts = "/podcast/getPopulars";
    private final static String uploadPodcasts = "/podcast/upload";
    private final static String getPopularStandUps = "/standup/getPopulars";
    private final static String uploadStandUps = "/standup/upload";
    private final static String fullMediaInfo = "media/videoFullInfo";
    private final static String deleteMedia = "media/delete";
    private final static String search = "search";

    @GetMapping(getPopularPodcasts)
    private ResponseEntity<List<SmallVideoDto>> getPopularPodcast() throws Exception {
        return ok(mediaService.getPopularUrls(1));
    }

    @RequestMapping(value = uploadPodcasts, method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    private ResponseEntity<Object> uploadPodcast(@RequestParam("video") MultipartFile video, @RequestParam("splash_screen") MultipartFile splashScreen, @RequestParam String name, @RequestParam String token) throws Exception {
        mediaService.uploadVideo(name, video, splashScreen, 1, token);
        return ok().build();
    }

    @GetMapping(getPopularStandUps)
    private ResponseEntity<List<SmallVideoDto>> getPopularStandUps() throws Exception {
        return ok(mediaService.getPopularUrls(0));
    }

    @RequestMapping(value = uploadStandUps, method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    private ResponseEntity<Object> uploadStandup(@RequestParam("video") MultipartFile video, @RequestParam("splash_screen") MultipartFile splashScreen, @RequestParam String name, @RequestParam String token) throws Exception {
        mediaService.uploadVideo(name, video, splashScreen, 0, token);
        return ok().build();
    }

    @GetMapping(fullMediaInfo)
    private ResponseEntity<FullVideoDto> fullMediaInfo(@RequestParam("videoId") Integer videoId) {
        return ok(mediaService.videoInfo(videoId));
    }

    @PostMapping(deleteMedia)
    private void remMedia(@RequestParam("videoId") Integer id) throws Exception {
        mediaService.deleteMedia(id);
    }

    @GetMapping(search)
    private ResponseEntity<SearchDto> search(@RequestParam("searchValue") String value) {
        return ok(mediaService.searchByName(value));
    }
}
