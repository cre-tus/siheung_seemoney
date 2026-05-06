package com.example.siheung_seemoney.ui

import android.os.Bundle
import com.example.siheung_seemoney.base.BaseActivity
import com.example.siheung_seemoney.databinding.ActivityParticipateBinding

class ParticipateActivity : BaseActivity<ActivityParticipateBinding>() {

    // 레이아웃 인플레이터를 사용하여 ActivityParticipateBinding 인스턴스를 생성
    override fun inflateBinding(): ActivityParticipateBinding {
        return ActivityParticipateBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}