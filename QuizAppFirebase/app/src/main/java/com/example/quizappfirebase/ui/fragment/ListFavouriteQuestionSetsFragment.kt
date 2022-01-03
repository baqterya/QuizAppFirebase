package com.example.quizappfirebase.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizappfirebase.data.QuestionSet
import com.example.quizappfirebase.data.User
import com.example.quizappfirebase.databinding.FragmentListFavouriteQuestionSetsBinding
import com.example.quizappfirebase.ui.fragment.fragmentutils.AdapterListFavQuestionSets
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class ListFavouriteQuestionSetsFragment : Fragment() {
    private var _binding: FragmentListFavouriteQuestionSetsBinding? = null
        private val binding get() = _binding!!

    private val db = Firebase.firestore
    private lateinit var currentUser: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListFavouriteQuestionSetsBinding.inflate(inflater, container, false)
        currentUser = requireArguments().getParcelable("currentUser")!!
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.title = "Favourite Question Sets"

        val query = db.collection("questionSets")
            .whereArrayContains("questionSetFavUsersId", currentUser.userId!!)
            .whereEqualTo("questionSetIsPrivate", false)
            .orderBy("questionSetFavCount", Query.Direction.DESCENDING)
            .orderBy("questionSetName")

        val options = FirestoreRecyclerOptions.Builder<QuestionSet>()
            .setQuery(query, QuestionSet::class.java)
            .setLifecycleOwner(requireActivity())
            .build()

        val adapter = AdapterListFavQuestionSets(options)
        binding.listFavouriteQuestionRecyclerView.adapter = adapter
        binding.listFavouriteQuestionRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }
}