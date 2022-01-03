package com.example.quizappfirebase.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.quizappfirebase.data.User
import com.example.quizappfirebase.databinding.FragmentRegisterBinding
import com.example.quizappfirebase.ui.activity.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
        private val binding get() = _binding!!

    private val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textViewLoginRegister.setOnClickListener {
            val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
            this.findNavController().navigate(action)
        }

        binding.buttonRegister.setOnClickListener {
            when {
                TextUtils.isEmpty(binding.editTextEmailRegister.text.toString().trim {it <= ' '}) -> {
                    Toast.makeText(
                        requireContext(),
                        "Please enter the email.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                TextUtils.isEmpty(binding.editTextPasswordRegister.text.toString().trim {it <= ' '}) -> {
                    Toast.makeText(
                        requireContext(),
                        "Please enter the password.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    val email: String = binding.editTextEmailRegister.text.toString().trim {it <= ' '}
                    val password: String = binding.editTextPasswordRegister.text.toString().trim {it <= ' '}
                    val username: String = binding.editTextUsernameRegister.text.toString().trim {it <= ' '}

                    db.collection("users")
                        .get()
                        .addOnSuccessListener { result ->
                            var isUsernameFree = true
                            for (document in result)
                                if (document.data["userName"] == username)
                                    isUsernameFree = false

                            if (isUsernameFree) {
                                createUser(email, password, username)
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    "This username is taken.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            }
        }
    }

    private fun createUser(email: String, password: String, username: String) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser: FirebaseUser = task.result!!.user!!

                    Toast.makeText(
                        requireContext(),
                        "You have registered successfully.",
                        Toast.LENGTH_SHORT
                    ).show()

                    val newUser = User(firebaseUser.uid, username, email)
                    addUserToFirestore(newUser)

                    val intent = Intent(activity, MainActivity::class.java)
                    intent.flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    intent.putExtra("currentUser", newUser)
                    startActivity(intent)
                    activity?.finish()
                } else {
                    Toast.makeText(
                        requireContext(),
                        task.exception!!.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun addUserToFirestore(newUser: User) {
        db.collection("users").document(newUser.userId!!)
            .set(newUser)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}