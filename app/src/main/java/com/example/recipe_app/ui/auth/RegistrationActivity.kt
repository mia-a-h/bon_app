package com.example.recipe_app.ui.auth
import android.content.Intent
import com.example.recipe_app.R
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegistrationActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val firestore = FirebaseFirestore.getInstance()
    private lateinit var password: EditText
    private lateinit var etemail: EditText
    private lateinit var registerbtn:Button
    private lateinit var backbtn:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        auth = FirebaseAuth.getInstance()
        registerbtn = findViewById(R.id.registerbtn)
        etemail = findViewById(R.id.etemail)
        password = findViewById(R.id.password)
        backbtn=findViewById(R.id.ivBackButton)

        registerbtn.setOnClickListener {
            val email = etemail.text.toString().trim()
            val password = password.text.toString().trim()
            registerUser(email, password)
        }

        backbtn.setOnClickListener {
          finish()
        }
    }

    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Registration successful
                    val user = auth.currentUser
                    val userProfile = mapOf(
                        "email" to email,
                        "role" to "standard",
                        "createdAt" to com.google.firebase.firestore.FieldValue.serverTimestamp()
                    )
                    firestore.collection("users").document(user!!.uid)
                        .set(userProfile)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Registration successful!", Toast.LENGTH_LONG).show()
                            // Redirect to main app or login
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Failed to create profile: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                } else
                {
                    // Registration failed
                    Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }
}

