package com.lodny.rwarticle.repository;

import com.lodny.rwarticle.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends Repository<Article, Long> {
    Article save(final Article article);
    Optional<Article> findBySlug(final String slug);
    Page<Article> findAllByOrderByCreatedAtDesc(PageRequest pageRequest);
    Page<Article> findByIdIn(List<Long> articleIds, PageRequest pageRequest);
}
