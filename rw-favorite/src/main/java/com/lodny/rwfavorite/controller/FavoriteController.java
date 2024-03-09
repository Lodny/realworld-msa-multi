package com.lodny.rwfavorite.controller;

import com.lodny.rwcommon.annotation.JwtTokenRequired;
import com.lodny.rwcommon.annotation.LoginUser;
import com.lodny.rwcommon.properties.JwtProperty;
import com.lodny.rwfavorite.entity.wrapper.WrapArticleResponse;
import com.lodny.rwfavorite.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/articles")
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final RestTemplate restTemplate;
    private final JwtProperty jwtProperty;

    private String getTokenByLoginInfo(final Map<String, Object> loginInfo) {
        return loginInfo != null ? (String) loginInfo.get("token") : "";
    }

    private long getLoginUserId(final Map<String, Object> loginInfo) {
        return loginInfo == null ? -1L : (long) loginInfo.get("userId");
    }

    @JwtTokenRequired
    @PostMapping("/{slug}/favorite")
    public ResponseEntity<?> favorite(@PathVariable final String slug,
                                      @LoginUser final Map<String, Object> loginInfo) {
        log.info("favorite() : slug={}", slug);
        log.info("favorite() : loginInfo={}", loginInfo);

        final var loginUserId = getLoginUserId(loginInfo);
        final var articleId = getArticleIdFromRestTemplate(slug);
        favoriteService.favorite(articleId, loginUserId);

        WrapArticleResponse wrapArticleResponse = getArticleResponseFromRestTemplate(slug, loginInfo);
        log.info("favorite() : wrapArticleResponse.article()={}", wrapArticleResponse.article());

        return ResponseEntity.status(HttpStatus.CREATED).body(wrapArticleResponse);
    }

    @JwtTokenRequired
    @DeleteMapping("/{slug}/favorite")
    public ResponseEntity<?> unfavorite(@PathVariable final String slug,
                                        @LoginUser final Map<String, Object> loginInfo) {
        log.info("unfavorite() : slug={}", slug);
        log.info("unfavorite() : loginInfo={}", loginInfo);

        final var loginUserId = getLoginUserId(loginInfo);
        final var articleId = getArticleIdFromRestTemplate(slug);
        favoriteService.unfavorite(articleId, loginUserId);

        WrapArticleResponse wrapArticleResponse = getArticleResponseFromRestTemplate(slug, loginInfo);
        log.info("unfavorite() : wrapArticleResponse.article()={}", wrapArticleResponse.article());

        return ResponseEntity.ok(wrapArticleResponse);
    }

    private WrapArticleResponse getArticleResponseFromRestTemplate(final String slug,
                                                               final Map<String, Object> loginInfo) {
        log.info("getArticleResponseFromRestTemplate() : slug={}", slug);

        final var token = getTokenByLoginInfo(loginInfo);

        HttpHeaders headers = new HttpHeaders();
        if (StringUtils.hasText(token))
            headers.set("Authorization", jwtProperty.getTokenTitle() + token);

        ResponseEntity<WrapArticleResponse> response = restTemplate.exchange(
                "http://localhost:8080/api/articles/" + slug,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                WrapArticleResponse.class);

        return response.getBody();
    }

    private Long getArticleIdFromRestTemplate(final String slug) {  //}, final String token) {
        ResponseEntity<Long> response = restTemplate.exchange(
//                FollowController.API_URL + "/users/" + username + "/id",
                "http://localhost:8080/api/articles/" + slug + "/id",
                HttpMethod.GET,
                new HttpEntity<>(new HttpHeaders()),
                Long.class);

        return response.getBody();
    }

    @GetMapping("/{articleId}/favorite-info")
    public ResponseEntity<?> favoriteInfo(@PathVariable final Long articleId,
                                          @LoginUser final Map<String, Object> loginInfo) {
        log.info("favoriteInfo() : articleId={}", articleId);
        log.info("favoriteInfo() : loginInfo={}", loginInfo);

        final var loginUserId = getLoginUserId(loginInfo);
        Long[] favoriteInfo = favoriteService.favoriteInfo(articleId, loginUserId);
        log.info("favoriteInfo() : favoriteInfo={}", favoriteInfo);

        return ResponseEntity.ok(favoriteInfo);
    }
}
