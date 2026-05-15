package com.example.siheung_seemoney.data.repository
import android.content.Context
import com.example.siheung_seemoney.data.network.*
import com.example.siheung_seemoney.data.model.Post
import com.example.siheung_seemoney.data.model.Comment

/**
 * 게시글 관련 Repository
 */
class PostRepository(context: Context) {

    private val authRepository = AuthRepository(context)
    private val apiService = RetrofitClient.apiService

    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    // 게시글 API
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

    /**
     * 전체 게시글 조회
     */
    suspend fun getPosts(): Result<List<Post>> {
        return try {
            val response = apiService.getPosts()

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("게시글 조회 실패"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * 게시글 작성
     */
    suspend fun createPost(title: String, content: String): Result<Post> {
        val token = authRepository.getBearerToken()
            ?: return Result.failure(Exception("로그인이 필요합니다"))

        return try {
            val request = CreatePostRequest(title, content)
            val response = apiService.createPost(token, request)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("게시글 작성 실패"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * 게시글 좋아요 토글
     */
    suspend fun toggleLike(postId: Int): Result<LikeResponse> {
        val token = authRepository.getBearerToken()
            ?: return Result.failure(Exception("로그인이 필요합니다"))

        return try {
            val response = apiService.toggleLike(token, postId)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("좋아요 실패"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    // 댓글 API
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

    /**
     * 특정 게시글의 댓글 조회
     */
    suspend fun getComments(postId: Int): Result<List<Comment>> {
        return try {
            val response = apiService.getComments(postId)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("댓글 조회 실패"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * 댓글 작성
     */
    suspend fun createComment(postId: Int, content: String): Result<Comment> {
        val token = authRepository.getBearerToken()
            ?: return Result.failure(Exception("로그인이 필요합니다"))

        return try {
            val request = CreateCommentRequest(postId, content)
            val response = apiService.createComment(token, request)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("댓글 작성 실패"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
