package com.lodny.rwtag.repository;

import com.lodny.rwtag.entity.Tag;
import org.springframework.data.repository.Repository;

import java.util.Set;

public interface TagRepository extends Repository<Tag, Long> {
//    int saveAll(List<Tag> list);

    void save(Tag tag);

    Set<Tag> findAllByArticleId(Long articleId);
}
