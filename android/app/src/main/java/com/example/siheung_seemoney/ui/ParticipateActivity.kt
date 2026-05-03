package com.example.siheung_seemoney.ui

import android.os.Bundle
import com.example.siheung_seemoney.base.BaseActivity
import com.example.siheung_seemoney.databinding.ActivityBoardBinding
import com.example.siheung_seemoney.databinding.ActivityParticipateBinding

class ParticipateActivity : BaseActivity<ActivityParticipateBinding>() {

    override fun inflateBinding(): ActivityParticipateBinding {
        return ActivityParticipateBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 참여 로직 여기 추가
    }
}