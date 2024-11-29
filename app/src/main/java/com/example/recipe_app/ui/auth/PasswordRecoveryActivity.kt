package com.example.recipe_app.ui.auth
import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.recipe_app.R
import com.google.firebase.auth.FirebaseAuth

class PasswordRecoveryActivity {


    class PasswordRecoveryActivity : AppCompatActivity() {

        private lateinit var auth: FirebaseAuth
        private lateinit var email: EditText
        private lateinit var resetbtn: Button
        @SuppressLint("MissingInflatedId")
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_password_recovery)
            resetbtn = findViewById(R.id.btnResetPassword)
            email = findViewById(R.id.email)
            auth = FirebaseAuth.getInstance()

            resetbtn.setOnClickListener {
                val email = email.text.toString().trim()
                sendPasswordResetEmail(email)
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

}