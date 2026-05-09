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
        var liked: Boolean = false,
        val comments: MutableList<Comment> = mutableListOf()
    )

    private val posts = mutableListOf(
        Post(
            1,
            "시청 앞 공원 재정비 제안",
            "시청 앞 공원이 노후화되어 리모델링이 필요합니다. 어린이 놀이터와 운동시설을 추가하면 좋겠습니다.",
            "김시흥",
            "2026.04.16",
            245
        ),
        Post(
            2,
            "청년 창업 지원 예산 확대 요청",
            "청년들의 창업을 돕기 위한 지원금과 교육 프로그램 예산을 늘려주세요.",
            "이청년",
            "2026.04.15",
            189
        ),
        Post(
            3,
            "문화센터 야간 운영 건의",
            "직장인들도 이용할 수 있도록 문화센터를 저녁 9시까지 운영해주시면 좋겠습니다.",
            "박시민",
            "2026.04.14",
            132
        ),
        Post(
            4,
            "학교 급식 품질 개선 예산 배정",
            "우리 아이들이 먹는 급식의 질을 높이기 위해 예산을 더 투입해주세요.",
            "최학부모",
            "2026.04.13",
            301
        )
    )

    private lateinit var writeForm: LinearLayout
    private lateinit var etTitle: EditText
    private lateinit var etContent: EditText
    private lateinit var topContainer: LinearLayout
    private lateinit var postContainer: LinearLayout

    private var activeCommentPostId: Int? = null
    private var commentText = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board)

        writeForm = findViewById(R.id.writeForm)
        etTitle = findViewById(R.id.etTitle)
        etContent = findViewById(R.id.etContent)
        topContainer = findViewById(R.id.topContainer)
        postContainer = findViewById(R.id.postContainer)

        val btnWrite = findViewById<Button>(R.id.btnWrite)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)
        val btnCancel = findViewById<Button>(R.id.btnCancel)

        btnWrite.setOnClickListener {
            writeForm.visibility =
                if (writeForm.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }

        btnCancel.setOnClickListener {
            writeForm.visibility = View.GONE
            etTitle.text.clear()
            etContent.text.clear()
        }

        btnSubmit.setOnClickListener {
            addPost()
        }

        renderTopPosts()
        renderPosts()
    }

    private fun addPost() {
        val title = etTitle.text.toString().trim()
        val content = etContent.text.toString().trim()

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "제목과 내용을 입력하세요", Toast.LENGTH_SHORT).show()
            return
        }

        val date = SimpleDateFormat("yyyy.MM.dd", Locale.KOREA).format(Date())

        posts.add(
            0,
            Post(
                id = posts.size + 1,
                title = title,
                content = content,
                author = "익명",
                date = date,
                likes = 0
            )
        )

        etTitle.text.clear()
        etContent.text.clear()
        writeForm.visibility = View.GONE

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
                row.setPadding(16, 14, 16, 14)
                row.setBackgroundColor(0xFFFFFFFF.toInt())

                val rank = TextView(this)
                rank.text = "${index + 1}"
                rank.textSize = 16f
                rank.setTypeface(null, Typeface.BOLD)
                rank.setTextColor(0xFFEA580C.toInt())

                val title = TextView(this)
                title.text = post.title
                title.textSize = 14f
                title.setTextColor(0xFF111827.toInt())
                title.layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                title.setPadding(18, 0, 8, 0)

                val likes = TextView(this)
                likes.text = "♡ ${post.likes}"
                likes.textSize = 13f
                likes.setTextColor(0xFFEA580C.toInt())

                row.addView(rank)
                row.addView(title)
                row.addView(likes)

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
            title.setTypeface(null, Typeface.BOLD)
            title.setTextColor(0xFF111827.toInt())

            val content = TextView(this)
            content.text = post.content
            content.textSize = 14f
            content.setTextColor(0xFF4B5563.toInt())
            content.setPadding(0, 12, 0, 12)

            val info = TextView(this)
            info.text = "${post.author} · ${post.date}"
            info.textSize = 12f
            info.setTextColor(0xFF6B7280.toInt())

            val actionRow = LinearLayout(this)
            actionRow.orientation = LinearLayout.HORIZONTAL
            actionRow.setPadding(0, 14, 0, 0)

            val likeButton = Button(this)
            likeButton.text = if (post.liked) "♥ ${post.likes}" else "♡ ${post.likes}"
            likeButton.setOnClickListener {
                if (post.liked) {
                    post.likes -= 1
                    post.liked = false
                } else {
                    post.likes += 1
                    post.liked = true
                    Toast.makeText(this, "공감하기 +50P", Toast.LENGTH_SHORT).show()
                }
                renderTopPosts()
                renderPosts()
            }

            val commentButton = Button(this)
            commentButton.text = "댓글 ${post.comments.size}"
            commentButton.setOnClickListener {
                activeCommentPostId =
                    if (activeCommentPostId == post.id) null else post.id
                renderPosts()
            }

            actionRow.addView(likeButton)
            actionRow.addView(commentButton)

            card.addView(title)
            card.addView(content)
            card.addView(info)
            card.addView(actionRow)

            if (activeCommentPostId == post.id) {
                addCommentSection(card, post)
            }

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(0, 0, 0, 16)

            postContainer.addView(card, params)
        }
    }

    private fun addCommentSection(card: LinearLayout, post: Post) {
        val section = LinearLayout(this)
        section.orientation = LinearLayout.VERTICAL
        section.setPadding(0, 16, 0, 0)

        post.comments.forEach {
            val commentBox = TextView(this)
            commentBox.text = "${it.author} · ${it.date}\n${it.content}"
            commentBox.setTextColor(0xFF4B5563.toInt())
            commentBox.textSize = 13f
            commentBox.setPadding(14, 12, 14, 12)
            commentBox.setBackgroundColor(0xFFF9FAFB.toInt())

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
        input.layoutParams = LinearLayout.LayoutParams(0, 48, 1f)

        val sendButton = Button(this)
        sendButton.text = "등록"
        sendButton.setOnClickListener {
            val text = input.text.toString().trim()
            if (text.isEmpty()) return@setOnClickListener

            val date = SimpleDateFormat("yyyy.MM.dd", Locale.KOREA).format(Date())
            post.comments.add(Comment("익명", text, date))
            Toast.makeText(this, "댓글 작성 +30P", Toast.LENGTH_SHORT).show()
            activeCommentPostId = null
            renderPosts()
        }

        inputRow.addView(input)
        inputRow.addView(sendButton)

        section.addView(inputRow)
        card.addView(section)
    }
}