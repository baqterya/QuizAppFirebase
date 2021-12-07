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
import com.example.quizappfirebase.data.User
import com.example.quizappfirebase.databinding.FragmentListUsersQuestionSetsBinding
import com.example.quizappfirebase.ui.fragment.fragmentutils.AdapterListUserQuestionSets
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ListUsersQuestionSetsFragment : Fragment() {
    private var _binding: FragmentListUsersQuestionSetsBinding? = null
        private val binding get() = _binding!!

    private val db = Firebase.firestore
    lateinit var currentUser: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListUsersQuestionSetsBinding.inflate(inflater, container, false)
        val extras = requireActivity().intent.extras
        currentUser = extras!!["currentUser"] as User
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userId = currentUser.userId

        val query = db.collection("questionSets")
            .whereEqualTo("questionSetOwnerId", userId)
            .orderBy("questionSetFavCount", Query.Direction.DESCENDING)


        val options = FirestoreRecyclerOptions.Builder<QuestionSet>()
            .setQuery(query, QuestionSet::class.java)
            .setLifecycleOwner(requireActivity())
            .build()

        val adapter = AdapterListUserQuestionSets(options)
        binding.listUsersQuestionSetsRecyclerView.adapter = adapter
        binding.listUsersQuestionSetsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        binding.fabUsersQuestionSetsRecyclerView.setOnClickListener {
            val questionSet = QuestionSet(
                questionSetName = "Spanish",
                questionSetOwnerId = userId,
                questionSetOwnerName = currentUser.userName,
                questionSetIsPrivate = true
            )
            addQuestionSetToFirestore(questionSet)
        }
    }

    private fun addQuestionSetToFirestore(questionSet: QuestionSet) {
        db.collection("questionSets")
            .add(questionSet)
            .addOnSuccessListener {
                db.collection("questionSets").document(it.id)
                    .update("questionSetId", it.id)
            }
    }
}