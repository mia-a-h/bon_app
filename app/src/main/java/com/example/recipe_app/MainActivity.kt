package com.example.recipe_app

import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.recipe_app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        Log.d("MainActivity", "onCreate started")
//
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        val navView: BottomNavigationView = binding.navView
//
//        val navController = findNavController(R.id.nav_host_fragment_activity_main)
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        Log.d("MainActivity", "onCreate started,nav controller")
//
//        val appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications, R.id.navigation_profile
//            )
//        )
//        Log.d("MainActivity", "onCreate started, app bar config")
//
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        navView.setupWithNavController(navController)
//        Log.d("MainActivity", "onCreate started, set up")
        Log.d("MainActivity", "onCreate started")

        try {
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)

            val navView: BottomNavigationView = binding.navView
            if (navView == null) {
                Log.e("MainActivity", "BottomNavigationView is null")
            }

            val navController = findNavController(R.id.nav_host_fragment)
            if (navController == null) {
                Log.e("MainActivity", "NavController is null")
            }

            Log.d("MainActivity", "onCreate started, nav controller")

            val appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications, R.id.navigation_profile
                )
            )
            Log.d("MainActivity", "onCreate started, app bar config")

            setupActionBarWithNavController(navController, appBarConfiguration)
            navView.setupWithNavController(navController)
            Log.d("MainActivity", "onCreate started, set up")

        } catch (e: Exception) {
            Log.e("MainActivity", "Error during onCreate: ${e.message}", e)
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

}