package com.example.quizappfirebase.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.example.quizappfirebase.R
import com.example.quizappfirebase.data.QuestionAndAnswer
import com.example.quizappfirebase.databinding.FragmentQuizTextBinding


class QuizTextFragment : Fragment() {
    private var _binding: FragmentQuizTextBinding? = null
        private val binding get() = _binding!!

    private lateinit var listQuestionsAndAnswers: ArrayList<QuestionAndAnswer>
    private lateinit var currentQuestion: QuestionAndAnswer
    private lateinit var pointCounter: TextView

    private var answerCounter = 0

    private val args by navArgs<QuizTextFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuizTextBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.title = "Writing Challenge"

        pointCounter = binding.textViewQuizTextPointCounter
        listQuestionsAndAnswers = args.arrayQuestionsAndAnswers.toCollection(ArrayList())

        setupQuestion()

        binding.buttonQuizTextCheck.setOnClickListener {
            checkAnswer()
        }
    }

    private fun setupQuestion() {
        currentQuestion = listQuestionsAndAnswers.random()
        binding.textViewQuizTextQuestion.text = currentQuestion.questionAndAnswerQuestionText
        binding.editTextQuizTextAnswer.text.clear()
    }

    private fun checkAnswer() {
        if (binding.editTextQuizTextAnswer.text.toString().equals(currentQuestion.questionAndAnswerAnswerText, ignoreCase = true)) {
            pointCounter.text = (pointCounter.text.toString().toInt() + 1).toString()
        } else {
            Toast.makeText(requireContext(), "Wrong answer!", Toast.LENGTH_SHORT).show()
        }

        if (++answerCounter >= args.questionAmount) {
            finishQuizWriting()
        }
        else {

            setupQuestion()
        }
    }

    private fun finishQuizWriting() {
        val dialog = MaterialDialog(requireContext())
            .noAutoDismiss()
            .customView(R.layout.dialog_finish_quiz)

        val string = "You got ${pointCounter.text} points"
        dialog.findViewById<TextView>(R.id.dialogFinishQuizTextView2).text = string
        dialog.findViewById<Button>(R.id.dialogFinishQuizButtonReturn).setOnClickListener {
            val action = QuizTextFragmentDirections.actionQuizTextFragmentToListAllQuestionSetsFragment()
            dialog.dismiss()
            findNavController().navigate(action)
        }

        dialog.show()
    }
}