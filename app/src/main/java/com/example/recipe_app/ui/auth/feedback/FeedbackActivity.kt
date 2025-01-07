package com.example.recipe_app.ui.auth.feedback
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.recipe_app.R
import com.example.recipe_app.ui.models.Feedback
import com.example.recipe_app.services.FeedbackServices
import com.google.firebase.auth.FirebaseAuth

class FeedbackActivity : AppCompatActivity() {

    private val feedbackService = FeedbackServices.FeedbackService()
    private val auth = FirebaseAuth.getInstance()
    private lateinit var btnSubmitFeedback: Button
    private lateinit var etFeedback :EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback2)
        btnSubmitFeedback = findViewById(R.id.btnSubmitFeedback)
        etFeedback = findViewById(R.id.etFeedback)
        btnSubmitFeedback.setOnClickListener {
            val feedbackText = etFeedback.text.toString().trim()
            submitFeedback(feedbackText)
        }
    }

    private fun submitFeedback(feedbackText: String) {
        val userId = auth.currentUser?.uid ?: return
        val feedback = Feedback(
            userId = userId,
            feedbackText = feedbackText
        )

        feedbackService.submitFeedback(feedback) { success ->
            if (success) {
                Toast.makeText(this, "Feedback submitted. Thank you!", Toast.LENGTH_LONG).show()
                finish()
            } else {
                Toast.makeText(this, "Failed to submit feedback. Please try again.", Toast.LENGTH_LONG).show()
            }
        }
    }
}
