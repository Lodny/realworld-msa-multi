package com.lodny.rwfavorite.service;

import com.lodny.rwfavorite.entity.Favorite;
import com.lodny.rwfavorite.entity.FavoriteId;
import com.lodny.rwfavorite.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;

    public Favorite favorite(final Long articleId, final Long loginUserId) {
        return favoriteRepository.save(Favorite.of(articleId, loginUserId));
    }

    public void unfavorite(final Long articleId, final Long loginUserId) {
        favoriteRepository.deleteById(new FavoriteId(articleId, loginUserId));
    }

    public Long[] favoriteInfo(final Long articleId, final long loginUserId) {
        Long favoritesCount = favoriteRepository.countByIdArticleId(articleId);
        log.info("favoriteInfo() : favoritesCount={}", favoritesCount);
        Favorite favorite = favoriteRepository.findById(new FavoriteId(articleId, loginUserId));
        log.info("addFavorite() : favorite={}", favorite);

        return new Long[]{favoritesCount, favorite == null ? 0L : 1L};
    }

//    private final ArticleRepository articleRepository;
//
//    public ArticleResponse favorite(final String slug, final Long loginUserId) {
//        log.info("favorite() : loginUserId={}", loginUserId);
//        Article article = articleRepository.findBySlug(slug)
//                .orElseThrow(() -> new IllegalArgumentException("article not found"));
//
//        Favorite favorite = favoriteRepository.save(Favorite.of(article.getId(), loginUserId));
//        log.info("addFavorite() : favorite={}", favorite);
//
//        Object[] objets = (Object[])articleRepository.findBySlugIncludeUser(slug, loginUserId);
//        return ArticleResponse.of(objets);
//    }
//
//    public ArticleResponse unfavorite(final String slug, final Long loginUserId) {
//        log.info("unfavorite() : loginUserId={}", loginUserId);
//        Article article = articleRepository.findBySlug(slug)
//                .orElseThrow(() -> new IllegalArgumentException("article not found"));
//        log.info("unfavorite() : article={}", article);
//
//        favoriteRepository.deleteById(new FavoriteId(article.getId(), loginUserId));
//
//        Object[] objets = (Object[])articleRepository.findBySlugIncludeUser(slug, loginUserId);
//        return ArticleResponse.of(objets);
//    }
}
