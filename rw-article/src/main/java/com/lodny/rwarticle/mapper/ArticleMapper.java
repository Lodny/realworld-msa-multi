package com.lodny.rwarticle.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface ArticleMapper {
//    ArticleResponse2 selectArticleBySlug(final String slug);
    Map<String, Object> selectArticleBySlug(@Param("slug") final String slug, @Param("loginUserId") final Long loginUserId);
}
