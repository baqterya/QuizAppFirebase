package com.example.quizappfirebase.data

import android.os.Parcelable
import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.parcelize.Parcelize
import java.lang.Exception

@Parcelize
data class QuestionSet(
    var questionSetId: String? = null,
    var questionSetName: String? = null,
    var questionSetOwnerId: String? = null,
    var questionSetOwnerName: String? = null,
    var questionSetFavCount: Int = 0,
    var questionSetIsPrivate: Boolean = false,
) : Parcelable
