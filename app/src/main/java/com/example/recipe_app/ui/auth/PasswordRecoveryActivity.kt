package com.example.recipe_app.ui.auth

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.recipe_app.R
import com.example.recipe_app.ui.auth.profile.ProfileActivity
import com.google.firebase.auth.FirebaseAuth

class PasswordRecoveryActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var email: EditText
    private lateinit var resetbtn: Button
    private lateinit var backbtn: ImageView
    private lateinit var prfbtn: ImageView
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_recovery)

        // Initialize views
        resetbtn = findViewById(R.id.btnResetPassword)
        email = findViewById(R.id.email)
        backbtn=findViewById(R.id.ivBackButton)
        prfbtn=findViewById(R.id.ivprofilebtn)
        // Initialize FirebaseAuth
        auth = FirebaseAuth.getInstance()

        // Set up click listener for the reset button
        resetbtn.setOnClickListener {
            val emailInput = email.text.toString().trim()
            if (emailInput.isNotEmpty()) {
                sendPasswordResetEmail(emailInput)
            } else {
                Toast.makeText(this, "Please enter your email address.", Toast.LENGTH_SHORT).show()
            }
        }
        backbtn.setOnClickListener {
//          startActivity(Intent(this, ProfileActivity::class.java))
        finish()
        }
        prfbtn.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))

        }
    }

    private fun sendPasswordResetEmail(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Inform user to check their email
                    Toast.makeText(this, "Password reset email sent.", Toast.LENGTH_LONG).show()
                    finish() // Close the activity
                } else {
                    // Handle errors
                    Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }
}
