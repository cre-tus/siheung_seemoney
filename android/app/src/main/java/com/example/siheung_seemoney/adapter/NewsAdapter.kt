package com.example.siheung_seemoney.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.siheung_seemoney.data.News
import com.example.siheung_seemoney.databinding.ItemNewsBinding
import com.example.siheung_seemoney.util.DateUtils

/**
 * 뉴스 목록을 표시하기 위한 어댑터
 */
class NewsAdapter(private val onItemClick: (News) -> Unit) :
    RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    private var newsList: List<News> = emptyList()

    fun submitList(list: List<News>) {
        this.newsList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(newsList[position])
    }

    override fun getItemCount(): Int = newsList.size

    inner class NewsViewHolder(private val binding: ItemNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(news: News) {
            binding.newsTitle.text = news.title
            binding.newsTimeAgo.text = DateUtils.getTimeAgo(news.pubDate)
            binding.newsDate.text = DateUtils.formatToDisplayDate(news.pubDate)

            binding.root.setOnClickListener {
                onItemClick(news)
            }
        }
    }
}
