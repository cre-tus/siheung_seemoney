package com.siheung.seemoney.domain.activity.dto;

import com.siheung.seemoney.domain.activity.entity.Activity;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class ActivityResponseDto {
    private final Long id;
    private final String type;
    private final String title;
    private final int points;
    private final Long postId;
    private final String postPublicId;
    private final LocalDateTime createdAt;

    public ActivityResponseDto(Activity activity) {
        this.id = activity.getId();
        this.type = activity.getType();
        this.title = activity.getTitle();
        this.points = activity.getPoints();
        this.postId = activity.getPostId();
        this.postPublicId = activity.getPostPublicId();
        this.createdAt = activity.getCreatedAt();
    }
}
