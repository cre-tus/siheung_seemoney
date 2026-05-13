package com.example.siheung_seemoney.ui.news

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.example.siheung_seemoney.base.BaseActivity
import com.example.siheung_seemoney.databinding.ActivityNewsDetailBinding
import com.example.siheung_seemoney.util.DateUtils

class NewsDetailActivity : BaseActivity<ActivityNewsDetailBinding>() {

    override fun inflateBinding(): ActivityNewsDetailBinding {
        return ActivityNewsDetailBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 인텐트로 전달받은 뉴스 데이터 추출
        val title = intent.getStringExtra("title") ?: ""
        val summary = intent.getStringExtra("summary") ?: ""
        val pubDate = intent.getStringExtra("pubDate") ?: ""
        val link = intent.getStringExtra("link") ?: ""

        // 데이터 바인딩
        binding.tvDetailTitle.text = title
        binding.tvDetailDate.text = DateUtils.formatToDisplayDate(pubDate)
        binding.tvDetailContent.text = summary

        // 뒤로가기 버튼
        binding.btnBack.setOnClickListener {
            finish()
        }

        // 원문 보기 버튼
        binding.btnOpenWeb.setOnClickListener {
            if (link.isNotEmpty()) {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                startActivity(browserIntent)
            }
        }
    }
}
