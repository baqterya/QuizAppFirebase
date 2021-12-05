package com.example.quizappfirebase.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.quizappfirebase.R
import com.example.quizappfirebase.databinding.ActivityLoginBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        checkUserLogin()
    }

    private fun checkUserLogin(){
        /*
         * Checks if user is logged in and skips login step if they are.
         */
        val user = Firebase.auth.currentUser

        if (user != null) {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra("user_id", user.uid)
            intent.putExtra("email_id", user.email)
            startActivity(intent)
            this.finish()
        }
    }
}