package com.example.siheung_seemoney.util

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    private val naverDateFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH)
    private val displayDateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.KOREA)

    fun formatToDisplayDate(pubDate: String): String {
        return try {
            val date = naverDateFormat.parse(pubDate)
            displayDateFormat.format(date ?: Date())
        } catch (e: Exception) {
            pubDate
        }
    }

    fun getTimeAgo(pubDate: String): String {
        return try {
            val date = naverDateFormat.parse(pubDate) ?: return ""
            val now = System.currentTimeMillis()
            val diff = now - date.time

            val minutes = diff / (1000 * 60)
            val hours = minutes / 60
            val days = hours / 24

            when {
                minutes < 1 -> "방금 전"
                minutes < 60 -> "${minutes}분 전"
                hours < 24 -> "${hours}시간 전"
                else -> "${days}일 전"
            }
        } catch (e: Exception) {
            ""
        }
    }
}
