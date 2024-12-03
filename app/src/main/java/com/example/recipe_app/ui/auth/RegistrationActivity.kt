package com.example.recipe_app.ui.auth

import android.app.DatePickerDialog
import android.content.Intent
import com.example.recipe_app.R
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class RegistrationActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val firestore = FirebaseFirestore.getInstance()

    private lateinit var password: EditText
    private lateinit var etemail: EditText
    private lateinit var etFirstName: EditText
    private lateinit var etLastName: EditText
    private lateinit var etBirthday: EditText
    private lateinit var spinnerGender: Spinner
    private lateinit var registerbtn: Button
    private lateinit var backbtn: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        auth = FirebaseAuth.getInstance()

        // Initialize Views
        registerbtn = findViewById(R.id.registerbtn)
        etemail = findViewById(R.id.etemail)
        password = findViewById(R.id.password)
        etFirstName = findViewById(R.id.fn)
        etLastName = findViewById(R.id.ln)
        etBirthday = findViewById(R.id.bday)
        spinnerGender = findViewById(R.id.gender)
        backbtn = findViewById(R.id.ivBackButton)


        etBirthday.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val birthday = "${selectedDay}/${selectedMonth + 1}/$selectedYear"
                    etBirthday.setText(birthday)
                }, year, month, day
            )
            datePicker.show()
        }

        registerbtn.setOnClickListener {
            val email = etemail.text.toString().trim()
            val password = password.text.toString().trim()
            val firstName = etFirstName.text.toString().trim()
            val lastName = etLastName.text.toString().trim()
            val gender = spinnerGender.selectedItem.toString()
            val birthday = etBirthday.text.toString().trim()

            registerUser(email, password, firstName, lastName, gender, birthday)
        }

        backbtn.setOnClickListener {
            finish()
        }
    }

    private fun registerUser(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        gender: String,
        birthday: String
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Registration successful
                    val user = auth.currentUser
                    val userProfile = mapOf(
                        "email" to email,
                        "firstName" to firstName,
                        "lastName" to lastName,
                        "gender" to gender,
                        "birthday" to birthday,
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
                } else {
                    // Registration failed
                    Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }
}
