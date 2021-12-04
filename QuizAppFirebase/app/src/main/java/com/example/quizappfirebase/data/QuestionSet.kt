package com.example.quizappfirebase.data

data class QuestionSet(
    var questionSetId: String? = null,
    var questionSetName: String? = null,
    var questionSetOwner: String? = null,
    var questionSetFavCount: Int = 0
)
