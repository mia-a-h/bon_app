package com.example.recipe_app.services
    import com.google.firebase.auth.FirebaseAuth
    import com.google.firebase.auth.FirebaseUser
    class AuthServices {
        private var auth: FirebaseAuth = FirebaseAuth.getInstance()
        fun registerUser(email: String, password: String, onComplete: (Boolean, String) -> Unit) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Send verification email
                        auth.currentUser?.sendEmailVerification()?.addOnCompleteListener { verifyTask ->
                            if (verifyTask.isSuccessful) {
                                onComplete(true, "Registered successfully. Verification email sent.")
                            } else {
                                onComplete(false, "Registration successful but failed to send verification email.")
                            }
                        }
                    } else {
                        onComplete(false, task.exception?.message ?: "Registration failed due to unknown error.")
                    }
                }
        }

        fun loginUser(email: String, password: String, onComplete: (Boolean, String) -> Unit) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful && auth.currentUser?.isEmailVerified == true) {
                        onComplete(true, "Login successful.")
                    } else {
                        if (auth.currentUser?.isEmailVerified == false) {
                            onComplete(false, "Please verify your email first.")
                        } else {
                            onComplete(false, task.exception?.message ?: "Login failed due to unknown error.")
                        }
                    }
                }
        }

        fun resetPassword(email: String, onComplete: (Boolean, String) -> Unit) {
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        onComplete(true, "Reset password email sent.")
                    } else {
                        onComplete(false, task.exception?.message ?: "Failed to send reset email.")
                    }
                }
        }
        fun logoutUser() {
            auth.signOut()
        }
        fun getCurrentUser(): FirebaseUser? {
            return auth.currentUser
        }
    }
