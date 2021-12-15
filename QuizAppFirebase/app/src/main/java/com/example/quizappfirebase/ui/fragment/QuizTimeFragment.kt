package com.example.quizappfirebase.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.quizappfirebase.R
import com.example.quizappfirebase.databinding.FragmentQuizTimeBinding


class QuizTimeFragment : Fragment() {
    private var _binding: FragmentQuizTimeBinding? = null
        private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuizTimeBinding.inflate(inflater, container, false)
        return binding.root
    }


}