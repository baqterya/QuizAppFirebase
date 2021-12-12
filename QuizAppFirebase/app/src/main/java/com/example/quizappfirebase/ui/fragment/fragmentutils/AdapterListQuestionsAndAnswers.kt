package com.example.quizappfirebase.ui.fragment.fragmentutils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.quizappfirebase.R
import com.example.quizappfirebase.data.QuestionAndAnswer
import com.example.quizappfirebase.databinding.RecyclerViewQuestionAndAnswerBinding
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class AdapterListQuestionsAndAnswers(options: FirestoreRecyclerOptions<QuestionAndAnswer>)
    : FirestoreRecyclerAdapter<QuestionAndAnswer, AdapterListQuestionsAndAnswers.QuestionAndAnswerViewHolder>(options){

    class QuestionAndAnswerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val binding = RecyclerViewQuestionAndAnswerBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionAndAnswerViewHolder {
        val objectView = LayoutInflater.from(parent.context).inflate(
            R.layout.recycler_view_question_and_answer,
            parent,
            false
        )
        return QuestionAndAnswerViewHolder(objectView)
    }

    override fun onBindViewHolder(
        holder: QuestionAndAnswerViewHolder,
        position: Int,
        model: QuestionAndAnswer
    ) {
        holder.binding.textViewQuestionAndAnswerQuestionText.text = model.questionAndAnswerQuestionText
        holder.binding.textViewQuestionAndAnswerAnswerText.text = model.questionAndAnswerAnswerText
    }
}