package com.example.quizappfirebase.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizappfirebase.data.QuestionAndAnswer
import com.example.quizappfirebase.databinding.FragmentListQuestionsAndAnswersBinding
import com.example.quizappfirebase.ui.fragment.fragmentutils.AdapterListCategories
import com.example.quizappfirebase.ui.fragment.fragmentutils.AdapterListQuestionsAndAnswers
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class ListQuestionsAndAnswersFragment : Fragment() {
    private var _binding: FragmentListQuestionsAndAnswersBinding? = null
        private val binding get() = _binding!!

    private val args by navArgs<ListQuestionsAndAnswersFragmentArgs>()
    private val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListQuestionsAndAnswersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val query = db.collection("questionsAndAnswers")
            .whereEqualTo("questionAndAnswerParentCategoryId", args.currentCategoryId)
            .orderBy("questionAndAnswerQuestionText")

        val options = FirestoreRecyclerOptions.Builder<QuestionAndAnswer>()
            .setQuery(query, QuestionAndAnswer::class.java)
            .setLifecycleOwner(requireActivity())
            .build()

        val adapter = AdapterListQuestionsAndAnswers(options)
        binding.listQuestionAndAnswerRecyclerView.adapter = adapter
        binding.listQuestionAndAnswerRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

}