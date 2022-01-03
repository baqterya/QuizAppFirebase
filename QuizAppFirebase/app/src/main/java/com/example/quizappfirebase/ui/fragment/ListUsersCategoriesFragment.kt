package com.example.quizappfirebase.ui.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.example.quizappfirebase.R
import com.example.quizappfirebase.data.Category
import com.example.quizappfirebase.databinding.FragmentListUsersCategoriesBinding
import com.example.quizappfirebase.ui.fragment.fragmentutils.AdapterListUserCategories
import com.example.quizappfirebase.ui.fragment.fragmentutils.WrapperLinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ListUsersCategoriesFragment : Fragment() {
    private var _binding: FragmentListUsersCategoriesBinding? = null
            private val binding get() = _binding!!

    private val args by navArgs<ListUsersCategoriesFragmentArgs>()

    private val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListUsersCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.title = args.currentQuestionSetName

        val query = db.collection("categories")
            .whereEqualTo("categoryParentQuestionSetId", args.currentQuestionSetId)
            .orderBy("categoryName")

        val options = FirestoreRecyclerOptions.Builder<Category>()
            .setQuery(query, Category::class.java)
            .setLifecycleOwner(requireActivity())
            .build()

        val adapter = AdapterListUserCategories(options)
        binding.listCategoryRecyclerViewUser.adapter = adapter
        binding.listCategoryRecyclerViewUser.layoutManager = WrapperLinearLayoutManager(requireContext())

        binding.fabUsersCategoryRecyclerView.setOnClickListener {
            showAddCategoryDialog()
        }
    }

    private fun showAddCategoryDialog() {
        val dialog = MaterialDialog(requireContext())
            .noAutoDismiss()
            .customView(R.layout.dialog_add_category)

        dialog.findViewById<Button>(R.id.button_add_category_dialog).setOnClickListener {
            val categoryName = dialog.findViewById<EditText>(R.id.text_input_edit_text_layout_add_category_dialog)
                .text.toString()

            if (inputCheck(categoryName)) {
                val category = Category(
                    categoryName = categoryName,
                    categoryParentQuestionSetId = args.currentQuestionSetId
                )
                addCategoryToFirestore(category)
                Toast.makeText(requireContext(), "Question Set added!", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            } else {
                Toast.makeText(requireContext(), "Please enter the name", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }

    private fun addCategoryToFirestore(category: Category) {
        db.collection("categories")
            .add(category)
            .addOnSuccessListener {
                db.collection("categories").document(it.id)
                    .update("categoryId", it.id)
            }
    }

    private fun inputCheck(name: String): Boolean {
        return !(TextUtils.isEmpty(name))
    }
}