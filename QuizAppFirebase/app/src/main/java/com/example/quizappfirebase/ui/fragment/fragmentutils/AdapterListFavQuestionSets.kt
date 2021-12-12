package com.example.quizappfirebase.ui.fragment.fragmentutils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.example.quizappfirebase.R
import com.example.quizappfirebase.data.QuestionSet
import com.example.quizappfirebase.databinding.RecyclerViewQuestionSetFavBinding
import com.example.quizappfirebase.ui.fragment.ListFavouriteQuestionSetsFragmentDirections
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AdapterListFavQuestionSets(options: FirestoreRecyclerOptions<QuestionSet>)
    : FirestoreRecyclerAdapter<QuestionSet, AdapterListFavQuestionSets.QuestionSetViewHolder>(options) {
    private val db = Firebase.firestore
    private val currentUser = Firebase.auth.currentUser

    class QuestionSetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = RecyclerViewQuestionSetFavBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionSetViewHolder {
        val objectView = LayoutInflater.from(parent.context).inflate(
            R.layout.recycler_view_question_set_fav,
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

                val textViewQuestionSetName = holder.binding.textViewQuestionSetNameFav
                val imageViewQuestionSetMakeFav = holder.binding.imageViewQuestionSetMakeFavFav
                val textViewQuestionSetAuthorName = holder.binding.textViewQuestionSetAuthorNameFav
                val textViewQuestionSetFavCounter = holder.binding.textViewQuestionSetFavCounterFav

                textViewQuestionSetName.text = model.questionSetName
                textViewQuestionSetFavCounter.text = model.questionSetFavCount.toString()
                textViewQuestionSetAuthorName.text = model.questionSetOwnerName

                holder.binding.cardViewQuestionSetFavRecyclerView.setOnClickListener {
                    val dialog = MaterialDialog(holder.itemView.context)
                        .noAutoDismiss()
                        .customView(R.layout.dialog_action_picker)

                    dialog.findViewById<Button>(R.id.button_play).setOnClickListener {
                        db.collection("questionsAndAnswers")
                            .whereEqualTo("questionAndAnswerParentQuestionSetId", model.questionSetId)
                            .get()
                            .addOnSuccessListener {
                                val arrayQuestions = arrayListOf<String>()
                                val arrayAnswers = arrayListOf<String>()
                                for (questionAndAnswer in it) {
                                    arrayQuestions.add(questionAndAnswer["questionAndAnswerQuestionText"] as String)
                                    arrayAnswers.add(questionAndAnswer["questionAndAnswerAnswerText"] as String)
                                }
                            }
                        dialog.dismiss()
                    }
                    dialog.findViewById<Button>(R.id.button_browse).setOnClickListener {
                        val action = ListFavouriteQuestionSetsFragmentDirections
                            .actionListFavouriteQuestionSetsFragmentToListCategoriesFragment(model.questionSetId!!)
                        holder.itemView.findNavController().navigate(action)

                        dialog.dismiss()
                    }
                    dialog.show()
                }

                imageViewQuestionSetMakeFav.setOnClickListener {
                    val queryQuestionSet = db.collection("questionSets").document(model.questionSetId!!)
                    val queryUser = db.collection("users").document(currentUser.uid)

                    if (model.questionSetId in userFavQuestionSets) {
                        queryQuestionSet.update("questionSetFavCount", model.questionSetFavCount - 1)
                        queryQuestionSet.update("questionSetFavUsersId", FieldValue.arrayRemove(currentUser.uid))
                        queryUser.update("userFavQuestionSet", FieldValue.arrayRemove(model.questionSetId))
                    }
                }
            }
    }


}