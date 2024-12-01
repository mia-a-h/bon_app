package com.example.recipe_app.ui.models
import com.google.firebase.Timestamp
//for feedback collection
data class Feedback(
    val userId: String = "",
    val feedbackText: String = "",
    val timestamp: Timestamp = Timestamp.now()
)
