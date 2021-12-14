package com.example.quizappfirebase.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.quizappfirebase.databinding.ActivityQuizBinding

class QuizActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuizBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}