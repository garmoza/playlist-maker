package com.practicum.playlistmaker.clean.ui.search

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.practicum.playlistmaker.debounce.ClickDebounce
import com.practicum.playlistmaker.debounce.SearchDebounce
import com.practicum.playlistmaker.preferences.HISTORY_TRACKS_KEY
import com.practicum.playlistmaker.preferences.PLAYLIST_MAKER_PREFERENCES
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.clean.Creator
import com.practicum.playlistmaker.clean.ui.ARTIST_NAME_EXTRA
import com.practicum.playlistmaker.clean.ui.ARTWORK_URL_512_EXTRA
import com.practicum.playlistmaker.clean.ui.COLLECTION_NAME_EXTRA
import com.practicum.playlistmaker.clean.ui.COUNTRY_EXTRA
import com.practicum.playlistmaker.clean.ui.PREVIEW_URL_EXTRA
import com.practicum.playlistmaker.clean.ui.PRIMARY_GENRE_NAME_EXTRA
import com.practicum.playlistmaker.clean.ui.player.PlayerActivity
import com.practicum.playlistmaker.clean.ui.RELEASE_YEAR_EXTRA
import com.practicum.playlistmaker.clean.ui.TRACK_NAME_EXTRA
import com.practicum.playlistmaker.clean.ui.TRACK_TIME_EXTRA
import com.practicum.playlistmaker.preferences.SearchHistory
import com.practicum.playlistmaker.clean.data.network.BadResponseException
import com.practicum.playlistmaker.clean.domain.api.TracksInteractor
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.clean.domain.models.Track

class SearchActivity : AppCompatActivity() {

    private val tracks = ArrayList<Track>()

    private var searchedValue = DEFAULT_SEARCHED_VALUE

    private lateinit var tracksInteractor: TracksInteractor

    private lateinit var binding: ActivitySearchBinding
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var historyTrackAdapter: HistoryTrackAdapter
    private lateinit var listener: OnSharedPreferenceChangeListener
    private lateinit var handler: Handler
    private lateinit var clickDebounce: ClickDebounce
    private lateinit var searchDebounce: SearchDebounce
    private lateinit var searchTask: Runnable

    private val tracksResponseHandler = object : TracksInteractor.TracksConsumer {
        override fun consume(foundTracks: List<Track>) {
            Log.i(TAG, "search start handle result")
            handler.post {
                tracks.clear()
                if (foundTracks.isNotEmpty()) {
                    Log.i(TAG, "search success result")
                    tracks.addAll(foundTracks)
                    binding.setState(SearchActivityState.TRACK_LIST)
                } else {
                    Log.i(TAG, "search empty result")
                    binding.setState(SearchActivityState.TRACK_NOT_FOUND)
                }
                trackAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tracksInteractor = Creator.provideTracksInteractor()

        val sharedPreferences = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)
        val searchHistory = SearchHistory(sharedPreferences)

        val onTrackClick = {track: Track ->
            searchHistory.addTrack(track)

            val displayIntent = Intent(this@SearchActivity, PlayerActivity::class.java)
            displayIntent.putExtra(ARTWORK_URL_512_EXTRA, track.artworkUrl512)
            displayIntent.putExtra(TRACK_NAME_EXTRA, track.trackName)
            displayIntent.putExtra(ARTIST_NAME_EXTRA, track.artistName)
            displayIntent.putExtra(COLLECTION_NAME_EXTRA, track.collectionName)
            displayIntent.putExtra(RELEASE_YEAR_EXTRA, track.releaseYear)
            displayIntent.putExtra(PRIMARY_GENRE_NAME_EXTRA, track.primaryGenreName)
            displayIntent.putExtra(COUNTRY_EXTRA, track.country)
            displayIntent.putExtra(TRACK_TIME_EXTRA, track.trackTime)
            displayIntent.putExtra(PREVIEW_URL_EXTRA, track.previewUrl)
            startActivity(displayIntent)
        }

        handler = Handler(Looper.getMainLooper())

        clickDebounce = ClickDebounce(Looper.getMainLooper())

        val onSearchedTrackDebounceClick = {track: Track ->
            clickDebounce.execute(
                { onTrackClick(track) },
                binding.recyclerViewTrack.id
            )
        }
        val onHistoryTrackDebounceClick = {track: Track ->
            clickDebounce.execute(
                { onTrackClick(track) },
                binding.recyclerViewHistoryTrack.id
            )
        }

        trackAdapter = TrackAdapter(tracks, onSearchedTrackDebounceClick)
        historyTrackAdapter = HistoryTrackAdapter(searchHistory, onHistoryTrackDebounceClick)

        binding.recyclerViewTrack.adapter = trackAdapter
        binding.recyclerViewHistoryTrack.adapter = historyTrackAdapter

        listener = OnSharedPreferenceChangeListener { sharedPreferences, key ->
            if (key == HISTORY_TRACKS_KEY) {
                historyTrackAdapter.notifyDataSetChanged()
            }
        }
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)

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
            binding.setState(SearchActivityState.EMPTY)
        }

