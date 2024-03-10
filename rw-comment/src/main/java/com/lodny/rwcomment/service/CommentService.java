package com.lodny.rwcomment.service;

import com.lodny.rwcomment.entity.Comment;
import com.lodny.rwcomment.entity.dto.CommentResponse;
import com.lodny.rwcomment.entity.dto.ProfileResponse;
import com.lodny.rwcomment.entity.dto.RegisterCommentRequest;
import com.lodny.rwcomment.repository.CommentRepository;
import com.lodny.rwcommon.properties.JwtProperty;
import com.lodny.rwcommon.util.LoginInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final RestTemplate restTemplate;
    private final JwtProperty jwtProperty;

    private Long getArticleIdBySlugWithRestTemplate(final String slug) {  //}, final String token) {
        ResponseEntity<Long> response = restTemplate.exchange(
                "http://localhost:8080/api/articles/" + slug + "/id",
                HttpMethod.GET,
                new HttpEntity<>(new HttpHeaders()),
                Long.class);

        return response.getBody();
    }

    private ProfileResponse getProfileResponseByUserIdWithRestTemplate(final Long userId) {
        ResponseEntity<ProfileResponse> response = restTemplate.exchange(
                "http://localhost:8080/api/profiles/by-id/" + userId,
                HttpMethod.GET,
                new HttpEntity<String>(new HttpHeaders()),
                ProfileResponse.class);

        return response.getBody();
    }

    public CommentResponse registerComment(final String slug,
                                           final RegisterCommentRequest registerCommentRequest,
                                           final LoginInfo loginUser) {
        final Long articleId = getArticleIdBySlugWithRestTemplate(slug);
        log.info("registerComment() : articleId={}", articleId);

        Comment comment = Comment.of(registerCommentRequest, articleId, loginUser.getUserId());
        log.info("registerComment() : comment={}", comment);
        Comment savedComment = commentRepository.save(comment);
        log.info("registerComment() : savedComment={}", savedComment);

        ProfileResponse profileResponse = getProfileResponseByUserIdWithRestTemplate(loginUser.getUserId());
        log.info("registerComment() : profileResponse={}", profileResponse);

        return CommentResponse.of(savedComment, profileResponse);
    }

    public List<CommentResponse> getComments(final String slug, final LoginInfo loginUser) {
        final Long articleId = getArticleIdBySlugWithRestTemplate(slug);
        log.info("registerComment() : articleId={}", articleId);

        List<Comment> comments = commentRepository.findAllByArticleIdOrderByCreatedAtDesc(articleId);
        log.info("getComments() : comments={}", comments);

        return comments.stream()
                .map(comment -> {
                    ProfileResponse profileResponse = getProfileResponseByUserIdWithRestTemplate(loginUser.getUserId());
                    return CommentResponse.of(comment, profileResponse);
                })
                .toList();
    }



    /*
    public int deleteComment(final String slug, final Long commentId, final Long loginUserId) {
        log.info("deleteComment() : loginUserId={}", loginUserId);

//        Article article = articleRepository.findBySlug(slug);
//        if (article == null)
//            throw new IllegalArgumentException("article not found");
//        log.info("deleteComment() : article={}", article);
//
//        Comment foundComment = commentRepository.findById(commentId);
//        if (foundComment == null)
//            throw new IllegalArgumentException("comment not found");
//        log.info("deleteComment() : foundComment={}", foundComment);
//
//        if (! foundComment.getArticleId().equals(article.getId()))
//            throw new IllegalArgumentException("The comment article id does not match slug-based article id.");
//
//        if (! foundComment.getAuthorId().equals(loginUserId))
//            throw new IllegalArgumentException("Author Id of Slug-based article does not match the login user id.");
//
//        commentRepository.delete(foundComment);

        return commentRepository.deleteDirectly(slug, commentId, loginUserId);
    }



    private ArticleResponse getArticleResponseByObjs(final Object[] articleAndOther) {
        final int ARRAY_COUNT = 5;

        log.info("getArticleResponseByObjs() : articleAndOther.length={}", articleAndOther.length);
        log.info("getArticleResponseByObjs() : articleAndOther={}", articleAndOther);
        if (articleAndOther.length < ARRAY_COUNT || articleAndOther[0] == null)
            throw new IllegalArgumentException("The article is not found");

        return ArticleResponse.of(
                (Article) articleAndOther[0],
                ProfileResponse.of((RealWorldUser) articleAndOther[1], (Boolean)articleAndOther[3]),
                (Boolean) articleAndOther[2],
                (Long) articleAndOther[4]);
    }*/
}
