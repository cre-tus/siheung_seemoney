package com.example.siheung_seemoney.data

import com.google.gson.annotations.SerializedName

/**
 * 백엔드에서 내려오는 뉴스 데이터 모델
 */
data class News(
    @SerializedName("title") val title: String,
    @SerializedName("link") val link: String,
    @SerializedName("summary") val summary: String,
    @SerializedName("pubDate") val pubDate: String
)
