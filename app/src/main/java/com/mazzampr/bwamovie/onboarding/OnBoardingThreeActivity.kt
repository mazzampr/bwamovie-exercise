package com.mazzampr.bwamovie.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mazzampr.bwamovie.R
import com.mazzampr.bwamovie.sign.sigin.SignInActivity
import kotlinx.android.synthetic.main.activity_on_boarding_three.*

class OnBoardingThreeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding_three)

        button_home.setOnClickListener {
            finishAffinity()
            startActivity(Intent(this@OnBoardingThreeActivity, SignInActivity::class.java))
        }
    }

}