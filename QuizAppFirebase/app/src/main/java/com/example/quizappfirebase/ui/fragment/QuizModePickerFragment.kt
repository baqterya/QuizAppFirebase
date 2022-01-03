package com.example.quizappfirebase.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.NumberPicker
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.example.quizappfirebase.R
import com.example.quizappfirebase.databinding.FragmentQuizModePickerBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class QuizModePickerFragment : Fragment() {
    private var _binding: FragmentQuizModePickerBinding? = null
        private val binding get() = _binding!!

    private val args by navArgs<QuizModePickerFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuizModePickerBinding.inflate(inflater, container, false)
        checkQuestionAmount()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        menuVisibility(false)

        (activity as AppCompatActivity).supportActionBar?.title = "Quiz Mode Picker"

        binding.buttonStartSimpleQuizPicker.setOnClickListener {
            showQuestionAmountPickerDialog("simple")
        }

        binding.buttonStartTimeQuizPicker.setOnClickListener {
            val action = QuizModePickerFragmentDirections.actionQuizModePickerFragmentToQuizTimeFragment(args.arrayQuestionsAndAnswers)
            findNavController().navigate(action)
        }

        binding.buttonStartTextQuizPicker.setOnClickListener {
            showQuestionAmountPickerDialog("text")
        }
    }

    override fun onPause() {
        super.onPause()
        menuVisibility(true)
    }

    private fun showQuestionAmountPickerDialog(mode: String) {
        val dialog = MaterialDialog(requireContext())
            .noAutoDismiss()
            .customView(R.layout.dialog_quiz_question_amount)

        val numberPicker = dialog.findViewById<NumberPicker>(R.id.dialogSimpleQuizQuestionsNumberPicker)
        numberPicker.minValue = 6
        numberPicker.maxValue = 20
        var questionAmount = 6

        numberPicker.setOnValueChangedListener { _, _, newVal ->
            questionAmount = newVal
        }

        dialog.findViewById<Button>(R.id.dialogSimpleQuizQuestionsButton).setOnClickListener {
            val action = if (mode == "simple") QuizModePickerFragmentDirections.actionQuizModePickerFragmentToQuizSimpleFragment(args.arrayQuestionsAndAnswers, questionAmount)
                else QuizModePickerFragmentDirections.actionQuizModePickerFragmentToQuizTextFragment(args.arrayQuestionsAndAnswers, questionAmount)

            findNavController().navigate(action)
            dialog.dismiss()
        }
        dialog.show()
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
        if (args.arrayQuestionsAndAnswers.size < 5) {
            binding.textView1QuizPicker.visibility = View.INVISIBLE
            binding.buttonStartSimpleQuizPicker.visibility = View.INVISIBLE
            binding.buttonStartTextQuizPicker.visibility = View.INVISIBLE
            binding.buttonStartTimeQuizPicker.visibility = View.INVISIBLE
            binding.textViewAmountWarningQuizPicker.visibility = View.VISIBLE
        }
    }
}