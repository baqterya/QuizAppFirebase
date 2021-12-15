package com.example.quizappfirebase.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.quizappfirebase.R
import com.example.quizappfirebase.databinding.FragmentQuizModePickerBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class QuizModePickerFragment : Fragment() {
    private var _binding: FragmentQuizModePickerBinding? = null
        private val binding get() = _binding!!

    private val args by navArgs<QuizModePickerFragmentArgs>()
    private lateinit var questionArray: ArrayList<String>
    private lateinit var answerArray: ArrayList<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuizModePickerBinding.inflate(inflater, container, false)
        questionArray = args.arrayQuestions.toCollection(ArrayList())
        answerArray = args.arrayAnswers.toCollection(ArrayList())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation_bar)
            .visibility = View.GONE


    }

    override fun onPause() {
        super.onPause()
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation_bar)
            .visibility = View.VISIBLE
    }
}