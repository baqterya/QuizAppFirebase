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
    var questionSetFavCount: Int = 0
) : Parcelable {
    companion object {
        private const val TAG = "QUESTION_SET_ERROR"

        fun DocumentSnapshot.toQuestionSet(): QuestionSet? {
            return try {
                val questionSetId = getString("questionSetId")!!
                val questionSetName = getString("questionSetName")!!
                val questionSetOwnerId = getString("questionSetOwnerId")!!
                val questionSetFavCount = 0
                return QuestionSet(questionSetId, questionSetName, questionSetOwnerId, questionSetFavCount)
            } catch (e: Exception) {
                Log.e(TAG, "Error converting QuestionSet", e)
                null
            }
        }
    }
}
