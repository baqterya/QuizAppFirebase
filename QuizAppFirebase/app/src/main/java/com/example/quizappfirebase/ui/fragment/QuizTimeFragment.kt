package com.example.quizappfirebase.ui.fragment

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.example.quizappfirebase.R
import com.example.quizappfirebase.data.QuestionAndAnswer
import com.example.quizappfirebase.databinding.FragmentQuizTimeBinding


class QuizTimeFragment : Fragment() {
    private var _binding: FragmentQuizTimeBinding? = null
        private val binding get() = _binding!!

    private lateinit var listButtons: ArrayList<Button>
    private lateinit var listQuestionsAndAnswers: ArrayList<QuestionAndAnswer>
    private lateinit var currentQuestion: QuestionAndAnswer

    private lateinit var pointCounter: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var timer: CountDownTimer

    private val listAnswers = arrayListOf<String>()
    private val args by navArgs<QuizTimeFragmentArgs>()

    private var timeLeft: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuizTimeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar = binding.progressBarQuizTime
        progressBar.progress = 0

        timer = MyTimer(10000, 100)
        timer.start()

        listQuestionsAndAnswers = args.arrayQuestionsAndAnswers.toCollection(ArrayList())
        listQuestionsAndAnswers.forEach {
            listAnswers.add(it.questionAndAnswerAnswerText!!)
        }

        pointCounter = binding.textViewQuizTimePointCounter
        listButtons = arrayListOf(
            binding.textViewQuizTimeAnswer1, binding.textViewQuizTimeAnswer2,
            binding.textViewQuizTimeAnswer3, binding.textViewQuizTimeAnswer4
        )

        setupQuestion()

        listButtons.forEach { button ->
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
        listAnswers.forEach {
            tempArray.add(it)
        }

        currentQuestion = listQuestionsAndAnswers.random()
        binding.textViewQuizTimeQuestion.text = currentQuestion.questionAndAnswerQuestionText
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
            timer.cancel()
            timer = MyTimer(timeLeft + 800, 100)
            timer.start()
        }
        setupQuestion()
    }

    private fun finishQuizTime() {
        val dialog = MaterialDialog(requireContext())
            .noAutoDismiss()
            .customView(R.layout.dialog_finish_quiz)

        val message = "You got ${binding.textViewQuizTimePointCounter.text} points"
        dialog.findViewById<TextView>(R.id.dialogFinishQuizTextView2).text = message
        dialog.findViewById<Button>(R.id.dialogFinishQuizButtonReturn).setOnClickListener {
            val action = QuizTimeFragmentDirections.actionQuizTimeFragmentToListAllQuestionSetsFragment()
            dialog.dismiss()
            findNavController().navigate(action)
        }

        dialog.show()
    }

    override fun onPause() {
        super.onPause()
        timer.cancel()
    }

    inner class MyTimer(time: Long, interval: Long) : CountDownTimer(time, interval) {
        override fun onTick(millisUntilFinished: Long) {
            val progress = (millisUntilFinished/100).toInt()
            timeLeft = millisUntilFinished
            progressBar.progress = progress
        }

        override fun onFinish() {
            finishQuizTime()
        }

    }
}