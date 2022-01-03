package com.example.quizappfirebase.ui.fragment

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.example.quizappfirebase.R
import com.example.quizappfirebase.data.Category
import com.example.quizappfirebase.databinding.FragmentListCategoriesBinding
import com.example.quizappfirebase.ui.fragment.fragmentutils.AdapterListCategories
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class ListCategoriesFragment : Fragment() {
    private var _binding: FragmentListCategoriesBinding? = null
        private val binding get() = _binding!!

    private val args by navArgs<ListCategoriesFragmentArgs>()

    private val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListCategoriesBinding.inflate(inflater, container, false)
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

        val adapter = AdapterListCategories(options)
        binding.listCategoryRecyclerView.adapter = adapter
        binding.listCategoryRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }
}