package com.example.siheung_seemoney.ui.custom

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.widget.AppCompatTextView
import java.text.DecimalFormat

class AnimatedNumberTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private var valueAnimator: ValueAnimator? = null
    private val decimalFormat = DecimalFormat("#,###")

    /**
     * 숫자를 애니메이션과 함께 변경합니다.
     * @param startValue 시작 숫자
     * @param endValue 목표 숫자
     * @param duration 애니메이션 지속 시간 (ms)
     */
    fun animateToNumber(
        startValue: Long,
        endValue: Long,
        duration: Long = 1500L
    ) {
        // 이전 애니메이션이 진행 중이라면 취소
        valueAnimator?.cancel()

        // 시작값과 종료값을 기반으로 애니메이터 생성
        valueAnimator = ValueAnimator.ofFloat(startValue.toFloat(), endValue.toFloat()).apply {
            this.duration = duration
            this.interpolator = DecelerateInterpolator() // 점점 느려지는 효과

            addUpdateListener { animator ->
                val animatedValue = (animator.animatedValue as Float).toLong()
                text = decimalFormat.format(animatedValue)
            }
        }

        valueAnimator?.start()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        // 메모리 릭 방지를 위해 뷰가 화면에서 사라질 때 애니메이션 취소
        valueAnimator?.cancel()
    }
}
