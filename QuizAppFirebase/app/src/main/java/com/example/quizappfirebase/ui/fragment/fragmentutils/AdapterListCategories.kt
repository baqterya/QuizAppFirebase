package com.example.quizappfirebase.ui.fragment.fragmentutils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.quizappfirebase.R
import com.example.quizappfirebase.data.Category
import com.example.quizappfirebase.databinding.RecyclerViewCategoryBinding
import com.example.quizappfirebase.ui.fragment.ListCategoriesFragment
import com.example.quizappfirebase.ui.fragment.ListCategoriesFragmentDirections
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class AdapterListCategories(options: FirestoreRecyclerOptions<Category>)
    : FirestoreRecyclerAdapter<Category, AdapterListCategories.CategoryViewHolder>(options) {

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
            val action = ListCategoriesFragmentDirections
                .actionListCategoriesFragmentToListQuestionsAndAnswersFragment(
                    model.categoryId!!, model.categoryParentQuestionSetId!!
                )
            holder.itemView.findNavController().navigate(action)
        }
    }
}
