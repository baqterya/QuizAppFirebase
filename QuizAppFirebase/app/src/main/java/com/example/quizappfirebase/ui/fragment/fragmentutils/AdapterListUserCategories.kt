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
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.example.quizappfirebase.R
import com.example.quizappfirebase.data.Category
import com.example.quizappfirebase.databinding.RecyclerViewCategoryUserBinding
import com.example.quizappfirebase.ui.fragment.ListUsersCategoriesFragmentDirections
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AdapterListUserCategories(options: FirestoreRecyclerOptions<Category>)
    : FirestoreRecyclerAdapter<Category, AdapterListUserCategories.CategoryViewHolder>(options) {

    val db = Firebase.firestore

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = RecyclerViewCategoryUserBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val objectView = LayoutInflater.from(parent.context).inflate(
            R.layout.recycler_view_category_user,
            parent,
            false
        )
        return CategoryViewHolder(objectView)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int, model: Category) {
        val textViewCategoryName = holder.binding.textViewCategoryNameUser
        textViewCategoryName.text = model.categoryName

        holder.binding.imageViewCategoryEditUser.setOnClickListener {
            showEditCategoryDialog(holder, model)
        }

        holder.binding.cardViewCategoryRecyclerViewUser.setOnClickListener {
            val action = ListUsersCategoriesFragmentDirections
                .actionListUsersCategoriesFragmentToListUsersQuestionsAndAnswersFragment(
                    model.categoryId!!,
                    model.categoryParentQuestionSetId!!
                )
            holder.itemView.findNavController().navigate(action)
        }
    }

    private fun showEditCategoryDialog(holder: CategoryViewHolder, category: Category) {
        val context = holder.itemView.context

        val queryCategory =
            db.collection("categories").document(category.categoryId!!)
        val queryQuestionsAndAnswers = db.collection("questionsAndAnswers")
            .whereEqualTo("questionAndAnswerParentCategoryId", category.categoryId!!)

        val dialog = MaterialDialog(context)
            .noAutoDismiss()
            .customView(R.layout.dialog_edit_category)

        val dialogEditText = dialog.findViewById<EditText>(R.id.text_input_edit_text_layout_edit_category_dialog)
        dialogEditText.setText(category.categoryName)

        dialog.findViewById<Button>(R.id.button_edit_category_dialog).setOnClickListener {
            val newCategoryName = dialogEditText.text.toString()
            if (inputCheck(newCategoryName)) {
                queryCategory.update("categoryName", newCategoryName)
                dialog.dismiss()
                Toast.makeText(context, "Update successful", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Please enter a name", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.findViewById<AppCompatImageButton>(R.id.image_button_remove_category_dialog)
            .setOnClickListener {
                AlertDialog.Builder(context)
                    .setPositiveButton("Yes") {_, _ ->
                        queryQuestionsAndAnswers.get().addOnSuccessListener {
                            it.forEach { questionAndAnswer ->
                                questionAndAnswer.reference.delete()
                            }
                        }
                        queryCategory.delete()
                        Toast.makeText(context, "Successfully removed ${category.categoryName}", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }
                    .setNegativeButton("No") {_, _ ->}
                    .setTitle("Delete ${category.categoryName}?")
                    .setMessage("Are you sure you want to delete ${category.categoryName} and all it's content?")
                    .create().show()
            }

        dialog.show()
    }

    private fun inputCheck(name: String): Boolean {
        return !(TextUtils.isEmpty(name))
    }
}