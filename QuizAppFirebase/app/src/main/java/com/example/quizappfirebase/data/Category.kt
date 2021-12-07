package com.example.quizappfirebase.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    var categoryId: String? = null,
    var categoryName: String? = null,
    var categoryParentQuestionSet: String? = null,
) : Parcelable
