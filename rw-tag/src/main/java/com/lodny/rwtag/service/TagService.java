package com.lodny.rwtag.service;

import com.lodny.rwtag.entity.Tag;
import com.lodny.rwtag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    public int registerTags(final Long articleId, final Set<String> tags) {
//        tags.forEach(tag -> tagRepository.save(new Tag(articleId, tag)));

        int count = 1;
        tagRepository.saveAll(tags.stream()
                .map(tag -> new Tag(articleId, tag))
                .toList());
        log.info("[S] registerTags() : count={}", count);

        return count;
    }

    public List<String> getTop10Tags() {
        return tagRepository.getTop10Tags().stream().map(tag -> tag[0]).toList();
    }

    public Set<Tag> getTags(final Long articleId) {
        return tagRepository.findAllByArticleId(articleId);
    }
}
