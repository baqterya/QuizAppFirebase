package com.example.quizappfirebase.ui.fragment

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import android.widget.Toast
import com.example.quizappfirebase.alarm.TimerExpiredReceiver
import com.example.quizappfirebase.databinding.FragmentSettingsBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import android.text.format.DateFormat
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class SettingsFragment : Fragment(), TimePickerDialog.OnTimeSetListener {
    private var _binding: FragmentSettingsBinding? = null
        private val binding get() = _binding!!

    private val currentUser = Firebase.auth.currentUser!!
    private val db = Firebase.firestore

    private var hourOfAlarm = 0
    private var minuteOfAlarm = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.title = "Settings"

        db.collection("users").document(currentUser.uid)
            .get()
            .addOnSuccessListener {
                binding.editTextEditUsernameSettings.setText(it["userName"] as String)
            }

        binding.buttonChangeUsernameSettings.setOnClickListener {
            val newUserName = binding.editTextEditUsernameSettings.text.toString()
            if (inputCheck(newUserName)) {
                db.collection("users").document(currentUser.uid)
                    .update("userName", newUserName)
                db.collection("questionSets")
                    .whereEqualTo("questionSetOwnerId", currentUser.uid)
                    .get()
                    .addOnSuccessListener {
                        for (questionSet in it) {
                            questionSet.reference.update("questionSetOwnerName", newUserName)
                        }
                    }

                Toast.makeText(context, "Update successful", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Please enter a name", Toast.LENGTH_SHORT).show()
            }

        }

        binding.buttonTimePickerSettings.setOnClickListener {
            val calendar = Calendar.getInstance()
            hourOfAlarm = calendar.get(Calendar.HOUR_OF_DAY)
            minuteOfAlarm = calendar.get(Calendar.MINUTE)

            TimePickerDialog(
                requireContext(),
                this,
                hourOfAlarm,
                minuteOfAlarm,
                DateFormat.is24HourFormat(requireContext())
            ).show()
        }

        binding.buttonDisableAlarmSettings.setOnClickListener {
            cancelTimer()
        }
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        hourOfAlarm = hourOfDay
        minuteOfAlarm = minute
        setAlarm(requireContext(), hourOfAlarm, minuteOfAlarm)
        Toast.makeText(requireContext(), "Reminder set on $hourOfAlarm:$minuteOfAlarm", Toast.LENGTH_SHORT).show()
    }

    private fun cancelTimer() {
        removeAlarm(requireContext())
        Toast.makeText(requireContext(), "Reminder disabled", Toast.LENGTH_SHORT).show()
    }

    companion object {
        fun setAlarm(context: Context, hour: Int, minute: Int) {
            val date = Calendar.getInstance()
            date.set(Calendar.HOUR_OF_DAY, hour)
            date.set(Calendar.MINUTE, minute)
            date.set(Calendar.SECOND, 0)

            val time = (hour * 3.6e6 + minute * 60000).toLong()

            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, TimerExpiredReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP, time, 60000, pendingIntent
            )
        }

        fun removeAlarm(context: Context) {
            val intent = Intent(context, TimerExpiredReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(pendingIntent)
        }
    }

    private fun inputCheck(name: String): Boolean {
        return !(TextUtils.isEmpty(name))
    }
}