package com.example.quizappfirebase.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var userId: String? = null,
    var userName: String? = null,
    var userEmail: String? = null,
    var userQuestionSet: ArrayList<String> = arrayListOf(),
    var userFavQuestionSet: ArrayList<String> = arrayListOf()
) : Parcelable
