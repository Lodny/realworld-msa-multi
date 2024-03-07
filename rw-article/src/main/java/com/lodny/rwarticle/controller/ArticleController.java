package com.lodny.rwarticle.controller;

import com.lodny.rwarticle.entity.Article;
import com.lodny.rwarticle.entity.dto.ArticleParam;
import com.lodny.rwarticle.entity.dto.ArticleResponse;
import com.lodny.rwarticle.entity.dto.RegisterArticleRequest;
import com.lodny.rwarticle.entity.wrapper.WrapArticleResponses;
import com.lodny.rwarticle.entity.wrapper.WrapRegisterArticleRequest;
import com.lodny.rwarticle.service.ArticleService;
import com.lodny.rwcommon.annotation.JwtTokenRequired;
import com.lodny.rwcommon.annotation.LoginUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/articles")
public class ArticleController {

    private final ArticleService articleService;

    @JwtTokenRequired
    @PostMapping
    public ResponseEntity<?> registerArticle(@RequestBody final WrapRegisterArticleRequest wrapRegisterArticleRequest,
                                             @LoginUser final Map<String, Object> loginInfo) {
        RegisterArticleRequest registerArticleRequest = wrapRegisterArticleRequest.article();
        log.info("registerArticle() : registerArticleRequest={}", registerArticleRequest);
        log.info("registerArticle() : loginInfo={}", loginInfo);

        Article registeredArticle = articleService.registerArticle(
                registerArticleRequest,
                (Long)loginInfo.get("userId"),
                (String)loginInfo.get("token"));
        log.info("registerArticle() : registeredArticle={}", registeredArticle);

//        return ArticleResponse.of(
//                savedArticle,
//                ProfileResponse.of(loginUser, false),
//                false,
//                0L);

//        return ResponseEntity.status(HttpStatus.CREATED).body(new WrapArticleResponse(articleResponse));
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredArticle);
    }

    @GetMapping("/{slug}")
    public ResponseEntity<?> getArticleBySlug(@PathVariable final String slug,
                                              @LoginUser final Map<String, Object> loginInfo) {
        log.info("getArticleBySlug() : slug={}", slug);
        log.info("getArticleBySlug() : loginInfo={}", loginInfo);

//        ArticleResponse articleResponse = articleService.getArticleBySlug(slug, loginUser);
//        log.info("getArticleBySlug() : articleResponse={}", articleResponse);

        return ResponseEntity.ok("new WrapArticleResponse(articleResponse)");
    }

    @JwtTokenRequired
    @GetMapping("/feed")
    public ResponseEntity<?> getFeedArticle(@ModelAttribute final ArticleParam articleParam,
                                            @LoginUser final Map<String, Object> loginInfo) {
        log.info("getFeedArticle() : articleParam={}", articleParam);
        log.info("getFeedArticle() : loginInfo={}", loginInfo);

        PageRequest pageRequest = getPageRequest(articleParam);
        log.info("getFeedArticle() : pageRequest={}", pageRequest);

//        final Page<ArticleResponse> pageArticles = articleService.getFeedArticles((long)loginInfo.get("userId"), pageRequest);
//        log.info("getFeedArticle() : pageArticles={}", pageArticles);

        return ResponseEntity.ok("new WrapArticleResponses(pageArticles)");
    }

    @GetMapping
    public ResponseEntity<?> getArticles(@ModelAttribute final ArticleParam articleParam,
                                         @LoginUser final Map<String, Object> loginInfo) {
        log.info("getArticles() : articleParam={}", articleParam);

        PageRequest pageRequest = getPageRequest(articleParam);
        log.info("getArticles() : pageRequest={}", pageRequest);

        final var loginUserId = loginInfo != null ? (Long)loginInfo.get("userId") : -1L;
        log.info("getArticles() : loginUserId={}", loginUserId);

        final var token = loginInfo != null ? (String)loginInfo.get("token") : "";

        final Page<ArticleResponse> pageArticles =
                switch (articleParam.type()) {
//                    case "tag"       -> articleService.getArticlesByTag(articleParam.tag(), loginUserId, pageRequest);
//                    case "author"    -> articleService.getArticlesByAuthor(articleParam.author(), loginUserId, pageRequest);
//                    case "favorited" -> articleService.getArticlesByFavorited(articleParam.favorited(), loginUserId, pageRequest);
                    default          -> articleService.getArticles(pageRequest, loginUserId, token);
                };
        log.info("getArticles() : pageArticles={}", pageArticles);

        return ResponseEntity.ok(new WrapArticleResponses(pageArticles));
    }

    private static PageRequest getPageRequest(final ArticleParam articleParam) {
        int pageSize = articleParam.limit();
        int pageNo = articleParam.offset() / pageSize;

        return PageRequest.of(pageNo, pageSize);
    }

    @JwtTokenRequired
    @DeleteMapping("/{slug}")
    public ResponseEntity<?> deleteArticleBySlug(@PathVariable final String slug,
                                                 @LoginUser final Map<String, Object> loginInfo) {
        log.info("deleteArticleBySlug() : slug={}", slug);

//        int count = articleService.deleteArticleBySlug(slug, loginUser.id());
//        log.info("deleteArticleBySlug() : count={}", count);

        return ResponseEntity.ok(1);
    }

    @GetMapping("/{slug}/id")
    public ResponseEntity<?> getArticleIdBySlug(@PathVariable final String slug) {
        log.info("getArticleIdBySlug() : slug={}", slug);

        Long articleId = articleService.getArticleIdBySlug(slug);
        log.info("getArticleIdBySlug() : articleId={}", articleId);

        return ResponseEntity.ok(articleId);
    }
}
