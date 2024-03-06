package com.lodny.rwcomment.repository;

import com.lodny.rwcomment.entity.Comment;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends Repository<Comment, Long> {
    Comment save(Comment comment);
    Optional<Comment> findById(Long commentId);

    // todo::delete one query
    void delete(Comment foundComment);
//
//    @Transactional
//    @Modifying
//    @Query("""
//        DELETE  FROM    Comment c
//        WHERE   c.id = :commentId
//        AND     c.articleId = (SELECT a.id FROM Article a WHERE a.slug = :slug)
//        AND     c.authorId = (SELECT u.id FROM RealWorldUser u WHERE u.id = :loginUserId)
//    """)
//    int deleteDirectly(final String slug, final Long commentId, final Long loginUserId);
//
//    @Query("""
//        SELECT  c
//              , u
//              , CASE WHEN f IS NULL THEN false ELSE true END
//        FROM    Comment c
//        JOIN    Article a ON a.id = c.articleId
//        JOIN    RealWorldUser u ON u.id = c.authorId
//        LEFT JOIN Follow f ON f.id.followeeId = c.authorId AND f.id.followerId = :loginUserId
//        WHERE   a.slug = :slug
//        ORDER BY c.createdAt DESC
//    """)
//    List<Object> findByArticleIdIncludeUser(String slug, Long loginUserId);
}
