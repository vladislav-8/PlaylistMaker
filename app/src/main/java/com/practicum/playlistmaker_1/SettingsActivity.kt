package com.practicum.playlistmaker_1

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.appcompat.widget.Toolbar


class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        findViewById<Toolbar>(R.id.settings_toolbar).setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        findViewById<TextView>(R.id.shareTextView).setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.support_share_link))
            intent.type = "text/plain"
            try {
                startActivity(intent)
            } catch (ex: ActivityNotFoundException) {
                Toast.makeText(
                    this@SettingsActivity,
                    getString(R.string.not_found_app),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        findViewById<TextView>(R.id.callTextView).setOnClickListener {
            val sendIntent = Intent(Intent.ACTION_SENDTO)
            sendIntent.data = Uri.parse("mailto:")
            sendIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_mail)))
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.subject))
            sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.message))
            try {
                startActivity(sendIntent)
            } catch (ex: ActivityNotFoundException) {
                Toast.makeText(
                    this@SettingsActivity,
                    getString(R.string.not_found_app),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        findViewById<TextView>(R.id.agreementTextView).setOnClickListener {
            val sendIntent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(getString(R.string.agreement_link))
            try {
                startActivity(sendIntent)
            } catch (ex: ActivityNotFoundException) {
                Toast.makeText(
                    this@SettingsActivity,
                    getString(R.string.not_found_app),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        findViewById<Switch>(R.id.switch1).setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
            }
        }
    }
}
