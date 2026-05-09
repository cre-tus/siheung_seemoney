package com.example.siheung_seemoney.ui.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.siheung_seemoney.data.model.CategoryBudget
import com.example.siheung_seemoney.databinding.ItemCategoryBudgetBinding
import com.example.siheung_seemoney.ui.adapter.CategoryBudgetAdapter.ViewHolder
import java.text.DecimalFormat

/**
 * 분석 화면의 분야별 예산 리스트(RecyclerView)를 연결해주는 어댑터 클래스입니다.
 * CategoryBudget 데이터를 받아 item_category_budget.xml 레이아웃에 바인딩합니다.
 */
class CategoryBudgetAdapter : RecyclerView.Adapter<ViewHolder>() {

    // 어댑터에서 관리하는 데이터 리스트
    private val items = mutableListOf<CategoryBudget>()

    /**
     * 새로운 데이터 리스트로 업데이트하고 화면을 갱신합니다.
     */
    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: List<CategoryBudget>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCategoryBudgetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    /**
     * 각각의 리스트 아이템 뷰를 관리하는 ViewHolder 클래스.
     */
    class ViewHolder(private val binding: ItemCategoryBudgetBinding) : RecyclerView.ViewHolder(binding.root) {
        // 금액을 3자리마다 콤마(,)로 구분하기 위한 포맷터
        private val formatter = DecimalFormat("#,###")

        fun bind(item: CategoryBudget) {
            // 카테고리 이름 및 예산 텍스트 바인딩
            binding.tvCategoryName.text = "● ${item.categoryName}"
            binding.tvCategoryBudget.text = "${formatter.format(item.budget)}원"
            binding.tvCategoryPercentage.text = "${item.percentage}%"
            // 변동률 및 화살표 표시 로직 제거됨 (요구사항 반영)

            // 프로그레스 바 수치 및 색상(Drawable) 바인딩
            binding.pbCategory.progress = item.percentage
            binding.pbCategory.progressDrawable = ContextCompat.getDrawable(binding.root.context, item.colorResId)
        }
    }
}
