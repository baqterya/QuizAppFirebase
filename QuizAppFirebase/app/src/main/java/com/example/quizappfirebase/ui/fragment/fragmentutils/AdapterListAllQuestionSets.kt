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
import com.example.quizappfirebase.data.QuestionAndAnswer
import com.example.quizappfirebase.data.QuestionSet
import com.example.quizappfirebase.databinding.RecyclerViewQuestionSetAllBinding
import com.example.quizappfirebase.ui.fragment.ListAllQuestionSetsFragmentDirections
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class AdapterListAllQuestionSets(options: FirestoreRecyclerOptions<QuestionSet>)
    : FirestoreRecyclerAdapter<QuestionSet, AdapterListAllQuestionSets.QuestionSetViewHolder>(options) {
    private val db = Firebase.firestore
    private val currentUser = Firebase.auth.currentUser!!

    class QuestionSetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = RecyclerViewQuestionSetAllBinding.bind(itemView)
    }

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

            val textViewQuestionSetName = holder.binding.textViewQuestionSetNameAll
            val imageViewQuestionSetMakeFav = holder.binding.imageViewQuestionSetMakeFavAll
            val textViewQuestionSetAuthorName = holder.binding.textViewQuestionSetAuthorNameAll
            val textViewQuestionSetFavCounter = holder.binding.textViewQuestionSetFavCounterAll

            textViewQuestionSetName.text = model.questionSetName
            textViewQuestionSetFavCounter.text = model.questionSetFavCount.toString()
            textViewQuestionSetAuthorName.text = model.questionSetOwnerName

            if (currentUser.uid in model.questionSetFavUsersId)
                imageViewQuestionSetMakeFav.setImageResource(R.drawable.ic_star_full_white)

            holder.binding.cardViewQuestionSetAllRecyclerView.setOnClickListener {
                val dialog = MaterialDialog(holder.itemView.context)
                    .noAutoDismiss()
                    .customView(R.layout.dialog_action_picker)

                dialog.findViewById<Button>(R.id.button_play).setOnClickListener {
                    db.collection("questionsAndAnswers")
                        .whereEqualTo("questionAndAnswerParentQuestionSetId", model.questionSetId)
                        .get()
                        .addOnSuccessListener {
                            val arrayQuestionsAndAnswers = arrayListOf<QuestionAndAnswer>()
                            for (questionAndAnswer in it) {
                                arrayQuestionsAndAnswers.add(
                                        questionAndAnswer.toObject(QuestionAndAnswer::class.java)
                                )
                            }
                            val action = ListAllQuestionSetsFragmentDirections
                                .actionListAllQuestionSetsFragmentToQuizModePickerFragment(
                                        arrayQuestionsAndAnswers.toTypedArray()
                                )
                            holder.itemView.findNavController().navigate(action)
                        }
                    dialog.dismiss()
                }
                dialog.findViewById<Button>(R.id.button_browse).setOnClickListener {
                    val action = ListAllQuestionSetsFragmentDirections
                        .actionListAllQuestionSetsFragmentToListCategoriesFragment(model.questionSetId!!, model.questionSetName!!)
                    holder.itemView.findNavController().navigate(action)
                    dialog.dismiss()
                }
                dialog.show()
            }

            imageViewQuestionSetMakeFav.setOnClickListener {
                val queryQuestionSet = db.collection("questionSets").document(model.questionSetId!!)
                val queryUser = db.collection("users").document(currentUser.uid)

                when (currentUser.uid in model.questionSetFavUsersId) {
                    true -> {
                        imageViewQuestionSetMakeFav.setImageResource(R.drawable.ic_star_border_white)
                        queryQuestionSet.update("questionSetFavCount", model.questionSetFavCount - 1)
                        queryQuestionSet.update("questionSetFavUsersId", FieldValue.arrayRemove(currentUser.uid))
                        queryUser.update("userFavQuestionSet", FieldValue.arrayRemove(model.questionSetId))
                    }
                    false -> {
                        imageViewQuestionSetMakeFav.setImageResource(R.drawable.ic_star_full_white)
                        queryQuestionSet.update("questionSetFavCount", model.questionSetFavCount + 1)
                        queryQuestionSet.update("questionSetFavUsersId", FieldValue.arrayUnion(currentUser.uid))
                        queryUser.update("userFavQuestionSet", FieldValue.arrayUnion(model.questionSetId))
                    }
                }
            }
    }

}