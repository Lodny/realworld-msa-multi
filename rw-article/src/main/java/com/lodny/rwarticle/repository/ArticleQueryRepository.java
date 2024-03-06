package com.lodny.rwarticle.repository;

import com.lodny.rwarticle.entity.dto.ArticleResponse;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ArticleQueryRepository {

    private final EntityManager em;

    public ArticleResponse selectArticleBySlug(String slug) {
        return em
                .createQuery("""
                    select  new com.lodny.rwarticle.entity.dto.ArticleResponse(
                                a.slug,
                                a.title,
                                a.description,
                                a.body,
                                null,
                                a.createdAt,
                                a.updatedAt,
                                false,
                                0L,
                                new com.lodny.realworldjuiceembeddable.entity.dto.ProfileResponse(
                                    u.username,
                                    u.bio,
                                    u.image,
                                    false
                               )
                            )
                    from    Article a
                    join    RealWorldUser u on u.id = a.authorId
                    where   a.slug = :slug
                """, ArticleResponse.class)
                .setParameter("slug", slug)
                .getSingleResult();
    }
}
