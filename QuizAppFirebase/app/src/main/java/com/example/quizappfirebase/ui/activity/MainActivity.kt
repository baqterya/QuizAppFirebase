package com.example.quizappfirebase.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.findNavController
import com.example.quizappfirebase.R
import com.example.quizappfirebase.data.User
import com.example.quizappfirebase.databinding.ActivityMainBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var toolbar: MaterialToolbar? = null
    private val db = Firebase.firestore
    private val currentUser = Firebase.auth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        toolbar = binding.topToolbar
        toolbar!!.title = "All Question Sets"
        setSupportActionBar(toolbar)

        binding.bottomNavigationBar.setOnItemSelectedListener {
            val navController = findNavController(R.id.main_fragment_container)
            when (it.itemId) {
                R.id.menu_explore -> {
                    navController.navigate(R.id.listAllQuestionSetsFragment)
                }
                R.id.menu_user_sets -> {
                    navController.navigate(R.id.listUsersQuestionSetsFragment)
                }
                R.id.menu_user_favourites -> {
                    db.collection("users").document(currentUser!!.uid)
                        .get()
                        .addOnSuccessListener { user ->
                            val bundle = Bundle()
                            bundle.putParcelable("currentUser", user.toObject<User>())
                            navController.navigate(
                                R.id.listFavouriteQuestionSetsFragment,
                                bundle
                            )
                        }
                }
            }
            true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.top_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_settings -> {
                findNavController(R.id.main_fragment_container).navigate(R.id.settingsFragment)
            }
            R.id.menu_logout -> userLogout()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun userLogout() {
        Firebase.auth.signOut()
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        this.finish()
    }

}