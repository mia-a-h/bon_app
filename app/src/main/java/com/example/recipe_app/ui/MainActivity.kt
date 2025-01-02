package com.example.recipe_app.ui

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.recipe_app.R
import com.example.recipe_app.databinding.ActivityMain2Binding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {

        private lateinit var binding: ActivityMain2Binding
        private lateinit var auth: FirebaseAuth

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            binding = ActivityMain2Binding.inflate(layoutInflater)
            setContentView(binding.root)
            auth = FirebaseAuth.getInstance()

            val navView: BottomNavigationView = binding.navView
            val navController = findNavController(R.id.nav_host_fragment_activity_main2)
            val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications, R.id.navigation_profile))
            setupActionBarWithNavController(navController, appBarConfiguration)
            navView.setupWithNavController(navController)

            // Listen for authentication changes
            auth.addAuthStateListener { firebaseAuth ->
                val user = firebaseAuth.currentUser
                updateUI(user)
            }
        }

        private fun updateUI(user: FirebaseUser?) {
            val navView: BottomNavigationView = binding.navView
            if (user != null) {
                // User is signed in
               // navView.menu.findItem(R.id.login).isVisible = false
               // navView.menu.findItem(R.id.register).isVisible = false
            } else {
                // User is signed out
                //navView.menu.findItem(R.id.login).isVisible = true
               // navView.menu.findItem(R.id.register).isVisible = true
            }
        }

        override fun onDestroy() {
            super.onDestroy()
            auth.removeAuthStateListener { }
        }
    }

