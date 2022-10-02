package com.mazzampr.bwamovie.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mazzampr.bwamovie.R
import com.mazzampr.bwamovie.sign.sigin.SignInActivity
import com.mazzampr.bwamovie.utils.Preferences
import kotlinx.android.synthetic.main.activity_on_boarding_one.*

class OnBoardingOneActivity : AppCompatActivity() {

    lateinit var preference:Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding_one)

        preference = Preferences(this)
        if(preference.getValues("onboarding").equals("1")) {
            finishAffinity()

            var intent = Intent(this@OnBoardingOneActivity, SignInActivity::class.java)
            startActivity(intent)
        }

        button_home.setOnClickListener {
            var intent = Intent(this@OnBoardingOneActivity, OnBoardingTwoActivity::class.java)
            startActivity(intent)
        }

        button_daftar.setOnClickListener {
            preference.setValues("onboarding", "1")
            finishAffinity()
            var intent = Intent(this@OnBoardingOneActivity, SignInActivity::class.java)
            startActivity(intent)
        }
    }
}