package com.example.quizappfirebase.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.example.quizappfirebase.R
import com.example.quizappfirebase.data.QuestionAndAnswer
import com.example.quizappfirebase.databinding.FragmentQuizSimpleBinding


class QuizSimpleFragment : Fragment() {
    private var _binding: FragmentQuizSimpleBinding? = null
        private val binding get() = _binding!!

    private lateinit var listButtons: ArrayList<Button>
    private lateinit var listQuestionsAndAnswers: ArrayList<QuestionAndAnswer>
    private lateinit var currentQuestion: QuestionAndAnswer

    private lateinit var pointCounter: TextView

    private val listAnswers = arrayListOf<String>()
    private var answerCounter = 0

    private val args by navArgs<QuizSimpleFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuizSimpleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.title = "Simple Quiz"

        listQuestionsAndAnswers = args.arrayQuestionsAndAnswers.toCollection(ArrayList())

        listQuestionsAndAnswers.forEach {
            listAnswers.add(it.questionAndAnswerAnswerText!!)
        }

        pointCounter = binding.textViewQuizSimplePointCounter
        listButtons = arrayListOf(
            binding.textViewQuizSimpleAnswer1, binding.textViewQuizSimpleAnswer2,
            binding.textViewQuizSimpleAnswer3, binding.textViewQuizSimpleAnswer4
        )

        setupQuestion()

        for (button in listButtons) {
            button.setOnClickListener {
                checkAnswer(button.text.toString())
            }
        }
    }

    private fun setupQuestion() {
        for (button in listButtons) {
            button.text = ""
        }

        val tempArray = arrayListOf<String>()
        tempArray.addAll(listAnswers)

        currentQuestion = listQuestionsAndAnswers.random()
        binding.textViewQuizSimpleQuestion.text = currentQuestion.questionAndAnswerQuestionText
        tempArray.remove(currentQuestion.questionAndAnswerAnswerText)

        listButtons.shuffle()
        listButtons[0].text = currentQuestion.questionAndAnswerAnswerText

        for (i in 1..3) {
            val randomAnswer = tempArray.random()
            tempArray.remove(randomAnswer)
            listButtons[i].text = randomAnswer
        }
    }

    private fun checkAnswer(buttonText: String) {
        if (buttonText == currentQuestion.questionAndAnswerAnswerText) {
            pointCounter.text = (pointCounter.text.toString().toInt() + 1).toString()
        }

        if (++answerCounter >= args.questionAmount) {
            finishQuizSimple()
        } else {
            setupQuestion()
        }
    }

    private fun finishQuizSimple() {
        val dialog = MaterialDialog(requireContext())
            .noAutoDismiss()
            .customView(R.layout.dialog_finish_quiz)

        val message = "You got ${pointCounter.text} points"
        dialog.findViewById<TextView>(R.id.dialogFinishQuizTextView2).text = message
        dialog.findViewById<TextView>(R.id.dialogFinishQuizButtonReturn).setOnClickListener {
            val action = QuizSimpleFragmentDirections.actionQuizSimpleFragmentToListAllQuestionSetsFragment()
            dialog.dismiss()
            findNavController().navigate(action)
        }

        dialog.show()
    }

}