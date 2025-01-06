package com.example.recipe_app.ui.auth.admin
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.recipe_app.R
import com.example.recipe_app.services.UserService
import com.google.firebase.auth.FirebaseAuth


class AdminDashboardActivity : AppCompatActivity() {

    private val userService = UserService()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)

        val currentUser = auth.currentUser
        currentUser?.let {
            userService.getUserRole(it.uid) { role ->
                if (role == "admin") {
                    // Show admin features
                    showAdminFeatures()
                } else {
                    // Redirect or show access denied
                    finish()
                }
            }
        }
    }

    private fun showAdminFeatures() {
        // Implement admin-specific UI components and functionalities
    }
}
