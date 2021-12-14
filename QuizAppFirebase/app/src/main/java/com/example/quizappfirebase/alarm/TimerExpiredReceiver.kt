package com.example.quizappfirebase.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class TimerExpiredReceiver : BroadcastReceiver(){
    override fun onReceive(context: Context, intent: Intent) {
        NotificationUtil.showTimerExpired(context)
    }
}