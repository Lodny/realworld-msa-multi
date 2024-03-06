package com.lodny.rwfavorite.controller;

import com.lodny.rwcommon.annotation.JwtTokenRequired;
import com.lodny.rwcommon.annotation.LoginUser;
import com.lodny.rwfavorite.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/articles/{slug}")
public class FavoriteController {

    private final FavoriteService favoriteService;

    @JwtTokenRequired
    @PostMapping("/favorite")
    public ResponseEntity<?> favorite(@PathVariable final String slug,
                                      @LoginUser final Map<String, String> loginInfo) {
        log.info("[C] favorite() : slug={}", slug);
        log.info("[C] favorite() : loginInfo={}", loginInfo);

//        Long loginUserId = getUserIdFromRestTemplate(username, loginInfo.get("token"));
//        ArticleResponse articleResponse = favoriteService.favorite(slug, loginUserId);
//        log.info("[C] favorite() : articleResponse={}", articleResponse);

//        return ResponseEntity.status(HttpStatus.CREATED).body(new WrapArticleResponse(articleResponse));
        return ResponseEntity.status(HttpStatus.CREATED).body("hello");
    }

    @JwtTokenRequired
    @DeleteMapping("/favorite")
    public ResponseEntity<?> unfavorite(@PathVariable final String slug,
                                        @LoginUser final Map<String, String> loginInfo) {
        log.info("[C] unfavorite() : slug={}", slug);
        log.info("[C] unfavorite() : loginInfo={}", loginInfo);

//        ArticleResponse articleResponse = favoriteService.unfavorite(slug, loginUser.id());
//        log.info("[C] unfavorite() : articleResponse={}", articleResponse);
//
        return ResponseEntity.status(HttpStatus.CREATED).body("new WrapArticleResponse(articleResponse)");
    }

}
