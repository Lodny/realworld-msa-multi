package com.lodny.rwcomment.controller;

import com.lodny.rwcomment.entity.dto.CommentResponse;
import com.lodny.rwcomment.entity.dto.RegisterCommentRequest;
import com.lodny.rwcomment.entity.wrapper.WrapCommentResponse;
import com.lodny.rwcomment.entity.wrapper.WrapCommentResponses;
import com.lodny.rwcomment.entity.wrapper.WrapRegisterCommentRequest;
import com.lodny.rwcomment.service.CommentService;
import com.lodny.rwcommon.annotation.JwtTokenRequired;
import com.lodny.rwcommon.annotation.LoginUser;
import com.lodny.rwcommon.util.LoginInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/articles/{slug}")
public class CommentController {
    private final CommentService commentService;

    @JwtTokenRequired
    @PostMapping("/comments")
    public ResponseEntity<?> registerComment(@PathVariable final String slug,
                                             @RequestBody final WrapRegisterCommentRequest wrapRegisterCommentRequest,
                                             @LoginUser final LoginInfo loginInfo) {
        RegisterCommentRequest registerCommentRequest = wrapRegisterCommentRequest.comment();
        log.info("registerComment() : slug={}", slug);
        log.info("registerComment() : registerCommentRequest={}", registerCommentRequest);
        log.info("registerComment() : loginInfo={}", loginInfo);

        CommentResponse commentResponse = commentService.registerComment(slug, registerCommentRequest, loginInfo);
        log.info("registerComment() : commentResponse={}", commentResponse);

        return ResponseEntity.status(HttpStatus.CREATED).body(new WrapCommentResponse(commentResponse));
    }

//    @GetMapping("/comments")
//    public ResponseEntity<?> getComments(@PathVariable final String slug, @LoginUser final UserResponse loginUser) {
//        log.info("getComments() : slug={}", slug);
//        log.info("getComments() : loginUser={}", loginUser);
//
//        List<CommentResponse> comments = commentService.getComments(slug, loginUser);
//        log.info("getComments() : comments={}", comments);
//
//        return ResponseEntity.ok(new WrapCommentResponses(comments));
//    }
//
//    @JwtTokenRequired
//    @DeleteMapping("/comments/{id}")
//    public ResponseEntity<?> deleteComment(@PathVariable final String slug,
//                              @PathVariable final Long id,
//                              @LoginUser final UserResponse loginUser) {
//        log.info("deleteComment() : slug={}", slug);
//        log.info("deleteComment() : comment id={}", id);
//        log.info("deleteComment() : loginUser={}", loginUser);
//
//        final Integer count = commentService.deleteComment(slug, id, loginUser.id());
//        log.info("deleteComment() : count={}", count);
//
//        return ResponseEntity.ok(count);
//    }
}
