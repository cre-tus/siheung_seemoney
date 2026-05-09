package com.example.siheung_seemoney.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.siheung_seemoney.adapter.NewsAdapter
import com.example.siheung_seemoney.base.BaseActivity
import com.example.siheung_seemoney.data.News
import com.example.siheung_seemoney.data.NewsApiService
import com.example.siheung_seemoney.databinding.ActivityAnalysisBinding
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * 시흥시 재정 분석 및 뉴스 화면
 * BaseActivity를 상속받아 하단 네비게이션 및 공통 바인딩 처리를 수행합니다.
 */
class AnalysisActivity : BaseActivity<ActivityAnalysisBinding>() {

    private lateinit var newsAdapter: NewsAdapter

    // 백엔드 API 베이스 URL (에뮬레이터 접속용)
    private val BASE_URL = "http://10.0.2.2:8081"

    // BaseActivity의 추상 메서드 구현 (바인딩 생성)
    override fun inflateBinding(): ActivityAnalysisBinding {
        return ActivityAnalysisBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // BaseActivity.onCreate()에서 이미 binding 초기화 및 setContentView가 수행됩니다.

        setupRecyclerView()
        fetchNews()
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter { news ->
            // 뉴스 아이템 클릭 시 브라우저로 링크 열기
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(news.link))
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(this, "링크를 열 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.newsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@AnalysisActivity)
            adapter = newsAdapter
            isNestedScrollingEnabled = false // ScrollView 내에서 부드러운 스크롤을 위해 설정
        }
    }

    private fun fetchNews() {
        // API 요청 로그 확인을 위한 인터셉터
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        val service = retrofit.create(NewsApiService::class.java)

        service.getNews().enqueue(object : Callback<List<News>> {
            override fun onResponse(call: Call<List<News>>, response: Response<List<News>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        newsAdapter.submitList(it)
                    }
                } else {
                    Log.e("AnalysisActivity", "API Error: ${response.code()}")
                }
            }

            override fun onFailure