package com.example.quizappfirebase.ui.fragment

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.example.quizappfirebase.R
import com.example.quizappfirebase.data.QuestionSet
import com.example.quizappfirebase.data.User
import com.example.quizappfirebase.databinding.FragmentListUsersQuestionSetsBinding
import com.example.quizappfirebase.ui.fragment.fragmentutils.AdapterListUserQuestionSets
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
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
        val userId = currentUser.userId!!

        val query = db.collection("questionSets")
            .whereEqualTo("questionSetOwnerId", userId)
            .orderBy("questionSetName")


        val options = FirestoreRecyclerOptions.Builder<QuestionSet>()
            .setQuery(query, QuestionSet::class.java)
            .setLifecycleOwner(requireActivity())
            .build()

        val adapter = AdapterListUserQuestionSets(options)
        binding.listUsersQuestionSetsRecyclerView.adapter = adapter
        binding.listUsersQuestionSetsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        binding.fabUsersQuestionSetsRecyclerView.setOnClickListener {
            showAddQuestionSetDialog()
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

    private fun showAddQuestionSetDialog() {
        val dialog = MaterialDialog(requireContext())
            .noAutoDismiss()
            .customView(R.layout.dialog_add_question_set)

        dialog.findViewById<Button>(R.id.button_add_question_set_dialog).setOnClickListener {
            val questionSetName = dialog.findViewById<EditText>(R.id.text_input_edit_text_layout_add_question_set_dialog)
                .text.toString()
            val isPrivate = dialog.findViewById<SwitchMaterial>(R.id.switch_is_private_add_question_set_dialog)
                .isChecked

            if (inputCheck(questionSetName)) {
                val questionSet = QuestionSet(
                    questionSetName = questionSetName,
                    questionSetIsPrivate = isPrivate,
                    questionSetOwnerId = currentUser.userId,
                    questionSetOwnerName = currentUser.userName
                )
                addQuestionSetToFirestore(questionSet)
                Toast.makeText(requireContext(), "Question Set added!", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            } else {
                Toast.makeText(requireContext(), "Please enter the name", Toast.LENGTH_SHORT).show()
            }
        }
        dialog.show()
    }

    private fun inputCheck(name: String): Boolean {
        return !(TextUtils.isEmpty(name))
    }
}