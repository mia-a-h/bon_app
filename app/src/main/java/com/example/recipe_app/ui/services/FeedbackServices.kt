package com.example.recipe_app.ui.services

import com.example.recipe_app.ui.models.Feedback
import com.google.firebase.firestore.FirebaseFirestore

// kotlin classe service
class FeedbackServices {
    class FeedbackService {
        private val firestore = FirebaseFirestore.getInstance()
        fun submitFeedback(feedback: Feedback, onComplete: (Boolean) -> Unit) {
            firestore.collection("feedback").add(feedback)
                .addOnSuccessListener {
                    onComplete(true)
                }
                .addOnFailureListener {
                    onComplete(false)
                }
        }
    }
}