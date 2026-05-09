package com.example.siheung_seemoney.data.model

import com.google.gson.annotations.SerializedName

/**
 * 활동 타입
 */
enum class ActivityType {
    @SerializedName("vote")
    VOTE,

    @SerializedName("post")
    POST,

    @SerializedName("like")
    LIKE,

    @SerializedName("comment")
    COMMENT
}

/**
 * 사용자 활동 내역 (DB activities 테이블)
 */
data class Activity(
    @SerializedName("id")
    val id: String,

    @SerializedName("user_id")
    val userId: Int,

    @SerializedName("type")
    val type: ActivityType,

    @SerializedName("title")
    val title: String,

    @SerializedName("date")
    val date: String,

    @SerializedName("points")
    val points: Int,

    @SerializedName("post_id")
    val postId: Int? = null,

    @SerializedName("created_at")
    val createdAt: String
)

/**
 * 활동 타입별 한글 이름
 */
fun ActivityType.toKorean(): String = when (this) {
    ActivityType.VOTE -> "투표"
    ActivityType.POST -> "제안 작성"
    ActivityType.LIKE -> "공감"
    ActivityType.COMMENT -> "댓글"
}

/**
 * 활동 타입별 색상
 */
fun ActivityType.toColorRes(): Int = when (this) {
    ActivityType.VOTE -> android.R.color.holo_blue_light
    ActivityType.POST -> android.R.color.holo_purple
    ActivityType.LIKE -> android.R.color.holo_red_light
    ActivityType.COMMENT -> android.R.color.holo_green_light
}
