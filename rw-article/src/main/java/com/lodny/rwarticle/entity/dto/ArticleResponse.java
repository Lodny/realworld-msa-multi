package com.lodny.rwarticle.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lodny.rwarticle.entity.Article;
import com.lodny.rwcommon.util.MapToDto.MapToDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleResponse {
    @JsonIgnore
    private Long id;

    private String slug;
    private String title;
    private String description;
    private String body;
    private Set<String> tagList;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean favorited;
    private Long favoritesCount;

    private ProfileResponse author;

    public static ArticleResponse of(final Article article) {
        return ArticleResponse.builder()
                .id(article.getId())
                .slug(article.getSlug())
                .title(article.getTitle())
                .description(article.getDescription())
                .body(article.getBody())
                .build();
    }

    public static ArticleResponse of(final Article article,
                                     final ProfileResponse author,
                                     final Boolean favorited,
                                     final Long favoritesCount) {
        // todo : BeanUtils.copyProperites
        return new ArticleResponse(
                article.getId(),
                article.getSlug(),
                article.getTitle(),
                article.getDescription(),
                article.getBody(),
//                article.getTagList(),
                null,
                article.getCreatedAt(),
                article.getUpdatedAt(),
                favorited,
                favoritesCount,
                author);
    }

    public static ArticleResponse of(final Object[] objects) {
        final int ARRAY_COUNT = 5;

        if (objects == null)
            throw new IllegalArgumentException("The article is not found");

        log.info("ArticleResponse of() : objects={}", objects);
        if (objects.length < ARRAY_COUNT || objects[0] == null)
            throw new IllegalArgumentException("The article is not found");

        return ArticleResponse.of((Article) objects[0],
//                ProfileResponse.of((RealWorldUser) objects[1], (Boolean)objects[3]),
                null,
                (Boolean) objects[2],
                (Long) objects[4]);
    }

    public static ArticleResponse of(final Map<String, Object> map) {
        return MapToDto.convert(map, ArticleResponse.class);
    }

    public void tagListToDto(Object value) {
        this.tagList = Set.of(((String)value).split(","));
//        log.info("tagListToDto() : this.tagList={}", this.tagList);
    }

}
