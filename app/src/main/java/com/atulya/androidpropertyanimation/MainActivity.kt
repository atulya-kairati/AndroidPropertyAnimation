package com.atulya.androidpropertyanimation

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.animation.AccelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.atulya.androidpropertyanimation.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val blueSkyColour by lazy {
        ContextCompat.getColor(this, R.color.blue_sky)
    }

    private val sunSetSkyColour by lazy {
        ContextCompat.getColor(this, R.color.sunset_sky)
    }

    private val nightSkyColour by lazy {
        ContextCompat.getColor(this, R.color.night_sky)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.scene.setOnClickListener {
            startAnimation()
        }
    }

    private fun startAnimation() {
        val sunYStart = binding.sun.top.toFloat()
        val sunYEnd = binding.sky.height.toFloat()

        val heightAnimator = ObjectAnimator
            .ofFloat(binding.sun, "y", sunYStart, sunYEnd)
            .setDuration(3000)

        heightAnimator.interpolator = AccelerateInterpolator()

        val skyColorAnimator = ObjectAnimator
            .ofInt(binding.sky, "backgroundColor", blueSkyColour, sunSetSkyColour)
            .setDuration(3000)

        skyColorAnimator.setEvaluator(ArgbEvaluator())

        val nightSkyColorAnimator = ObjectAnimator
            .ofInt(binding.sky, "backgroundColor", sunSetSkyColour, nightSkyColour)
            .setDuration(1500)

        nightSkyColorAnimator.setEvaluator(ArgbEvaluator())

        val animatorSet = AnimatorSet()

        animatorSet.play(heightAnimator)
            .with(skyColorAnimator)
            .before(nightSkyColorAnimator)

        animatorSet.start()

    }
}