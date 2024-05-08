package com.example.cs481localnotification

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.util.*

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createNotificationChannel()

        val btnOpenCalendar = findViewById<Button>(R.id.btnOpenCalendar)
        val txDate = findViewById<EditText>(R.id.txDate)

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                val selectedDate = "$year-${month + 1}-$dayOfMonth"
                txDate.setText(selectedDate)
            },
            year,
            month,
            day
        )

        btnOpenCalendar.setOnClickListener {
            datePickerDialog.show()
        }

        findViewById<Button>(R.id.btnReminder).setOnClickListener {
            sendNotification()
        }
    }

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel: NotificationChannel = NotificationChannel("C10","Title", NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = "Description"
            }
            val notificationManager:NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendNotification() {
        val title = findViewById<EditText>(R.id.txTitle).text.toString()
        val reminder = findViewById<EditText>(R.id.txReminder).text.toString()
        val date = findViewById<EditText>(R.id.txDate).text.toString()

        if (title.isEmpty() || reminder.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        showAlert(title, reminder, date)

        val builder: NotificationCompat.Builder = NotificationCompat.Builder(this, "C10")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(reminder)
            .setContentText(date)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            notify(10, builder.build())
        }
    }

    private fun showAlert(title: String, reminder: String, date: String)
    {
        AlertDialog.Builder(this)
            .setTitle("Reminder Set!")
            .setMessage("Title: " + title + "\nReminder: " + reminder + "\nDate: " + date)
            .setPositiveButton("Okay"){_,_ ->}
            .show()
    }
}
