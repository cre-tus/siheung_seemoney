package com.example.siheung_seemoney.data.model

import com.google.gson.annotations.SerializedName

/**
 * 게시글 (DB posts 테이블)
 */
data class Post(
    @SerializedName("id")
    val id: Int,

    @SerializedName("publicId")
    val publicId: String,

    @SerializedName("nickname")
    val nickname: String,

    @SerializedName("title")
    val title: String,

    @SerializedName("content")
    val content: String,

    @SerializedName("likeCount")
    val likeCount: Int = 0,

    @SerializedName("commentCount")
    val commentCount: Int = 0,

    @SerializedName("viewCount")
    val viewCount: Int = 0,

    @SerializedName("createdAt")
    val createdAt: String
)

/**
 * 댓글 (DB comments 테이블)
 */
data class Comment(
    @SerializedName("id")
    val id: Int,

    @SerializedName("nickname")
    val nickname: String,

    @SerializedName("content")
    val content: String,

    @SerializedName("createdAt")
    val createdAt: String
)
