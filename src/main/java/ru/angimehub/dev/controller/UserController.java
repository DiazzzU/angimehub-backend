package ru.angimehub.dev.controller;

import org.springframework.web.multipart.MultipartFile;
import ru.angimehub.dev.dto.SmallVideoDto;
import ru.angimehub.dev.dto.UserDto;
import ru.angimehub.dev.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final static String userRegister = "/user/register";
    private final static String userLogin = "/user/login";
    private final static String userInfo = "/user/info";
    private final static String becomeArtist = "/user/becomeartist";
    private final static String becomeSimple = "/user/becomesimple";
    private final static String signout = "/user/signout";
    private final static String favouriteAdd = "user/favourite/add";
    private final static String favouriteDelete = "user/favourite/delete";
    private final static String favouriteGet = "user/favourite/get";
    private final static String recordings = "user/recordings";
    private final static String uploadPhoto = "user/uploadPhoto";

    @PostMapping(userRegister)
    private void register(@RequestBody UserDto userDto) {
        userService.register(userDto);
    }

    @GetMapping(userLogin)
    private ResponseEntity<String> login(@RequestParam("email") String email, @RequestParam("password") String password) throws Exception {
        String token = userService.login(email, password);
        return ok(token);
    }

    @GetMapping(userInfo)
    private ResponseEntity<Map<String, String>> info(@RequestParam("token") String token) throws Exception {
        return ok(userService.info(token));
    }

    @PostMapping(becomeArtist)
    private void becomeArtist(@RequestParam("token") String token) throws Exception {
        userService.becomeArtist(token);
    }

    @PostMapping(signout)
    private void signout(@RequestParam("token") String token) throws Exception {
        userService.signout(token);
    }

    @PostMapping(favouriteAdd)
    private void favouriteAdd(@RequestParam("token") String token, @RequestParam("videoId") Integer videoId) {
        userService.favouriteAdd(token, videoId);
    }

    @PostMapping(favouriteDelete)
    private void favouriteDelete(@RequestParam("token") String token, @RequestParam("videoId") Integer videoId) {
        userService.favouriteDelete(token, videoId);
    }

    @GetMapping(favouriteGet)
    private ResponseEntity<List<SmallVideoDto>> favouriteGet(@RequestParam("token") String token) {
        return ok(userService.favouriteGet(token));
    }

    @GetMapping(recordings)
    private ResponseEntity<List<SmallVideoDto>> recordings(@RequestParam("token") String token) {
        return ok(userService.recordings(token));
    }

    @PostMapping(uploadPhoto)
    private void uploadPhoto(@RequestParam("token") String token, @RequestParam("photo") MultipartFile photo) throws Exception {
        userService.uploadPhoto(token, photo);
    }

    /*@GetMapping("/user/popularness")
    private ResponseEntity<Integer> pop(@RequestParam("userId") Integer id) {
        return ok(userService.pop(id));
    }*/

}
