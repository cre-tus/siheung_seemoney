package com.example.siheung_seemoney.data.network

import com.google.gson.annotations.SerializedName
import com.example.siheung_seemoney.data.model.Post
import com.example.siheung_seemoney.data.model.Comment

// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
// 게시글 요청 (Request) 모델
// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

/**
 * 게시글 작성 요청
 */
data class CreatePostRequest(
    @SerializedName("title")
    val title: String,

    @SerializedName("content")
    val content: String
)

/**
 * 댓글 작성 요청
 */
data class CreateCommentRequest(
    @SerializedName("postId")
    val postId: Int,

    @SerializedName("content")
    val content: String
)

// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
// 게시글 응답 (Response) 모델
// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

/**
 * 좋아요 토글 응답
 */
data class LikeResponse(
    @SerializedName("liked")
    val liked: Boolean,

    @SerializedName("message")
    val message: String
)
