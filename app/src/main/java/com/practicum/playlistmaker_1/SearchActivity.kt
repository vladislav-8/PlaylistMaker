package com.practicum.playlistmaker_1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar

class SearchActivity : AppCompatActivity() {

    companion object {
        const val SEARCH_QUERY = "SEARCH_QUERY"
    }

    private var searchInputQuery = ""
    private lateinit var inputEditText: EditText

    private val simpleTextWatcher = object : TextWatcher {

        override fun onTextChanged(s: CharSequence?, s1: Int, s2: Int, s3: Int) {
            if (s.isNullOrEmpty()) {
                findViewById<ImageView>(R.id.clearImageView).visibility = GONE
            } else {
                findViewById<ImageView>(R.id.clearImageView).visibility = VISIBLE
            }
            searchInputQuery = s.toString()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun afterTextChanged(s: Editable?) {}

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        inputEditText = findViewById(R.id.inputEditText)
        inputEditText.requestFocus()

        findViewById<Toolbar>(R.id.settings_toolbar).setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
        findViewById<ImageView>(R.id.clearImageView).setOnClickListener { clearSearchForm() }
        findViewById<EditText>(R.id.inputEditText).addTextChangedListener(simpleTextWatcher)
    }


    private fun clearSearchForm() {
        findViewById<EditText>(R.id.inputEditText).setText("")
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_QUERY, searchInputQuery)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchInputQuery = savedInstanceState.getString(SEARCH_QUERY, "")
        inputEditText.setText(searchInputQuery)

    }
}

