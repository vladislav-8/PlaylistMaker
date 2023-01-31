package com.practicum.playlistmaker_1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val title = findViewById<TextView>(R.id.title2)

        title.setOnClickListener {
            finish()
        }
    }
}