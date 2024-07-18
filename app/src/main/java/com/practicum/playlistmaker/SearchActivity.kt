package com.practicum.playlistmaker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.view.isVisible
import com.practicum.playlistmaker.response.ITunseTracksResponse
import com.practicum.playlistmaker.SearchPlaceholderState.*
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.model.Track
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private val retrofit = Retrofit.Builder()
        .baseUrl(ITUNSE_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunseService = retrofit.create(ITunseApi::class.java)
    private val tracks = ArrayList<Track>()
    private val trackAdapter = TrackAdapter(tracks)

    private var searchedValue = DEFAULT_SEARCHED_VALUE

    private lateinit var binding: ActivitySearchBinding

    private val iTunseTracksResponseHandler = object : Callback<ITunseTracksResponse> {
        override fun onResponse(
            call: Call<ITunseTracksResponse>,
            response: Response<ITunseTracksResponse>
        ) {
            Log.i(TAG, "search onResponse start")
            tracks.clear()
            when (response.code()) {
                200 -> {
                    Log.i(TAG, "search response is 200")
                    if (response.body()?.results?.isNotEmpty() == true) {
                        tracks.addAll(response.body()?.results!!)
                        setPlaceholder(GONE)
                    } else {
                        Log.i(TAG, "search empty response")
                        setPlaceholder(TRACK_NOT_FOUND)
                    }
                }
                else -> {
                    Log.i(TAG, "search bad response")
                    setPlaceholder(NETWORK_PROBLEM)
                }
            }
            trackAdapter.notifyDataSetChanged()
            Log.i(TAG, "search onResponse end")
        }

        override fun onFailure(call: Call<ITunseTracksResponse>, t: Throwable) {
            Log.i(TAG, "search onFailure")
            setPlaceholder(NETWORK_PROBLEM)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarSearch.setOnClickListener {
            finish()
        }

        binding.imageViewClear.setOnClickListener {
            tracks.clear()
            trackAdapter.notifyDataSetChanged()
            binding.editTextSearch.setText("")
            binding.editTextSearch.clearFocus()
            val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            manager.hideSoftInputFromWindow(binding.editTextSearch.windowToken, 0)
        }

        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchedValue = s.toString()
                binding.imageViewClear.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        binding.editTextSearch.addTextChangedListener(searchTextWatcher)

        binding.recyclerViewTrack.adapter = trackAdapter

        binding.editTextSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                Log.i(TAG, "search action editor click")
                iTunseService.search(binding.editTextSearch.text.toString())
                    .enqueue(iTunseTracksResponseHandler)
            }
            false
        }

        binding.placeholderButton.setOnClickListener {
            Log.i(TAG, "search action button click")
            iTunseService.search(binding.editTextSearch.text.toString())
                .enqueue(iTunseTracksResponseHandler)
        }
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

    private fun setPlaceholder(state: SearchPlaceholderState) {
        with(binding) {
            recyclerViewTrack.isVisible = state == GONE

            placeholderImage.visibility = View.GONE
            placeholderMessage.visibility = View.GONE
            placeholderAdditionalMessage.visibility = View.GONE
            placeholderButton.visibility = View.GONE

            state.image?.let {
                placeholderImage.setImageResource(it)
                placeholderImage.visibility = View.VISIBLE
            }
            state.message?.let {
                placeholderMessage.text = getString(it)
                placeholderMessage.visibility = View.VISIBLE
            }
            state.additionalMessage?.let {
                placeholderAdditionalMessage.text = getString(it)
                placeholderAdditionalMessage.visibility = View.VISIBLE
            }
            state.button?.let {
                placeholderButton.text = getString(it)
                placeholderButton.visibility = View.VISIBLE
            }
        }
    }

    companion object {
        const val SEARCHED_VALUE_KEY = "SEARCH_VALUE"
        const val DEFAULT_SEARCHED_VALUE = ""
        const val ITUNSE_BASE_URL = "https://itunes.apple.com"
        const val TAG = "SearchActivity"
    }
}