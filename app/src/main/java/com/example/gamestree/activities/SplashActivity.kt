package com.example.gamestree.activities

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.gallery.activities.VideoActivity
import com.example.gamestree.databinding.ActivitySplashBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
//    Testing Branch
    private val binding by lazy {
        ActivitySplashBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.splashAnimation()
    }

    private fun ActivitySplashBinding.splashAnimation() {
        // Create zoom-in animation
        val scaleX = ObjectAnimator.ofFloat(splashVector, "scaleX", 0.1f, 1.1f)
        val scaleY = ObjectAnimator.ofFloat(splashVector, "scaleY", 0.1f, 1.1f)
        val translationY = ObjectAnimator.ofFloat(splashVector, "translationY", 1300f)
        scaleX.duration = 4000
        scaleY.duration = 4000
        translationY.duration = 4000

        // Create an AnimatorSet to play animations together
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(scaleX, scaleY)

        // Add listener to start new activity after animation ends
        animatorSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                startActivity(Intent(this@SplashActivity, VideoActivity::class.java))
                finish() // Optional: Call finish() to remove the splash activity from the back stack
            }
        })

        // Start the animations
        animatorSet.start()
    }
}