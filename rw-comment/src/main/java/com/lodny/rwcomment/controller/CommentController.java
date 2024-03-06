package com.lodny.rwcomment.controller;

import com.lodny.rwcomment.entity.dto.CommentResponse;
import com.lodny.rwcomment.entity.dto.RegisterCommentRequest;
import com.lodny.rwcomment.entity.wrapper.WrapCommentResponse;
import com.lodny.rwcomment.entity.wrapper.WrapCommentResponses;
import com.lodny.rwcomment.entity.wrapper.WrapRegisterCommentRequest;
import com.lodny.rwcomment.service.CommentService;
import com.lodny.rwcommon.annotation.JwtTokenRequired;
import com.lodny.rwcommon.annotation.LoginUser;
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
                                             @LoginUser final Map<String, String> loginInfo) {
        RegisterCommentRequest registerCommentRequest = wrapRegisterCommentRequest.comment();
        log.info("[C] registerComment() : slug={}", slug);
        log.info("[C] registerComment() : registerCommentRequest={}", registerCommentRequest);
        log.info("[C] registerComment() : loginInfo={}", loginInfo);

//        CommentResponse commentResponse = commentService.registerComment(slug, registerCommentRequest, loginUser);
//        log.info("[C] registerComment() : commentResponse={}", commentResponse);

        return ResponseEntity.status(HttpStatus.CREATED).body("new WrapCommentResponse(commentResponse)");
    }

//    @GetMapping("/comments")
//    public ResponseEntity<?> getComments(@PathVariable final String slug, @LoginUser final UserResponse loginUser) {
//        log.info("[C] getComments() : slug={}", slug);
//        log.info("[C] getComments() : loginUser={}", loginUser);
//
//        List<CommentResponse> comments = commentService.getComments(slug, loginUser);
//        log.info("[C] getComments() : comments={}", comments);
//
//        return ResponseEntity.ok(new WrapCommentResponses(comments));
//    }
//
//    @JwtTokenRequired
//    @DeleteMapping("/comments/{id}")
//    public ResponseEntity<?> deleteComment(@PathVariable final String slug,
//                              @PathVariable final Long id,
//                              @LoginUser final UserResponse loginUser) {
//        log.info("[C] deleteComment() : slug={}", slug);
//        log.info("[C] deleteComment() : comment id={}", id);
//        log.info("[C] deleteComment() : loginUser={}", loginUser);
//
//        final Integer count = commentService.deleteComment(slug, id, loginUser.id());
//        log.info("[C] deleteComment() : count={}", count);
//
//        return ResponseEntity.ok(count);
//    }
}
