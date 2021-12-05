package com.example.quizappfirebase.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.quizappfirebase.data.User
import com.example.quizappfirebase.databinding.FragmentLoginBinding
import com.example.quizappfirebase.ui.activity.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
        private val binding get() = _binding!!

    val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textViewRegisterLogin.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            this.findNavController().navigate(action)
        }

        binding.buttonLogin.setOnClickListener {
            when {
                TextUtils.isEmpty(binding.editTextEmailLogin.text.toString().trim {it <= ' '}) -> {
                    Toast.makeText(
                        requireContext(),
                        "Please enter the email.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                TextUtils.isEmpty(binding.editTextPasswordLogin.text.toString().trim {it <= ' '}) -> {
                    Toast.makeText(
                        requireContext(),
                        "Please enter the password.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    val email: String = binding.editTextEmailLogin.text.toString().trim {it <= ' '}
                    val password: String = binding.editTextPasswordLogin.text.toString().trim {it <= ' '}

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val firebaseUser: FirebaseUser = FirebaseAuth.getInstance().currentUser!!

                                Toast.makeText(
                                    requireContext(),
                                    "You have logged in successfully.",
                                    Toast.LENGTH_SHORT
                                ).show()

                                db.collection("users")
                                    .whereEqualTo("userId", firebaseUser.uid)
                                    .get()
                                    .addOnSuccessListener {
                                        for (document in it.documents) {
                                            val newUser = User(
                                                firebaseUser.uid,
                                                document["userName"].toString(),
                                                email
                                            )

                                            val intent = Intent(activity, MainActivity::class.java)
                                            intent.flags =
                                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                            intent.putExtra("currentUser", newUser)
                                            startActivity(intent)
                                            requireActivity().finish()
                                        }
                                    }
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    task.exception!!.message.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            }
        }

    }
}