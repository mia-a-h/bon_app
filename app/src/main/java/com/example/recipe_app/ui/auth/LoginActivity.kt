package com.example.recipe_app.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.recipe_app.R
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var loginbtn: Button
    private lateinit var etEmail: EditText
    private lateinit var etpassword: EditText
    private lateinit var tvForgotPassword: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        loginbtn = findViewById(R.id.loginbtn)
        etEmail = findViewById(R.id.etEmail)
        etpassword = findViewById(R.id.etpassword)
        tvForgotPassword = findViewById(R.id.tvForgotPassword)
        loginbtn.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etpassword.text.toString().trim()
            loginUser(email, password)
        }

        tvForgotPassword.setOnClickListener {
            // Redirect to PasswordRecoveryActivity
            startActivity(Intent(this, PasswordRecoveryActivity::class.java))
        }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Login successful
                    Toast.makeText(this, "Login successful!", Toast.LENGTH_LONG).show()
                    // Redirect to MainActivity
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    // Login failed
                    Toast.makeText(this, "Login failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }
}
