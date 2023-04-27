package com.atulya.androidpropertyanimation

import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.atulya.androidpropertyanimation.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    /**
     * Use Transitions for animation that run at the start of a activity
     */

    private var sunUp = true

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

    private fun getSunDownAnimator(reverse: Boolean = false): ObjectAnimator {
        val sunYStart = binding.sun.top.toFloat()
        val sunYEnd = binding.sky.height.toFloat()

        val (start, end) = if (reverse) {
            listOf(sunYEnd, sunYStart)
        } else {
            listOf(sunYStart, sunYEnd)
        }

        val heightAnimator = ObjectAnimator
            .ofFloat(binding.sun, "y", start, end)
            .setDuration(3000)

        heightAnimator.interpolator = AccelerateInterpolator()
        return heightAnimator
    }

    private fun getDaySkyColorAnimator(reverse: Boolean = false): ObjectAnimator {

        val (startColor, endColor) = if (reverse) {
            listOf(sunSetSkyColour, blueSkyColour)
        } else {
            listOf(blueSkyColour, sunSetSkyColour)
        }

        val skyColorAnimator = ObjectAnimator
            .ofInt(binding.sky, "backgroundColor", startColor, endColor)
            .setDuration(3000)

        skyColorAnimator.setEvaluator(ArgbEvaluator())

        return skyColorAnimator
    }


    private fun getNightSkyColorAnimator(reverse: Boolean = false): ObjectAnimator {

        val (startColor, endColor) = if (reverse) {
            listOf(nightSkyColour, sunSetSkyColour)
        } else {
            listOf(sunSetSkyColour, nightSkyColour)
        }

        val nightSkyColorAnimator = ObjectAnimator
            .ofInt(binding.sky, "backgroundColor", startColor, endColor)
            .setDuration(3000)

        nightSkyColorAnimator.setEvaluator(ArgbEvaluator())

        return nightSkyColorAnimator
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.scene.setOnClickListener {

            sunUp = if (sunUp){
                sunSetAnimation()
                false
            }
            else{
                sunUpAnimation()
                true
            }
        }

        val ro = ObjectAnimator
            .ofFloat(binding.sun, "rotation", 0f, 360f)
            .setDuration(8000)

        ro.repeatCount = ValueAnimator.INFINITE
        ro.interpolator = LinearInterpolator()

        ro.start()
    }

    private fun sunSetAnimation() {

        if (sunUp) {
            val animatorSet = AnimatorSet()
            animatorSet.play(getSunDownAnimator())
                .with(getDaySkyColorAnimator())
                .before(getNightSkyColorAnimator())

            animatorSet.start()

        }

    }

    private fun sunUpAnimation() {

        if (!sunUp) {
            val animatorSet = AnimatorSet()

            animatorSet.play(getSunDownAnimator(reverse = true))
                .with(getDaySkyColorAnimator(reverse = true))
                .after(getNightSkyColorAnimator(reverse = true))

            animatorSet.start()

        }
    }

}