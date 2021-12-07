package com.example.quizappfirebase.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizappfirebase.R
import com.example.quizappfirebase.data.QuestionSet
import com.example.quizappfirebase.databinding.FragmentListAllQuestionSetsBinding
import com.example.quizappfirebase.ui.fragment.fragmentutils.AdapterListAllQuestionSets
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ListAllQuestionSetsFragment : Fragment() {
    private var _binding: FragmentListAllQuestionSetsBinding? = null
        private val binding get() = _binding!!

    val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListAllQuestionSetsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val query = db.collection("questionSets").orderBy("questionSetFavCount")

        val options = FirestoreRecyclerOptions.Builder<QuestionSet>()
            .setQuery(query, QuestionSet::class.java)
            .setLifecycleOwner(requireActivity())
            .build()

        val adapter = AdapterListAllQuestionSets(options)
        binding.listAllQuestionRecyclerView.adapter = adapter
        binding.listAllQuestionRecyclerView.layoutManager = LinearLayoutManager(requireContext())

    }
}