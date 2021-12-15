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
import com.example.quizappfirebase.data.Category
import com.example.quizappfirebase.data.QuestionAndAnswer
import com.example.quizappfirebase.databinding.RecyclerViewCategoryBinding
import com.example.quizappfirebase.ui.fragment.ListAllQuestionSetsFragmentDirections
import com.example.quizappfirebase.ui.fragment.ListCategoriesFragment
import com.example.quizappfirebase.ui.fragment.ListCategoriesFragmentDirections
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AdapterListCategories(options: FirestoreRecyclerOptions<Category>)
    : FirestoreRecyclerAdapter<Category, AdapterListCategories.CategoryViewHolder>(options) {
    val db = Firebase.firestore

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = RecyclerViewCategoryBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val objectView = LayoutInflater.from(parent.context).inflate(
            R.layout.recycler_view_category,
            parent,
            false
        )
        return CategoryViewHolder(objectView)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int, model: Category) {
        val textViewCategoryName = holder.binding.textViewCategoryName
        textViewCategoryName.text = model.categoryName

        holder.binding.cardViewCategoryRecyclerView.setOnClickListener {
            val dialog = MaterialDialog(holder.itemView.context)
                .noAutoDismiss()
                .customView(R.layout.dialog_action_picker)

            dialog.findViewById<Button>(R.id.button_play).setOnClickListener {
                db.collection("questionsAndAnswers")
                    .whereEqualTo("questionAndAnswerParentCategoryId", model.categoryId)
                    .get()
                    .addOnSuccessListener {
                        val arrayQuestionsAndAnswers = arrayListOf<QuestionAndAnswer>()
                        for (questionAndAnswer in it) {
                            arrayQuestionsAndAnswers.add(
                                    questionAndAnswer.toObject(QuestionAndAnswer::class.java)
                            )
                        }
                        val action = ListCategoriesFragmentDirections
                                .actionListCategoriesFragmentToQuizModePickerFragment(
                                        arrayQuestionsAndAnswers.toTypedArray()
                                )
                        holder.itemView.findNavController().navigate(action)
                    }
                dialog.dismiss()
            }
            dialog.findViewById<Button>(R.id.button_browse).setOnClickListener {
                val action = ListCategoriesFragmentDirections
                    .actionListCategoriesFragmentToListQuestionsAndAnswersFragment(
                        model.categoryId!!, model.categoryParentQuestionSetId!!
                    )
                holder.itemView.findNavController().navigate(action)

                dialog.dismiss()
            }
            dialog.show()

        }
    }
}
