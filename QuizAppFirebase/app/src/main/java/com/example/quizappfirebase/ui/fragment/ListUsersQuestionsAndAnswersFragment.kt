package com.example.quizappfirebase.ui.fragment

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.example.quizappfirebase.R
import com.example.quizappfirebase.data.QuestionAndAnswer
import com.example.quizappfirebase.databinding.FragmentListUsersQuestionsAndAnswersBinding
import com.example.quizappfirebase.ui.fragment.fragmentutils.AdapterListUserQuestionsAndAnswers
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class ListUsersQuestionsAndAnswersFragment : Fragment() {
    private var _binding: FragmentListUsersQuestionsAndAnswersBinding? =  null
        private val binding get() = _binding!!

    private val args by navArgs<ListUsersQuestionsAndAnswersFragmentArgs>()
    private val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListUsersQuestionsAndAnswersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.title = args.currentCategoryName

        val query = db.collection("questionsAndAnswers")
            .whereEqualTo("questionAndAnswerParentCategoryId", args.currentCategoryId)
            .orderBy("questionAndAnswerQuestionText")

        val options = FirestoreRecyclerOptions.Builder<QuestionAndAnswer>()
            .setQuery(query, QuestionAndAnswer::class.java)
            .setLifecycleOwner(requireActivity())
            .build()

        val adapter = AdapterListUserQuestionsAndAnswers(options)
        binding.listQuestionAndAnswerRecyclerViewUser.adapter = adapter
        binding.listQuestionAndAnswerRecyclerViewUser.layoutManager = LinearLayoutManager(requireContext())

        binding.fabUsersQuestionAndAnswerRecyclerView.setOnClickListener {
            showAddQuestionAndAnswerDialog()
        }
    }

    private fun showAddQuestionAndAnswerDialog() {
        val dialog = MaterialDialog(requireContext())
            .noAutoDismiss()
            .customView(R.layout.dialog_add_question_and_answer)

        dialog.findViewById<Button>(R.id.button_add_question_and_answer_dialog).setOnClickListener {
            val newQuestionText = dialog.findViewById<EditText>(R.id.text_input_edit_text_layout_add_question_dialog)
                .text.toString()
            val newAnswerText = dialog.findViewById<EditText>(R.id.text_input_edit_text_layout_add_answer_dialog)
                .text.toString()

            if (inputCheck(newQuestionText) && inputCheck(newAnswerText)) {
                val questionAndAnswer = QuestionAndAnswer(
                    questionAndAnswerQuestionText = newQuestionText,
                    questionAndAnswerAnswerText = newAnswerText,
                    questionAndAnswerParentQuestionSetId = args.currentQuestionSetId,
                    questionAndAnswerParentCategoryId = args.currentCategoryId
                )
                addQuestionAndAnswerToFirestore(questionAndAnswer)
                Toast.makeText(requireContext(), "Question and Answer added!", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            } else {
                Toast.makeText(requireContext(), "Please enter the names", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }

    private fun addQuestionAndAnswerToFirestore(questionAndAnswer: QuestionAndAnswer) {
        db.collection("questionsAndAnswers")
            .add(questionAndAnswer)
            .addOnSuccessListener {
                db.collection("questionsAndAnswers").document(it.id)
                    .update("questionAndAnswerId", it.id)
            }
    }

    private fun inputCheck(name: String): Boolean {
        return !(TextUtils.isEmpty(name))
    }
}