package com.example.siheung_seemoney.ui

import android.os.Bundle
import com.example.siheung_seemoney.base.BaseActivity
import com.example.siheung_seemoney.databinding.ActivityMypageBinding

class MyPageActivity : BaseActivity<ActivityMypageBinding>() {

    override fun inflateBinding(): ActivityMypageBinding {
        return ActivityMypageBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 여기다가 마이페이지 로직 추가하면 됨
    }
}