        searchDebounce = SearchDebounce(Looper.getMainLooper())

        searchTask = Runnable {
            binding.setState(SearchActivityState.SEARCHING)
            try {
                tracksInteractor.searchTracks(
                    binding.editTextSearch.text.toString(),
                    tracksResponseHandler
                )
            } catch (ex: BadResponseException) {
                Log.i(TAG, "search bad response")
                binding.setState(SearchActivityState.NETWORK_PROBLEM)
            }
        }

        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchedValue = s.toString()
                binding.imageViewClear.visibility = if (s.isNullOrEmpty()) {
                    searchDebounce.remove(searchTask)
                    View.GONE
                } else {
                    searchDebounce.execute(searchTask)
                    View.VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        binding.editTextSearch.addTextChangedListener(searchTextWatcher)

        binding.editTextSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                Log.i(TAG, "search action editor click")
                searchDebounce.execute(searchTask)
            }
            false
        }

        binding.placeholderButton.setOnClickListener {
            Log.i(TAG, "search action button click")
            searchDebounce.execute(searchTask)
        }

        binding.editTextSearch.setOnFocusChangeListener { v, hasFocus ->
            if (binding.editTextSearch.hasFocus() && binding.editTextSearch.text.isEmpty() && searchHistory.getSize() > 0) {
                binding.setState(SearchActivityState.HISTORY)
            } else {
                binding.setState(SearchActivityState.TRACK_LIST)
            }
        }

        binding.editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (binding.editTextSearch.hasFocus() && binding.editTextSearch.text.isEmpty() && searchHistory.getSize() > 0) {
                    binding.setState(SearchActivityState.HISTORY)
                } else {
                    binding.setState(SearchActivityState.TRACK_LIST)
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.clearHistoryButton.setOnClickListener {
            searchHistory.clear()
            binding.setState(SearchActivityState.EMPTY)
        }
    }

    override fun onDestroy() {
        searchDebounce.remove(searchTask)
        super.onDestroy()
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

    private fun ActivitySearchBinding.setState(state: SearchActivityState) {
        when (state) {
            SearchActivityState.EMPTY -> {
                hideViews()
            }
            SearchActivityState.TRACK_LIST -> {
                hideViews()
                recyclerViewTrack.visibility = View.VISIBLE
            }
            SearchActivityState.HISTORY -> {
                hideViews()
                historyViewGroup.visibility = View.VISIBLE
            }
            SearchActivityState.TRACK_NOT_FOUND -> {
                hideViews()
                placeholderImage.visibility = View.VISIBLE
                placeholderImage.setImageResource(R.drawable.track_not_found)
                placeholderMessage.visibility = View.VISIBLE
                placeholderMessage.setText(R.string.placeholder_message_not_found)
            }
            SearchActivityState.NETWORK_PROBLEM -> {
                hideViews()
                placeholderImage.visibility = View.VISIBLE
                placeholderImage.setImageResource(R.drawable.network_problem)
                placeholderMessage.visibility = View.VISIBLE
                placeholderMessage.setText(R.string.placeholder_message_network_problem)
                placeholderAdditionalMessage.visibility = View.VISIBLE
                placeholderAdditionalMessage.setText(R.string.placeholder_aditional_message_network_problems)
                placeholderButton.visibility = View.VISIBLE
                placeholderButton.setText(R.string.update)
            }
            SearchActivityState.SEARCHING -> {
                hideViews()
                progressBar.visibility = View.VISIBLE
            }
        }
    }

    private fun ActivitySearchBinding.hideViews() {
        recyclerViewTrack.visibility = View.GONE
        placeholderImage.visibility = View.GONE
        placeholderMessage.visibility = View.GONE
        placeholderAdditionalMessage.visibility = View.GONE
        placeholderButton.visibility = View.GONE
        historyViewGroup.visibility = View.GONE
        progressBar.visibility = View.GONE
    }

    companion object {
        const val SEARCHED_VALUE_KEY = "SEARCH_VALUE"
        const val DEFAULT_SEARCHED_VALUE = ""
        const val TAG = "SearchActivity"
    }
}