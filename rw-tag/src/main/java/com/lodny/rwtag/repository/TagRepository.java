package com.lodny.rwtag.repository;

import com.lodny.rwtag.entity.Tag;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Set;

public interface TagRepository extends Repository<Tag, Long> {
//    int saveAll(List<Tag> list);

    void save(Tag tag);

    Set<Tag> findAllByArticleId(Long articleId);

    @Query("SELECT t.tag, COUNT(t) AS tagCount FROM Tag t GROUP BY t.tag ORDER BY tagCount DESC")
    List<String[]> getTop10Tags();
}
