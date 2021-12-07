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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AdapterListAllQuestionSets(options: FirestoreRecyclerOptions<QuestionSet>)
    : FirestoreRecyclerAdapter<QuestionSet, AdapterListAllQuestionSets.QuestionSetViewHolder>(options) {
    private val db = Firebase.firestore
    private val currentUser = Firebase.auth.currentUser

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
        db.collection("users").document(currentUser!!.uid)
            .get()
            .addOnSuccessListener { user ->
                val userFavQuestionSets = user["userFavQuestionSet"] as ArrayList<*>

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

                if (model.questionSetId in userFavQuestionSets)
                    imageViewQuestionSetMakeFav.setImageResource(R.drawable.ic_star_full_white)

                imageViewQuestionSetMakeFav.setOnClickListener {
                    val queryQuestionSet = db.collection("questionSets").document(model.questionSetId!!)
                    val queryUser = db.collection("users").document(currentUser.uid)

                        when (model.questionSetId in userFavQuestionSets) {
                            true -> {
                                imageViewQuestionSetMakeFav.setImageResource(R.drawable.ic_star_full_white)
                                queryQuestionSet.update("questionSetFavCount", model.questionSetFavCount - 1)
                                queryUser.update("userFavQuestionSet", FieldValue.arrayRemove(model.questionSetId))
                            }
                            false -> {
                                imageViewQuestionSetMakeFav.setImageResource(R.drawable.ic_star_border_white)
                                queryQuestionSet.update("questionSetFavCount", model.questionSetFavCount + 1)
                                queryUser.update("userFavQuestionSet", FieldValue.arrayUnion(model.questionSetId))
                            }
                        }
                }
            }
    }

}