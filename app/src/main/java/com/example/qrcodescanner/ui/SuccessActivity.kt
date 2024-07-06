package com.example.qrcodescanner.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.qrcodescanner.R
import com.example.qrcodescanner.databinding.ActivitySuccessBinding

class SuccessActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySuccessBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySuccessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up the animation
        binding.lottieAnimationView.setAnimation(R.raw.animation)
        binding.lottieAnimationView.playAnimation()
    }
}
