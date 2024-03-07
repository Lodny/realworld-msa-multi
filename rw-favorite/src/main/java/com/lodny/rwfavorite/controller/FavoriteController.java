package com.lodny.rwfavorite.controller;

import com.lodny.rwcommon.annotation.JwtTokenRequired;
import com.lodny.rwcommon.annotation.LoginUser;
import com.lodny.rwcommon.properties.JwtProperty;
import com.lodny.rwfavorite.entity.Favorite;
import com.lodny.rwfavorite.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/articles/{slug}")
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final RestTemplate restTemplate;
    private final JwtProperty jwtProperty;

    @JwtTokenRequired
    @PostMapping("/favorite")
    public ResponseEntity<?> favorite(@PathVariable final String slug,
                                      @LoginUser final Map<String, Object> loginInfo) {
        log.info("favorite() : slug={}", slug);
        log.info("favorite() : loginInfo={}", loginInfo);

        final var loginUserId = (long)loginInfo.get("userId");
//        Long articleId = getArticleIdFromRestTemplate(slug, (String)loginInfo.get("token"));
        Long articleId = getArticleIdFromRestTemplate(slug);
        Favorite savedFavorite = favoriteService.favorite(articleId, loginUserId);
        log.info("favorite() : savedFavorite={}", savedFavorite);

//        ArticleResponse articleResponse = favoriteService.favorite(slug, loginUserId);
//        log.info("favorite() : articleResponse={}", articleResponse);

//        return ResponseEntity.status(HttpStatus.CREATED).body(new WrapArticleResponse(articleResponse));
        return ResponseEntity.status(HttpStatus.CREATED).body("hello");
    }

    @JwtTokenRequired
    @DeleteMapping("/favorite")
    public ResponseEntity<?> unfavorite(@PathVariable final String slug,
                                        @LoginUser final Map<String, Object> loginInfo) {
        log.info("unfavorite() : slug={}", slug);
        log.info("unfavorite() : loginInfo={}", loginInfo);

//        ArticleResponse articleResponse = favoriteService.unfavorite(slug, loginUser.id());
//        log.info("unfavorite() : articleResponse={}", articleResponse);
//
        return ResponseEntity.status(HttpStatus.CREATED).body("new WrapArticleResponse(articleResponse)");
    }

    private Long getArticleIdFromRestTemplate(final String slug) {  //}, final String token) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", jwtProperty.getTokenTitle() + token);
//        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Long> response = restTemplate.exchange(
//                FollowController.API_URL + "/users/" + username + "/id",
                "http://localhost:8080/api/articles/" + slug + "/id",
                HttpMethod.GET,
//                entity,
                new HttpEntity<>(new HttpHeaders()),
                Long.class);

        return response.getBody();
    }
}
