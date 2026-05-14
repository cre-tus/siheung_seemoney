package com.example.siheung_seemoney.ui

import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.siheung_seemoney.R
import com.example.siheung_seemoney.data.model.Post
import com.example.siheung_seemoney.data.model.Comment
import com.example.siheung_seemoney.data.repository.PostRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class BoardActivity : AppCompatActivity() {

    // =========================
    // PostRepository
    // Retrofit/API 통신 담당 객체
    // =========================
    private val postRepository by lazy {
        PostRepository(this)
    }

    // =========================
    // 서버에서 받아온 게시글 저장 리스트
    // =========================
    private val posts = mutableListOf<Post>()

    // =========================
    // 게시글별 댓글 캐시
    // key = postId
    // value = 댓글 리스트
    // =========================
    private val postComments =
        mutableMapOf<Int, MutableList<Comment>>()

    // =========================
    // 현재 좋아요 누른 게시글 저장
    // =========================
    private val likedPosts =
        mutableSetOf<Int>()

    // =========================
    // 현재 열려있는 댓글창 게시글 ID
    // =========================
    private var activeCommentBox: Int? = null

    private lateinit var writeForm: LinearLayout
    private lateinit var etTitle: EditText
    private lateinit var etContent: EditText
    private lateinit var topContainer: LinearLayout
    private lateinit var postContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_board)

        // =========================
        // XML View 연결
        // =========================
        writeForm = findViewById(R.id.writeForm)
        etTitle = findViewById(R.id.etTitle)
        etContent = findViewById(R.id.etContent)
        topContainer = findViewById(R.id.topContainer)
        postContainer = findViewById(R.id.postContainer)

        // =========================
        // 글쓰기 버튼
        // 작성 폼 열기/닫기
        // =========================
        findViewById<Button>(R.id.btnWrite)
            .setOnClickListener {

                writeForm.visibility =
                    if (writeForm.visibility == View.VISIBLE)
                        View.GONE
                    else
                        View.VISIBLE
            }

        // =========================
        // 게시글 등록 버튼
        // =========================
        findViewById<Button>(R.id.btnSubmit)
            .setOnClickListener {

                handleSubmit()
            }

        // =========================
        // 글쓰기 취소 버튼
        // =========================
        findViewById<Button>(R.id.btnCancel)
            .setOnClickListener {

                writeForm.visibility = View.GONE
            }

        // =========================
        // 앱 시작 시
        // 서버에서 게시글 조회
        // =========================
        loadPosts()
    }

    private fun loadPosts() {

        // =========================
        // 코루틴 실행
        // 네트워크 작업용
        // =========================
        lifecycleScope.launch {

            // =========================
            // 서버 API 호출
            // 게시글 목록 요청
            // =========================
            val result =
                postRepository.getPosts()

            // =========================
            // 성공 시
            // =========================
            result.onSuccess { postList ->

                // 기존 게시글 제거
                posts.clear()

                // 서버 게시글 추가
                posts.addAll(postList)

                // 화면 다시 그림
                renderAll()
            }

            // =========================
            // 실패 시
            // =========================
            result.onFailure {

                Toast.makeText(
                    this@BoardActivity,
                    "게시글 불러오기 실패",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun handleSubmit() {

        // =========================
        // 입력값 가져오기
        // =========================
        val title =
            etTitle.text.toString().trim()

        val content =
            etContent.text.toString().trim()

        // =========================
        // 제목/내용 비어있으면 종료
        // =========================
        if (title.isEmpty() || content.isEmpty())
            return

        lifecycleScope.launch {

            // =========================
            // 서버에 게시글 생성 요청
            // =========================
            val result =
                postRepository.createPost(
                    title,
                    content
                )

            // =========================
            // 작성 성공
            // =========================
            result.onSuccess {

                Toast.makeText(
                    this@BoardActivity,
                    "+100P 적립!",
                    Toast.LENGTH_SHORT
                ).show()

                // 입력창 초기화
                etTitle.text.clear()
                etContent.text.clear()

                // 작성 폼 닫기
                writeForm.visibility = View.GONE

                // 게시글 새로고침
                loadPosts()
            }

            // =========================
            // 작성 실패
            // =========================
            result.onFailure { error ->

                Toast.makeText(
                    this@BoardActivity,
                    "게시글 작성 실패",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun toggleLike(post: Post) {

        lifecycleScope.launch {

            // =========================
            // 좋아요 API 요청
            // =========================
            val result =
                postRepository.toggleLike(post.id)

            result.onSuccess { response ->

                Toast.makeText(
                    this@BoardActivity,
                    response.message,
                    Toast.LENGTH_SHORT
                ).show()

                // =========================
                // 좋아요 상태 저장
                // =========================
                if (response.liked) {

                    likedPosts.add(post.id)

                } else {

                    likedPosts.remove(post.id)
                }

                // =========================
                // 게시글 다시 조회
                // =========================
                loadPosts()
            }

            result.onFailure {

                Toast.makeText(
                    this@BoardActivity,
                    "좋아요 실패",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun loadComments(postId: Int) {

        lifecycleScope.launch {

            // =========================
            // 댓글 목록 요청
            // =========================
            val result =
                postRepository.getComments(postId)

            result.onSuccess { comments ->

                // =========================
                // 댓글 캐시에 저장
                // =========================
                postComments[postId] =
                    comments.toMutableList()

                // 댓글 UI 새로고침
                renderPosts()
            }

            result.onFailure {

                Toast.makeText(
                    this@BoardActivity,
                    "댓글 불러오기 실패",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun addComment(
        post: Post,
        input: EditText
    ) {

        // =========================
        // 댓글 입력값
        // =========================
        val text =
            input.text.toString().trim()

        if (text.isEmpty())
            return

        lifecycleScope.launch {

            // =========================
            // 댓글 작성 API 요청
            // =========================
            val result =
                postRepository.createComment(
                    post.id,
                    text
                )

            result.onSuccess {

                Toast.makeText(
                    this@BoardActivity,
                    "+30P 적립!",
                    Toast.LENGTH_SHORT
                ).show()

                // 입력창 초기화
                input.text.clear()

                // 댓글 새로고침
                loadComments(post.id)

                // 게시글 새로고침
                // commentCount 반영
                loadPosts()
            }

            result.onFailure {

                Toast.makeText(
                    this@BoardActivity,
                    "댓글 작성 실패",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    private fun renderAll() {
        renderTopPosts()
        renderPosts()
    }

    private fun renderTopPosts() {
        topContainer.removeAllViews()

        posts.sortedByDescending { it.likeCount }
            .take(3)
            .forEachIndexed { index, post ->
                val row = LinearLayout(this)
                row.orientation = LinearLayout.HORIZONTAL
                row.setPadding(14, 14, 14, 14)
                row.setBackgroundColor(0xFFFFFFFF.toInt())

                val rank = TextView(this)
                rank.text = "${index + 1}"
                rank.textSize = 15f
                rank.setTextColor(0xFFFFFFFF.toInt())
                rank.gravity = android.view.Gravity.CENTER
                rank.setTypeface(null, Typeface.BOLD)
                rank.setBackgroundColor(0xFFF97316.toInt())
                rank.layoutParams = LinearLayout.LayoutParams(32, 32)

                val title = TextView(this)
                title.text = post.title
                title.textSize = 14f
                title.setTextColor(0xFF111827.toInt())
                title.setSingleLine(true)
                title.layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                title.setPadding(14, 0, 8, 0)

                val like = TextView(this)
                like.text = "♥ ${post.likeCount}"
                like.textSize = 13f
                like.setTextColor(0xFFEA580C.toInt())
                like.setTypeface(null, Typeface.BOLD)

                row.addView(rank)
                row.addView(title)
                row.addView(like)

                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(0, 0, 0, 8)

                topContainer.addView(row, params)
            }
    }

    private fun renderPosts() {
        postContainer.removeAllViews()

        posts.forEach { post ->
            val card = LinearLayout(this)
            card.orientation = LinearLayout.VERTICAL
            card.setPadding(22, 22, 22, 22)
            card.setBackgroundColor(0xFFFFFFFF.toInt())

            val title = TextView(this)
            title.text = post.title
            title.textSize = 18f
            title.setTextColor(0xFF111827.toInt())
            title.setTypeface(null, Typeface.BOLD)

            val content = TextView(this)
            content.text = post.content
            content.textSize = 14f
            content.setTextColor(0xFF4B5563.toInt())
            content.setPadding(0, 10, 0, 10)

            val info = TextView(this)
            // createdAt을 date 형식으로 변환
            val dateStr = formatDate(post.createdAt)
            info.text = "${post.nickname}   🕒 $dateStr"
            info.textSize = 12f
            info.setTextColor(0xFF6B7280.toInt())

            val actionRow = LinearLayout(this)
            actionRow.orientation = LinearLayout.HORIZONTAL
            actionRow.setPadding(0, 14, 0, 0)

            val likeButton = Button(this)
            val isLiked = likedPosts.contains(post.id)
            likeButton.text = if (isLiked) "♥ ${post.likeCount}" else "♡ ${post.likeCount}"
            likeButton.setOnClickListener {
                toggleLike(post)
            }

            val commentButton = Button(this)
            commentButton.text = "💬 ${post.commentCount}"
            commentButton.setOnClickListener {
                activeCommentBox = if (activeCommentBox == post.id) null else post.id
                if (activeCommentBox == post.id) {
                    // 댓글창 열릴 때 댓글 불러오기
                    loadComments(post.id)
                } else {
                    renderPosts()
                }
            }

            actionRow.addView(likeButton)
            actionRow.addView(commentButton)

            card.addView(title)
            card.addView(content)
            card.addView(info)
            card.addView(actionRow)

            if (activeCommentBox == post.id) {
                addCommentSection(card, post)
            }

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(0, 0, 0, 14)

            postContainer.addView(card, params)
        }
    }

    private fun addCommentSection(card: LinearLayout, post: Post) {
        val section = LinearLayout(this)
        section.orientation = LinearLayout.VERTICAL
        section.setPadding(0, 16, 0, 0)

        // 캐시된 댓글 목록 사용
        val comments = postComments[post.id] ?: emptyList()

        comments.forEach { comment ->
            val commentBox = LinearLayout(this)
            commentBox.orientation = LinearLayout.VERTICAL
            commentBox.setPadding(14, 12, 14, 12)
            commentBox.setBackgroundColor(0xFFF9FAFB.toInt())

            val meta = TextView(this)
            val commentDateStr = formatDate(comment.createdAt)
            meta.text = "${comment.nickname}   $commentDateStr"
            meta.textSize = 12f
            meta.setTextColor(0xFF374151.toInt())
            meta.setTypeface(null, Typeface.BOLD)

            val body = TextView(this)
            body.text = comment.content
            body.textSize = 13f
            body.setTextColor(0xFF4B5563.toInt())
            body.setPadding(0, 4, 0, 0)

            commentBox.addView(meta)
            commentBox.addView(body)

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(0, 0, 0, 8)

            section.addView(commentBox, params)
        }

        val inputRow = LinearLayout(this)
        inputRow.orientation = LinearLayout.HORIZONTAL

        val input = EditText(this)
        input.hint = "댓글을 입력하세요..."
        input.textSize = 14f
        input.layoutParams = LinearLayout.LayoutParams(0, 48, 1f)

        val sendButton = Button(this)
        sendButton.text = "전송"
        sendButton.setOnClickListener {
            addComment(post, input)
        }

        val closeButton = Button(this)
        closeButton.text = "X"
        closeButton.setOnClickListener {
            activeCommentBox = null
            renderPosts()
        }

        inputRow.addView(input)
        inputRow.addView(sendButton)
        inputRow.addView(closeButton)

        section.addView(inputRow)
        card.addView(section)
    }

    /**
     * ISO 8601 날짜를 "yyyy.MM.dd" 형식으로 변환
     */
    private fun formatDate(isoDate: String): String {
        return try {
            // "2026-05-13T10:00:00" → "2026.05.13"
            isoDate.split("T")[0].replace("-", ".")
        } catch (e: Exception) {
            isoDate
        }
    }
}
