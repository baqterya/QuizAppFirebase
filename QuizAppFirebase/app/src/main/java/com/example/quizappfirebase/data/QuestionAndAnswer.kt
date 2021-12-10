package com.example.quizappfirebase.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class QuestionAndAnswer(
    var questionAndAnswerId: String? = null,
    var questionAndAnswerQuestionText: String? = null,
    var questionAndAnswerAnswerText: String? = null,
    var questionAndAnswerParentQuestionSetId: String? = null,
    var questionAndAnswerParentCategoryId: String? = null,
) : Parcelable
