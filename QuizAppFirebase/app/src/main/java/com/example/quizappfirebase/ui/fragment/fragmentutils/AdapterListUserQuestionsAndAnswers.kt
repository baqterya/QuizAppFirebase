package com.example.quizappfirebase.ui.fragment.fragmentutils

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.example.quizappfirebase.R
import com.example.quizappfirebase.data.QuestionAndAnswer
import com.example.quizappfirebase.databinding.RecyclerViewQuestionAndAnswerUserBinding
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AdapterListUserQuestionsAndAnswers(options: FirestoreRecyclerOptions<QuestionAndAnswer>)
    : FirestoreRecyclerAdapter<QuestionAndAnswer, AdapterListUserQuestionsAndAnswers.QuestionAndAnswerViewHolder>(options) {

    val db = Firebase.firestore

    class QuestionAndAnswerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val binding = RecyclerViewQuestionAndAnswerUserBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionAndAnswerViewHolder {
        val objectView = LayoutInflater.from(parent.context).inflate(
            R.layout.recycler_view_question_and_answer_user,
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
        holder.binding.textViewQuestionAndAnswerQuestionTextUser.text =
            model.questionAndAnswerQuestionText
        holder.binding.textViewQuestionAndAnswerAnswerTextUser.text =
            model.questionAndAnswerAnswerText

        holder.binding.imageViewQuestionAndAnswerEditUser.setOnClickListener {
            showQuestionAndAnswerEditDialog(holder, model)
        }
    }

    private fun showQuestionAndAnswerEditDialog(
        holder: QuestionAndAnswerViewHolder,
        questionAndAnswer: QuestionAndAnswer
    ) {
        val context = holder.itemView.context

        val queryQuestionsAndAnswers = db.collection("questionsAndAnswers")
            .document(questionAndAnswer.questionAndAnswerId!!)

        val dialog = MaterialDialog(context)
            .noAutoDismiss()
            .customView(R.layout.dialog_edit_question_and_answer)

        val dialogQuestionEditText = dialog.findViewById<EditText>(R.id.text_input_edit_text_layout_edit_question_dialog)
        val dialogAnswerEditText = dialog.findViewById<EditText>(R.id.text_input_edit_text_layout_edit_answer_dialog)
        dialogQuestionEditText.setText(questionAndAnswer.questionAndAnswerQuestionText)
        dialogAnswerEditText.setText(questionAndAnswer.questionAndAnswerAnswerText)

        dialog.findViewById<Button>(R.id.button_edit_question_and_answer_dialog).setOnClickListener {
            val newQuestionText = dialogQuestionEditText.text.toString()
            val newAnswerText = dialogAnswerEditText.text.toString()
            if (inputCheck(newAnswerText) && inputCheck(newQuestionText)) {
                queryQuestionsAndAnswers.update(mutableMapOf<String, Any>(
                    "questionAndAnswerQuestionText" to newQuestionText,
                    "questionAndAnswerAnswerText" to newAnswerText
                ))
                dialog.dismiss()
                Toast.makeText(context, "Update successful", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Please enter a name", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.findViewById<AppCompatImageButton>(R.id.image_button_remove_question_and_answer_dialog)
            .setOnClickListener {
                AlertDialog.Builder(context)
                    .setPositiveButton("Yes") {_, _ ->
                        queryQuestionsAndAnswers.delete()
                        Toast.makeText(context, "Successfully removed ${questionAndAnswer.questionAndAnswerQuestionText}", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }
                    .setNegativeButton("No") {_, _ ->}
                    .setTitle("Delete ${questionAndAnswer.questionAndAnswerQuestionText}?")
                    .setMessage("Are you sure you want to delete ${questionAndAnswer.questionAndAnswerQuestionText} and all it's content?")
                    .create().show()
            }

        dialog.show()
    }

    private fun inputCheck(name: String): Boolean {
        return !(TextUtils.isEmpty(name))
    }
}