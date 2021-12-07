package com.example.quizappfirebase.ui.fragment.fragmentutils

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

class AdapterListAllQuestionSets(options: FirestoreRecyclerOptions<QuestionSet>)
    : FirestoreRecyclerAdapter<QuestionSet, AdapterListAllQuestionSets.QuestionSetViewHolder>(options) {
    private val db = Firebase.firestore

    class QuestionSetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionSetViewHolder {
        val objectView = LayoutInflater.from(parent.context).inflate(
            R.layout.recycler_view_question_set_all,
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
            .findViewById(R.id.text_view_question_set_name_all)
        val imageViewQuestionSetMakeFav: ImageView = holder.itemView
            .findViewById(R.id.image_view_question_set_make_fav_all)
        val textViewQuestionSetAuthorName: TextView = holder.itemView
            .findViewById(R.id.text_view_question_set_author_name_all)
        val textViewQuestionSetFavCounter: TextView = holder.itemView
            .findViewById(R.id.text_view_question_set_fav_counter_all)

        textViewQuestionSetName.text = model.questionSetName
        textViewQuestionSetFavCounter.text = model.questionSetFavCount.toString()
        textViewQuestionSetAuthorName.text = model.questionSetOwnerName

        if (model.questionSetIsFav)
            imageViewQuestionSetMakeFav.setImageResource(R.drawable.ic_star_full_white)

        imageViewQuestionSetMakeFav.setOnClickListener {
            val query = db.collection("questionSets").document("questionSetId")
            when (model.questionSetIsFav){
                true -> {
                    imageViewQuestionSetMakeFav.setImageResource(R.drawable.ic_star_full_white)
                    query.update(mapOf(
                        "questionSetIsFav" to false,
                        "questionSetFavCount" to model.questionSetFavCount - 1
                    ))
                }
                false -> {
                    imageViewQuestionSetMakeFav.setImageResource(R.drawable.ic_star_border_white)
                    query.update(mapOf(
                        "questionSetIsFav" to true,
                        "questionSetFavCount" to model.questionSetFavCount + 1
                    ))
                }
            }
        }

    }

}