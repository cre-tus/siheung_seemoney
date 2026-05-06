package com.example.siheung_seemoney.ui

import android.os.Bundle
import com.example.siheung_seemoney.base.BaseActivity
import com.example.siheung_seemoney.databinding.ActivityBoardBinding

class BoardActivity : BaseActivity<ActivityBoardBinding>() {

    // 레이아웃 인플레이터를 사용하여 ActivityBoardBinding 인스턴스를 생성
    override fun inflateBinding(): ActivityBoardBinding {
        return ActivityBoardBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}