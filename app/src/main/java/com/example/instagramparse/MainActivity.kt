package com.example.instagramparse

import android.os.Bundle
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.instagramparse.fragments.ComposeFragment
import com.example.instagramparse.fragments.FeedFragment
import com.example.instagramparse.fragments.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


// Create posts by taking a photo with camera
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentManager: FragmentManager = supportFragmentManager

        var progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val bottomNavBar = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNavBar.setOnItemSelectedListener {
            item ->

            var fragmentToShow: Fragment? = null
            when(item.itemId) {

                R.id.action_home -> {
                    fragmentToShow = FeedFragment()
                }
                R.id.action_compose -> {
                    fragmentToShow = ComposeFragment()
                }
                R.id.action_profile -> {
                    fragmentToShow = ProfileFragment()
                }
            }

            if (fragmentToShow != null) {
                fragmentManager.beginTransaction().replace(R.id.frameContainer, fragmentToShow).commit()
            }

            true
        }

        bottomNavBar.selectedItemId = R.id.action_home
    }

    companion object {
        val TAG = "MainActivity"
    }

}