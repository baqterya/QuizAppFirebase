package com.example.quizappfirebase.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizappfirebase.data.QuestionSet
import com.example.quizappfirebase.databinding.FragmentListAllQuestionSetsBinding
import com.example.quizappfirebase.ui.fragment.fragmentutils.AdapterListAllQuestionSets
import com.example.quizappfirebase.ui.fragment.fragmentutils.WrapperLinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ListAllQuestionSetsFragment : Fragment() {
    private var _binding: FragmentListAllQuestionSetsBinding? = null
        private val binding get() = _binding!!

    private val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListAllQuestionSetsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.title = "All Question Sets"

        val query = db.collection("questionSets")
            .whereEqualTo("questionSetIsPrivate", false)
            .orderBy("questionSetFavCount", Query.Direction.DESCENDING)
            .orderBy("questionSetName")

        val options = FirestoreRecyclerOptions.Builder<QuestionSet>()
            .setQuery(query, QuestionSet::class.java)
            .setLifecycleOwner(requireActivity())
            .build()

        val adapter = AdapterListAllQuestionSets(options)
        binding.listAllQuestionRecyclerView.adapter = adapter
        binding.listAllQuestionRecyclerView.layoutManager = WrapperLinearLayoutManager(requireContext())

    }
}