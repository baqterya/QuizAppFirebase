package com.example.quizappfirebase.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class QuestionSet(
    var questionSetId: String? = null,
    var questionSetName: String? = null,
    var questionSetOwnerId: String? = null,
    var questionSetOwnerName: String? = null,
    var questionSetFavCount: Int = 0,
    var questionSetIsPrivate: Boolean = false,
) : Parcelable
