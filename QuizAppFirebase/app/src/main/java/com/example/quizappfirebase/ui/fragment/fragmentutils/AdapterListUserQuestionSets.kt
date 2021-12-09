package com.example.quizappfirebase.ui.fragment.fragmentutils

import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.example.quizappfirebase.R
import com.example.quizappfirebase.data.QuestionSet
import com.example.quizappfirebase.databinding.RecyclerViewQuestionSetUserBinding
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AdapterListUserQuestionSets(options: FirestoreRecyclerOptions<QuestionSet>)
    : FirestoreRecyclerAdapter<QuestionSet, AdapterListUserQuestionSets.QuestionSetViewHolder>(options){
    private val db = Firebase.firestore

    class QuestionSetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = RecyclerViewQuestionSetUserBinding.bind(itemView)
    }

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
        val textViewQuestionSetName = holder.binding.textViewQuestionSetNameUser

        textViewQuestionSetName.text = model.questionSetName

        holder.binding.imageViewQuestionSetEditUser
            .setOnClickListener {
                showEditQuestionSetDialog(holder, model)
            }
    }

    private fun showEditQuestionSetDialog(holder: QuestionSetViewHolder, questionSet: QuestionSet) {
        val context = holder.itemView.context

        val queryQuestionSet =
            db.collection("questionSets").document(questionSet.questionSetId!!)
        val queryAllUsers = db.collection("users")

        val dialog = MaterialDialog(context)
            .noAutoDismiss()
            .customView(R.layout.dialog_edit_question_set)

        val dialogEditText = dialog.findViewById<EditText>(R.id.text_input_edit_text_layout_edit_question_set_dialog)
        val dialogSwitch = dialog.findViewById<SwitchMaterial>(R.id.switch_is_private_edit_question_set_dialog)

        dialogEditText.setText(questionSet.questionSetName)
        dialogSwitch.isChecked = questionSet.questionSetIsPrivate

        dialog.findViewById<Button>(R.id.button_edit_question_set_dialog).setOnClickListener {
            val newQuestionSetName = dialogEditText.text.toString()
            val newIsPrivate = dialogSwitch.isChecked

            if (inputCheck(newQuestionSetName)) {

                queryQuestionSet.update("questionSetName", newQuestionSetName)
                queryQuestionSet.update("questionSetIsPrivate", newIsPrivate)
                dialog.dismiss()
                Toast.makeText(context, "Update successful", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Please enter a name", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.findViewById<AppCompatImageButton>(R.id.image_button_remove_edit_question_set_dialog)
            .setOnClickListener {
                AlertDialog.Builder(context)
                    .setPositiveButton("Yes") {_, _ ->
                        queryAllUsers.get().addOnSuccessListener {
                            it.forEach { user ->
                                user.reference.update(
                                    "userFavQuestionSet",
                                    FieldValue.arrayRemove(questionSet.questionSetId)
                                )
                            }
                        }
                        queryQuestionSet.delete()
                        Toast.makeText(context, "Successfully removed ${questionSet.questionSetName}", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }
                    .setNegativeButton("No") {_, _ ->}
                    .setTitle("Delete ${questionSet.questionSetName}?")
                    .setMessage("Are you sure you want to delete ${questionSet.questionSetName} and all it's content?")
                    .create().show()
            }

        dialog.show()
    }

    private fun inputCheck(name: String): Boolean {
        return !(TextUtils.isEmpty(name))
    }
}