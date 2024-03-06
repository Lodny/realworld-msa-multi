package com.lodny.rwfollow.controller;

import com.lodny.rwcommon.annotation.JwtTokenRequired;
import com.lodny.rwcommon.annotation.LoginUser;
import com.lodny.rwcommon.properties.JwtProperty;
import com.lodny.rwfollow.entity.Follow;
import com.lodny.rwfollow.entity.wrapper.WrapProfileResponse;
import com.lodny.rwfollow.service.FollowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/profiles/{username}")
public class FollowController {

    private final FollowService followService;
    private final RestTemplate restTemplate;
    private final JwtProperty jwtProperty;

//    private static final String API_URL = "http://localhost:8080/api";

    @JwtTokenRequired
    @PostMapping("/follow")
    public ResponseEntity<?> follow(@PathVariable final String username,
                                    @LoginUser final Map<String, String> loginInfo) {
        log.info("[C] follow() : username={}", username);
        log.info("[C] follow() : loginInfo={}", loginInfo);

        Long followeeId = getUserIdFromRestTemplate(username, loginInfo.get("token"));
        log.info("[C] follow() : followeeId={}", followeeId);

        long followerId = Long.parseLong(loginInfo.get("userId"));
        Follow follow = followService.follow(followeeId, followerId);
        log.info("follow() : follow={}", follow);

        WrapProfileResponse wrapProfileResponse = getProfileResponse(username);
        wrapProfileResponse.profile().setFollowing(true);

        return ResponseEntity.ok(wrapProfileResponse);
    }

    @JwtTokenRequired
    @DeleteMapping("/follow")
    public ResponseEntity<?> unfollow(@PathVariable final String username,
                                      @LoginUser final Map<String, String> loginInfo) {
        log.info("[C] unfollow() : username={}", username);
        log.info("[C] unfollow() : loginInfo={}", loginInfo);

        Long followeeId = getUserIdFromRestTemplate(username, loginInfo.get("token"));
        log.info("[C} unfollow() : followeeId={}", followeeId);

        long followerId = Long.parseLong(loginInfo.get("userId"));
        followService.unfollow(followeeId, followerId);

        WrapProfileResponse wrapProfileResponse = getProfileResponse(username);
        wrapProfileResponse.profile().setFollowing(false);

        return ResponseEntity.ok(wrapProfileResponse);
    }

    @JwtTokenRequired
    @GetMapping("/follow")
    public ResponseEntity<?> isFollow(@PathVariable final String username,
                                      @LoginUser final Map<String, String> loginInfo) {
        log.info("[C] isFollow() : username={}", username);
        log.info("[C] isFollow() : loginInfo={}", loginInfo);

        Long followeeId = getUserIdFromRestTemplate(username, loginInfo.get("token"));
        log.info("[C] isFollow() : followeeId={}", followeeId);

        long followerId = Long.parseLong(loginInfo.get("userId"));
        Boolean following = followService.isFollow(followerId, followerId);
        log.info("[C] isFollow() : following={}", following);

        return ResponseEntity.ok(following);
    }

    private WrapProfileResponse getProfileResponse(final String username) {
        ResponseEntity<WrapProfileResponse> response = restTemplate.exchange(
//                FollowController.API_URL + "/profiles/" + username,
                "http://localhost:8080/api/profiles/" + username,
                HttpMethod.GET,
                new HttpEntity<String>(new HttpHeaders()),
                WrapProfileResponse.class);

        return response.getBody();
    }

    private Long getUserIdFromRestTemplate(final String username, final String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtProperty.getTokenTitle() + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Long> response = restTemplate.exchange(
//                FollowController.API_URL + "/users/" + username + "/id",
                "http://localhost:8080/api/users/" + username + "/id",
                HttpMethod.GET,
                entity,
                Long.class);

        return response.getBody();
    }
}
