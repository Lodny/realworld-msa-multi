package com.lodny.rwarticle.service;

import com.lodny.rwarticle.entity.Article;
import com.lodny.rwarticle.entity.dto.ArticleResponse;
import com.lodny.rwarticle.entity.dto.ProfileResponse;
import com.lodny.rwarticle.entity.dto.RegisterArticleRequest;
import com.lodny.rwarticle.repository.ArticleRepository;
import com.lodny.rwcommon.properties.JwtProperty;
import com.lodny.rwcommon.util.LoginInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final RestTemplate restTemplate;
    private final JwtProperty jwtProperty;

    @Transactional
    public ArticleResponse registerArticle(final RegisterArticleRequest registerArticleRequest,
                                           final LoginInfo loginInfo) {
        Article article = Article.of(registerArticleRequest, loginInfo.getUserId());
        log.info("registerArticle() : article={}", article);

        Article savedArticle = articleRepository.save(article);
        log.info("registerArticle() : savedArticle={}", savedArticle);

        Integer result = registerTagsWithRestTemplate(registerArticleRequest.tagList(), savedArticle.getId(), loginInfo.getToken());
        log.info("registerArticle() : result={}", result);

        ProfileResponse profileResponse = getProfileByIdWithRestTemplate(savedArticle.getAuthorId(), loginInfo.getToken());
        log.info("registerArticle() : profileResponse={}", profileResponse);
        Long[] favoriteInfo = new Long[]{0L, 0L};

        return ArticleResponse.of(
                savedArticle,
                registerArticleRequest.tagList(),
                profileResponse,
                favoriteInfo);
    }

    public Page<ArticleResponse> getArticles(final PageRequest pageRequest,
                                             final Long loginUserId,
                                             final String token) {
        Page<Article> articlePage = articleRepository.findAllByOrderByCreatedAtDesc(pageRequest);
        log.info("getArticles() : articlePage={}", articlePage);

        return getArticleResponses(articlePage, loginUserId, token);
    }

    public Page<ArticleResponse> getArticlesByTag(final PageRequest pageRequest,
                                                  final Long loginUserId,
                                                  final String token,
                                                  final String tag) {
        List<Long> articleIds = getArticleIdsByTagWithRestTemplate(tag);
        log.info("getArticlesByTag() : articleIds={}", articleIds);
        //todo::orderby

        Page<Article> articlePage = articleRepository.findByIdIn(articleIds, pageRequest);
        log.info("getArticlesByTag() : articlePage={}", articlePage);

        return getArticleResponses(articlePage, loginUserId, token);
    }

    private List<Long> getArticleIdsByTagWithRestTemplate(final String tag) {
        ResponseEntity<List> response = restTemplate.exchange(
                "http://localhost:8080/api/tags/" + tag + "/article-ids",
                HttpMethod.GET,
                new HttpEntity<String>(new HttpHeaders()),
                List.class);

        return response.getBody();
    }

    private Integer registerTagsWithRestTemplate(final Set<String> tags, final Long articleId, final String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", jwtProperty.getTokenTitle() + token);

        HttpEntity<Set<String>> requestEntity = new HttpEntity<>(tags, headers);
        ResponseEntity<Integer> response = restTemplate.exchange(
                "http://localhost:8080/api/articles/" + articleId + "/tags/list",
                HttpMethod.POST,
                requestEntity,
                Integer.class);

        return response.getBody();
    }

    private Page<ArticleResponse> getArticleResponses(final Page<Article> articlePage, final Long loginUserId, final String token) {
        List<ArticleResponse> articleResponses = articlePage.getContent().stream()
                .map(article -> {
                    Set<String> tags = getTagsByArticleIdWithRestTemplate(article.getId());
                    ProfileResponse profileResponse = getProfileByIdWithRestTemplate(article.getAuthorId(), token);
                    Long[] favoriteInfo = getFavoriteInfoByArticleIdWithRestTemplate(article.getId(), token);
                    return ArticleResponse.of(article, tags, profileResponse, favoriteInfo);
                })
                .toList();

        log.info("getArticleResponses() : articleResponses={}", articleResponses);

        return new PageImpl<>(articleResponses, articlePage.getPageable(), articlePage.getTotalElements());
    }

    private Set<String> getTagsByArticleIdWithRestTemplate(final Long articleId) {
        ResponseEntity<Set> response = restTemplate.exchange(
                "http://localhost:8080/api/articles/" + articleId + "/tags",
                HttpMethod.GET,
                new HttpEntity<String>(new HttpHeaders()),
                Set.class);

        return response.getBody();
    }

    private ProfileResponse getProfileByIdWithRestTemplate(final Long authorId, final String token) {
        log.info("getProfileByIdWithRestTemplate() : authorId={}", authorId);
        log.info("getProfileByIdWithRestTemplate() : token={}", token);

        HttpHeaders headers = new HttpHeaders();
        if (StringUtils.hasText(token))
            headers.set("Authorization", jwtProperty.getTokenTitle() + token);

        ResponseEntity<ProfileResponse> response = restTemplate.exchange(
                "http://localhost:8080/api/profiles/by-id/" + authorId,
                HttpMethod.GET,
                new HttpEntity<String>(headers),
                ProfileResponse.class);

        return response.getBody();
    }

    public Long getArticleIdBySlug(final String slug) {
        Article foundArticle = articleRepository.findBySlug(slug)
                .orElseThrow(() -> new IllegalArgumentException("article not found"));
        log.info("getArticleIdBySlug() : foundArticle={}", foundArticle);

        return foundArticle.getId();
    }

    @Transactional
    public ArticleResponse getArticleBySlug(final String slug, final String token) {
        log.info("getArticleBySlug() : token={}", token);
        Article foundArticle = articleRepository.findBySlug(slug)
                .orElseThrow(() -> new IllegalArgumentException("article not found"));
        log.info("getArticleBySlug() : foundArticle={}", foundArticle);

        Set<String> tags = getTagsByArticleIdWithRestTemplate(foundArticle.getId());
        log.info("getArticleBySlug() : tags={}", tags);
        ProfileResponse profileResponse = getProfileByIdWithRestTemplate(foundArticle.getAuthorId(), token);
        log.info("getArticleBySlug() : profileResponse={}", profileResponse);
        Long[] favoriteInfo = getFavoriteInfoByArticleIdWithRestTemplate(foundArticle.getId(), token);
        log.info("getArticleBySlug() : favoriteInfo={}", favoriteInfo);

        return ArticleResponse.of(foundArticle, tags, profileResponse, favoriteInfo);
    }

    private Long[] getFavoriteInfoByArticleIdWithRestTemplate(final Long articleId, final String token) {
        log.info("getFavoriteInfoByArticleIdWithRestTemplate() : articleId={}", articleId);

        HttpHeaders headers = new HttpHeaders();
        if (StringUtils.hasText(token))
            headers.set("Authorization", jwtProperty.getTokenTitle() + token);

        ResponseEntity<Long[]> response = restTemplate.exchange(
                "http://localhost:8080/api/articles/" + articleId + "/favorite-info",
                HttpMethod.GET,
                new HttpEntity<String>(headers),
                Long[].class);

        return response.getBody();
    }

    /*public int deleteArticleBySlug(final String slug, final Long loginUserId) {
//        Article foundArticle = getArticleBySlug(slug);
//        if (! foundArticle.getAuthorId().equals(loginUserId))
//            throw new IllegalArgumentException("The author is different from the logged-in user.");
//
//        articleRepository.delete(foundArticle);

//        articleRepository.deleteBySlug(slug);
        return articleRepository.deleteBySlugAndAuthorId(slug, loginUserId);
    }*/
/*


    public Page<ArticleResponse> getArticlesByAuthor(final String author,
                                                     final Long loginUserId,
                                                     final PageRequest pageRequest) {
        Page<Object> objs = articleRepository.getArticlesByAuthor(author, loginUserId, pageRequest);
        log.info("getArticlesByTag() : objs={}", objs);

        return getArticleResponses(objs);
    }

    public Page<ArticleResponse> getFeedArticles(final Long loginUserId, final PageRequest pageRequest) {
        Page<Object> objs = articleRepository.getFeedArticles(loginUserId, pageRequest);
        log.info("getFeedArticles() : objs={}", objs);

        return getArticleResponses(objs);
    }

    public Page<ArticleResponse> getArticlesByFavorited(final String favorited, final Long loginUserId, final PageRequest pageRequest) {
        Page<Object> objs = articleRepository.getArticlesByFavorited(favorited, loginUserId, pageRequest);
        log.info("getArticlesByFavorited() : objs={}", objs);

        return getArticleResponses(objs);
    }
*/
}
