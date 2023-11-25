package com.codingtester.eatwell.view.reward

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.codingtester.eatwell.databinding.ActivityRewardBinding
import com.codingtester.eatwell.view.reward.points.PointsActivity
import com.codingtester.eatwell.view.reward.subscribe.SubscribeActivity
import com.codingtester.eatwell.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RewardsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRewardBinding
    private val registerViewModel by viewModels<RegisterViewModel>()
    private var isUserLogin = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRewardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerViewModel.loginLiveData.observe(this) { response ->
            if(response?.firebaseUser != null) {
                isUserLogin = true
            }
        }

        binding.cardPoints.setOnClickListener {
            if(isUserLogin) {
                startActivity(Intent(this, PointsActivity::class.java))
            } else {
                SubscribeDialog().show(supportFragmentManager, "sup")
            }
        }

        binding.cardSubsripe.setOnClickListener {
            if(isUserLogin) {
                startActivity(Intent(this, SubscribeActivity::class.java))
            } else {
                SubscribeDialog().show(supportFragmentManager, "sup")
            }
        }

        binding.imgBack.setOnClickListener { finish() }
    }
}