package com.practicum.playlistmaker_1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        findViewById<Toolbar>(R.id.settings_toolbar).setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        findViewById<ImageView>(R.id.clearImageView).setOnClickListener {
            findViewById<EditText>(R.id.inputEditText).setText("")
        }

        val simpleTextWatcher = object : TextWatcher {

            override fun onTextChanged(s: CharSequence?, s1: Int, s2: Int, s3: Int) {
                if (s.isNullOrEmpty()) {
                    findViewById<ImageView>(R.id.clearImageView).visibility = GONE
                } else {
                    findViewById<ImageView>(R.id.clearImageView).visibility = VISIBLE
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
        }
        findViewById<EditText>(R.id.inputEditText).addTextChangedListener(simpleTextWatcher)
    }
}