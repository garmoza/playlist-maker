package com.practicum.playlistmaker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toolbar

class SearchActivity : AppCompatActivity() {

    private var searchedValue = DEFAULT_SEARCHED_VALUE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val toolbarSettings = findViewById<Toolbar>(R.id.toolbarSearch)
        toolbarSettings.setOnClickListener {
            finish()
        }

        val editTextSearch = findViewById<EditText>(R.id.editTextSearch)
        val imageViewClear = findViewById<ImageView>(R.id.imageViewClear)

        imageViewClear.setOnClickListener {
            editTextSearch.setText("")
            editTextSearch.clearFocus()
            val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            manager.hideSoftInputFromWindow(editTextSearch.windowToken, 0)
        }

        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchedValue = s.toString()
                imageViewClear.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        editTextSearch.addTextChangedListener(searchTextWatcher)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCHED_VALUE_KEY, searchedValue)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchedValue = savedInstanceState.getString(SEARCHED_VALUE_KEY, DEFAULT_SEARCHED_VALUE)
        findViewById<EditText>(R.id.editTextSearch).setText(searchedValue)
    }

    companion object {
        const val SEARCHED_VALUE_KEY = "SEARCH_VALUE"
        const val DEFAULT_SEARCHED_VALUE = ""
    }
}