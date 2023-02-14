package com.practicum.playlistmaker_1

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar


class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val toolbar = findViewById<Toolbar>(R.id.settings_toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        val shareTextView = findViewById<TextView>(R.id.shareTextView)
        val callTextView = findViewById<TextView>(R.id.callTextView)
        val agreementTextView = findViewById<TextView>(R.id.agreementTextView)

        shareTextView.setOnClickListener {
            val shareText = "https://practicum.yandex.ru/profile/android-developer/"
            val sendIntent = Intent(Intent.ACTION_SEND)
            sendIntent.putExtra(Intent.EXTRA_TEXT, shareText)
            sendIntent.type = "text/plain"
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

        callTextView.setOnClickListener {
            val message = "Спасибо разработчикам и разработчицам за крутое приложение!"
            val subject = "Сообщение разработчикам и разработчицам приложения Playlist Maker"
            val sendIntent = Intent(Intent.ACTION_SENDTO)
            sendIntent.data = Uri.parse("mailto:")
            sendIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("tchervladislav@yandex.ru"))
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
            sendIntent.putExtra(Intent.EXTRA_TEXT, message)
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)

        }

        agreementTextView.setOnClickListener {
            val sendIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse("https://yandex.ru/legal/practicum_offer/"))
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

    }

}
