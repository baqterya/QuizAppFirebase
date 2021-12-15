package com.example.quizappfirebase.ui.fragment

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.quizappfirebase.R
import com.example.quizappfirebase.data.QuestionAndAnswer
import com.example.quizappfirebase.databinding.FragmentQuizModePickerBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class QuizModePickerFragment : Fragment() {
    private var _binding: FragmentQuizModePickerBinding? = null
        private val binding get() = _binding!!

    private val args by navArgs<QuizModePickerFragmentArgs>()
    private lateinit var arrayQuestionsAndAnswers: ArrayList<QuestionAndAnswer>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentQuizModePickerBinding.inflate(inflater, container, false)
        arrayQuestionsAndAnswers = args.arrayQuestionsAndAnswers.toCollection(ArrayList())
        checkQuestionAmount()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        menuVisibility(false)
    }

    override fun onPause() {
        super.onPause()
        menuVisibility(true)
    }

    private fun menuVisibility(switch: Boolean) {
        if (switch) {
            requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation_bar)
                .visibility = View.VISIBLE
            val toolbar = requireActivity().findViewById<Toolbar>(R.id.top_toolbar)
            toolbar.menu.findItem(R.id.menu_settings).isVisible = true
            toolbar.menu.findItem(R.id.menu_logout).isVisible = true
        } else {
            requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation_bar)
                .visibility = View.GONE
            val toolbar = requireActivity().findViewById<Toolbar>(R.id.top_toolbar)
            toolbar.menu.findItem(R.id.menu_settings).isVisible = false
            toolbar.menu.findItem(R.id.menu_logout).isVisible = false
        }
    }

    private fun checkQuestionAmount() {
        if (arrayQuestionsAndAnswers.size < 5) {
            binding.textView1QuizPicker.visibility = View.INVISIBLE
            binding.buttonStartSimpleQuizPicker.visibility = View.INVISIBLE
            binding.buttonStartTextQuizPicker.visibility = View.INVISIBLE
            binding.buttonStartTimeQuizPicker.visibility = View.INVISIBLE
            binding.textViewAmountWarningQuizPicker.visibility = View.VISIBLE
        }
    }
}