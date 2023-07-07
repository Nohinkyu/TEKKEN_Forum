package com.nik.tkforum.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.nik.tkforum.R
import com.nik.tkforum.databinding.ActivityMainBinding
import com.nik.tkforum.ui.home.HomeFragmentDirections

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpBottomNavigation()
    }

    private fun setUpBottomNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container_view_main) as NavHostFragment
        val navController = navHostFragment.findNavController()
        binding.navMainBottom.setupWithNavController(navController)

        if (firebaseAuth.currentUser == null) {
            val action = HomeFragmentDirections.actionNavHomeToNavSignIn()
            navController.navigate(action)
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            val shouldShowBottomNavigation = when (destination.id) {
                R.id.nav_home,
                R.id.nav_video,
                R.id.nav_chat,
                R.id.nav_setting -> true
                else -> false
            }
            binding.navMainBottom.visibility = if (shouldShowBottomNavigation) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }
}