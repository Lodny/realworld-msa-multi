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
        log.info("[C] registerArticle() : registerArticleRequest={}", registerArticleRequest);
        log.info("[C] registerArticle() : loginInfo={}", loginInfo);

        Article registeredArticle = articleService.registerArticle(
                registerArticleRequest,
                Long.parseLong((String)loginInfo.get("userId")),
                (String)loginInfo.get("token"));
        log.info("[C] registerArticle() : registeredArticle={}", registeredArticle);

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
        log.info("[C] getArticleBySlug() : slug={}", slug);
        log.info("[C] getArticleBySlug() : loginInfo={}", loginInfo);

//        ArticleResponse articleResponse = articleService.getArticleBySlug(slug, loginUser);
//        log.info("[C] getArticleBySlug() : articleResponse={}", articleResponse);

        return ResponseEntity.ok("new WrapArticleResponse(articleResponse)");
    }

    @JwtTokenRequired
    @GetMapping("/feed")
    public ResponseEntity<?> getFeedArticle(@ModelAttribute final ArticleParam articleParam,
                                            @LoginUser final Map<String, Object> loginInfo) {
        log.info("[C] getFeedArticle() : articleParam={}", articleParam);
        log.info("[C] getFeedArticle() : loginInfo={}", loginInfo);

        PageRequest pageRequest = getPageRequest(articleParam);
        log.info("[C] getFeedArticle() : pageRequest={}", pageRequest);

//        final Page<ArticleResponse> pageArticles = articleService.getFeedArticles((long)loginInfo.get("userId"), pageRequest);
//        log.info("[C] getFeedArticle() : pageArticles={}", pageArticles);

        return ResponseEntity.ok("new WrapArticleResponses(pageArticles)");
    }

    @GetMapping
    public ResponseEntity<?> getArticles(@ModelAttribute final ArticleParam articleParam,
                                         @LoginUser final Map<String, Object> loginInfo) {
        log.info("[C] getArticles() : articleParam={}", articleParam);

        PageRequest pageRequest = getPageRequest(articleParam);
        log.info("[C] getArticles() : pageRequest={}", pageRequest);

        final var loginUserId = loginInfo != null ? (long)loginInfo.get("userId") : -1;
        log.info("[C] getArticles() : loginUserId={}", loginUserId);

        final Page<ArticleResponse> pageArticles =
                switch (articleParam.type()) {
//                    case "tag"       -> articleService.getArticlesByTag(articleParam.tag(), loginUserId, pageRequest);
//                    case "author"    -> articleService.getArticlesByAuthor(articleParam.author(), loginUserId, pageRequest);
//                    case "favorited" -> articleService.getArticlesByFavorited(articleParam.favorited(), loginUserId, pageRequest);
                    default          -> articleService.getArticles(pageRequest, loginUserId);
                };
        log.info("[C] getArticles() : pageArticles={}", pageArticles);

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
        log.info("[C] deleteArticleBySlug() : slug={}", slug);

//        int count = articleService.deleteArticleBySlug(slug, loginUser.id());
//        log.info("[C] deleteArticleBySlug() : count={}", count);

        return ResponseEntity.ok(1);
    }
}
