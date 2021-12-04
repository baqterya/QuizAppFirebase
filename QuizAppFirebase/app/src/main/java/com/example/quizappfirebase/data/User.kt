package com.example.quizappfirebase.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var userName: String? = null,
    var userEmail: String? = null,
    var userQuestionSet: ArrayList<String>,
    var userFavQuestionSet: ArrayList<String>
) : Parcelable
