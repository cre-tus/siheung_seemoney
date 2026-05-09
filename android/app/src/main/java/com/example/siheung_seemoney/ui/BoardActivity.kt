package com.example.siheung_seemoney.ui

import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.siheung_seemoney.R
import java.text.SimpleDateFormat
import java.util.*

class BoardActivity : AppCompatActivity() {

    data class Comment(
        val id: Long,
        val postId: Int,
        val author: String,
        val content: String,
        val date: String
    )

    data class Post(
        val id: Int,
        var title: String,
        var content: String,
        var author: String,
        var date: String,
        var likes: Int,
        val comments: MutableList<Comment>,
        var liked: Boolean
    )

    private val posts = mutableListOf(
        Post(1, "시청 앞 공원 재정비 제안", "시청 앞 공원이 노후화되어 리모델링이 필요합니다. 어린이 놀이터와 운동시설을 추가하면 좋겠습니다.", "김시흥", "2026.04.16", 245, mutableListOf(), false),
        Post(2, "청년 창업 지원 예산 확대 요청", "청년들의 창업을 돕기 위한 지원금과 교육 프로그램 예산을 늘려주세요.", "이청년", "2026.04.15", 189, mutableListOf(), false),
        Post(3, "문화센터 야간 운영 건의", "직장인들도 이용할 수 있도록 문화센터를 저녁 9시까지 운영해주시면 좋겠습니다.", "박시민", "2026.04.14", 132, mutableListOf(), false),
        Post(4, "학교 급식 품질 개선 예산 배정", "우리 아이들이 먹는 급식의 질을 높이기 위해 예산을 더 투입해주세요.", "최학부모", "2026.04.13", 301, mutableListOf(), false)
    )

    private var activeCommentBox: Int? = null

    private lateinit var writeForm: LinearLayout
    private lateinit var etTitle: EditText
    private lateinit var etContent: EditText
    private lateinit var topContainer: LinearLayout
    private lateinit var postContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board)

        writeForm = findViewById(R.id.writeForm)
        etTitle = findViewById(R.id.etTitle)
        etContent = findViewById(R.id.etContent)
        topContainer = findViewById(R.id.topContainer)
        postContainer = findViewById(R.id.postContainer)

        findViewById<Button>(R.id.btnWrite).setOnClickListener {
            writeForm.visibility =
                if (writeForm.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }

        findViewById<Button>(R.id.btnSubmit).setOnClickListener {
            handleSubmit()
        }

        findViewById<Button>(R.id.btnCancel).setOnClickListener {
            writeForm.visibility = View.GONE
        }

        renderAll()
    }

    private fun handleSubmit() {
        val title = etTitle.text.toString().trim()
        val content = etContent.text.toString().trim()

        if (title.isEmpty() || content.isEmpty()) return

        val date = SimpleDateFormat("yyyy.MM.dd", Locale.KOREA).format(Date())

        posts.add(
            0,
            Post(
                id = posts.size + 1,
                title = title,
                content = content,
                author = "익명",
                date = date,
                likes = 0,
                comments = mutableListOf(),
                liked = false
            )
        )

        etTitle.text.clear()
        etContent.text.clear()
        writeForm.visibility = View.GONE

        renderAll()
    }

    private fun toggleLike(post: Post) {
        post.liked = !post.liked
        post.likes = if (post.liked) post.likes + 1 else post.likes - 1

        if (post.liked) {
            Toast.makeText(this, "${post.title} 공감 +50P", Toast.LENGTH_SHORT).show()
        }

        renderAll()
    }

    private fun addComment(post: Post, input: EditText) {
        val text = input.text.toString().trim()
        if (text.isEmpty()) return

        val date = SimpleDateFormat("yyyy.MM.dd", Locale.KOREA).format(Date())

        post.comments.add(
            Comment(
                id = System.currentTimeMillis(),
                postId = post.id,
                author = "익명",
                content = text,
                date = date
            )
        )

        Toast.makeText(this, "${post.title} 댓글 작성 +30P", Toast.LENGTH_SHORT).show()

        activeCommentBox = null
        renderAll()
    }

    private fun renderAll() {
        renderTopPosts()
        renderPosts()
    }

    private fun renderTopPosts() {
        topContainer.removeAllViews()

        posts.sortedByDescending { it.likes }
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
                like.text = "♥ ${post.likes}"
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
            info.text = "${post.author}   🕒 ${post.date}"
            info.textSize = 12f
            info.setTextColor(0xFF6B7280.toInt())

            val actionRow = LinearLayout(this)
            actionRow.orientation = LinearLayout.HORIZONTAL
            actionRow.setPadding(0, 14, 0, 0)

            val likeButton = Button(this)
            likeButton.text = if (post.liked) "♥ ${post.likes}" else "♡ ${post.likes}"
            likeButton.setOnClickListener {
                toggleLike(post)
            }

            val commentButton = Button(this)
            commentButton.text = "💬 ${post.comments.size}"
            commentButton.setOnClickListener {
                activeCommentBox = if (activeCommentBox == post.id) null else post.id
                renderPosts()
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

        post.comments.forEach { comment ->
            val commentBox = LinearLayout(this)
            commentBox.orientation = LinearLayout.VERTICAL
            commentBox.setPadding(14, 12, 14, 12)
            commentBox.setBackgroundColor(0xFFF9FAFB.toInt())

            val meta = TextView(this)
            meta.text = "${comment.author}   ${comment.date}"
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
}