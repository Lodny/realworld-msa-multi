package com.lodny.rwuser.controller;

import com.lodny.rwuser.entity.dto.ProfileResponse;
import com.lodny.rwuser.entity.dto.UserResponse;
import com.lodny.rwuser.entity.wrapper.WrapProfileResponse;
import com.lodny.rwuser.service.ProfileService;
import com.lodny.rwcommon.annotation.LoginUser;
import com.lodny.rwcommon.util.ImageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/profiles")
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/{username}")
    public ResponseEntity<?> getProfile(@PathVariable final String username,
                                        @LoginUser final UserResponse loginUser) {
        log.info("[C] getProfile() : username={}", username);
        log.info("[C] getProfile() : loginUser={}", loginUser);

        ProfileResponse profileResponse = profileService.getProfile(username, loginUser);
        profileResponse.setImage(ImageUtil.nullToDefaultImage(profileResponse.getImage()));
        log.info("[C] getProfile() : profileResponse={}", profileResponse);

        return ResponseEntity.ok(new WrapProfileResponse(profileResponse));
    }

    @GetMapping("/by-id/{id}")
    public ResponseEntity<?> getProfileById(@PathVariable final Long id,
                                            @LoginUser final UserResponse loginUser) {
        log.info("[C] getProfileById() : id={}", id);
        log.info("[C] getProfile() : loginUser={}", loginUser);

        ProfileResponse profileResponse = profileService.getProfileById(id, loginUser);
        profileResponse.setImage(ImageUtil.nullToDefaultImage(profileResponse.getImage()));
        log.info("[C] getProfile() : profileResponse={}", profileResponse);

        return ResponseEntity.ok(profileResponse);
    }
}
