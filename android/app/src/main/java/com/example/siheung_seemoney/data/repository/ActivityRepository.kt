package com.example.siheung_seemoney.data.repository

import android.content.Context
import com.example.siheung_seemoney.data.network.*
import com.example.siheung_seemoney.data.model.Activity
import com.example.siheung_seemoney.data.model.ActivityType

/**
 * 활동 내역 관련 Repository
 */
class ActivityRepository(context: Context) {

    private val authRepository = AuthRepository(context)
    private val apiService = RetrofitClient.apiService

    /**
     * 내 활동 내역 조회
     */
    suspend fun getActivities(): Result<List<Activity>> {
        val token = authRepository.getBearerToken()
            ?: return Result.failure(Exception("로그인이 필요합니다"))

        return try {
            val response = apiService.getActivities(token)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.activities)
            } else {
                Result.failure(Exception("활동 내역 조회 실패"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * 활동 추가
     */
    suspend fun addActivity(
        type: ActivityType,
        title: String,
        points: Int,
        postId: Int? = null
    ): Result<String> {
        val token = authRepository.getBearerToken()
            ?: return Result.failure(Exception("로그인이 필요합니다"))

        return try {
            val request = AddActivityRequest(type, title, points, postId)
            val response = apiService.addActivity(token, request)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.id)
            } else {
                Result.failure(Exception("활동 추가 실패"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * 좋아요 추가
     */
    suspend fun addLike(postTitle: String, postId: Int): Result<String> {
        return addActivity(
            type = ActivityType.LIKE,
            title = "$postTitle 공감",
            points = 50,
            postId = postId
        )
    }

    /**
     * 댓글 추가
     */
    suspend fun addComment(postTitle: String, postId: Int): Result<String> {
        return addActivity(
            type = ActivityType.COMMENT,
            title = "$postTitle 댓글 작성",
            points = 30,
            postId = postId
        )
    }

    /**
     * 투표 참여
     */
    suspend fun addVote(voteTitle: String): Result<String> {
        return addActivity(
            type = ActivityType.VOTE,
            title = voteTitle,
            points = 100
        )
    }

    /**
     * 제안 작성
     */
    suspend fun addPost(postTitle: String): Result<String> {
        return addActivity(
            type = ActivityType.POST,
            title = "$postTitle 작성",
            points = 100
        )
    }
}
