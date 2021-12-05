package com.example.quizappfirebase.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.quizappfirebase.data.User
import com.example.quizappfirebase.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val user: User = intent.extras?.get("currentUser") as User
    }
}