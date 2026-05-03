package com.example.siheung_seemoney.ui

import android.os.Bundle
import com.example.siheung_seemoney.base.BaseActivity
import com.example.siheung_seemoney.databinding.ActivityBoardBinding

class BoardActivity : BaseActivity<ActivityBoardBinding>() {

    override fun inflateBinding(): ActivityBoardBinding {
        return ActivityBoardBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 제안(게시판) 로직 여기 추가
    }
}