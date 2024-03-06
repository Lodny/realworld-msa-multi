package com.lodny.rwtag.controller;

import com.lodny.rwcommon.annotation.JwtTokenRequired;
import com.lodny.rwtag.entity.wrapper.WrapTag10Response;
import com.lodny.rwtag.service.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TagController {

    private final TagService tagService;

    @JwtTokenRequired
    @PostMapping("/articles/{articleId}/tags/list")
    public ResponseEntity<?> registerTags(@PathVariable final Long articleId,
                                          @RequestBody final Set<String> tags) {
        log.info("[C] registerTags() : articleId={}", articleId);
        log.info("[C] registerTags() : tags={}", tags);
        int count = tagService.registerTags(articleId, tags);

        return ResponseEntity.status(HttpStatus.CREATED).body(count);
    }

    @GetMapping("/tags")
    public ResponseEntity<?> getTop10Tags() {
        log.info("getTop10Tags() : 1={}", 1);

        List<String> top10Tags = tagService.getTop10Tags();
        log.info("getTop10Tags() : top10Tags={}", top10Tags);

        return ResponseEntity.ok(new WrapTag10Response(top10Tags));
    }
}
