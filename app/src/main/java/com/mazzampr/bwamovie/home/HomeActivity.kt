package com.mazzampr.bwamovie.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.mazzampr.bwamovie.R
import com.mazzampr.bwamovie.home.dashboard.DashboardFragment
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val fragmentHome = DashboardFragment()
        val fragmentTicket = TicketFragment()
        val fragmentProfile = ProfileFragment()

        setFragment(fragmentHome)

        btn_dashboard.setOnClickListener {
            setFragment(fragmentHome)
            changeIcon(btn_dashboard, R.drawable.ic_home_active)
            changeIcon(btn_tiket, R.drawable.ic_tiket)
            changeIcon(btn_profile, R.drawable.ic_profile)
        }
        btn_tiket.setOnClickListener {
            setFragment(fragmentTicket)
            changeIcon(btn_dashboard, R.drawable.ic_home)
            changeIcon(btn_tiket, R.drawable.ic_tiket_active)
            changeIcon(btn_profile, R.drawable.ic_profile)
        }
        btn_profile.setOnClickListener {
            setFragment(fragmentProfile)
            changeIcon(btn_dashboard, R.drawable.ic_home)
            changeIcon(btn_tiket, R.drawable.ic_tiket)
            changeIcon(btn_profile, R.drawable.ic_profile_active)
        }
    }

    private fun setFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.layout_frame, fragment)
        fragmentTransaction.commit()
    }

    private fun changeIcon(imageView: ImageView, int : Int) {
        imageView.setImageResource(int)
    }


}