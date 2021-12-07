package com.example.quizappfirebase.ui.fragment.fragmentutils

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.quizappfirebase.R
import com.example.quizappfirebase.data.QuestionSet
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AdapterListUserQuestionSets(options: FirestoreRecyclerOptions<QuestionSet>)
    : FirestoreRecyclerAdapter<QuestionSet, AdapterListUserQuestionSets.QuestionSetViewHolder>(options){
    private val db = Firebase.firestore

    class QuestionSetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionSetViewHolder {
        val objectView = LayoutInflater.from(parent.context).inflate(
            R.layout.recycler_view_question_set_user,
            parent,
            false
        )
        return QuestionSetViewHolder(objectView)
    }

    override fun onBindViewHolder(
        holder: QuestionSetViewHolder,
        position: Int,
        model: QuestionSet
    ) {
        val textViewQuestionSetName: TextView = holder.itemView
            .findViewById(R.id.text_view_question_set_name_user)

        textViewQuestionSetName.text = model.questionSetName

        holder.itemView.findViewById<ImageView>(R.id.image_view_question_set_edit_user)
            .setOnClickListener {
                Log.d("TAG123", "onBindViewHolder: edit clicked")
            }
    }

}