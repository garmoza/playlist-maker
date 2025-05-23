package com.practicum.playlistmaker.search.ui.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.practicum.playlistmaker.common.ui.debounce.ClickDebounce
import com.practicum.playlistmaker.common.ui.debounce.SearchDebounce
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.palyer.ui.activity.PlayerActivity
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.common.domain.models.Track
import com.practicum.playlistmaker.common.ui.debounce.SearchDebounce.Companion.NONE_DELAY
import com.practicum.playlistmaker.search.domain.model.ErrorType
import com.practicum.playlistmaker.search.domain.model.SearchScreenState
import com.practicum.playlistmaker.search.ui.view_model.SearchViewModel

class SearchActivity : AppCompatActivity() {

    private val viewModel by viewModels<SearchViewModel> { SearchViewModel.getViewModelFactory() }

    private var searchedValue = DEFAULT_SEARCHED_VALUE

    private lateinit var binding: ActivitySearchBinding
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var trackHistoryAdapter: TrackAdapter
    private lateinit var handler: Handler
    private lateinit var clickDebounce: ClickDebounce
    private lateinit var searchDebounce: SearchDebounce

    private val searchTask = Runnable {
        viewModel.searchTracks(binding.editTextSearch.text.toString())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handler = Handler(Looper.getMainLooper())
        clickDebounce = ClickDebounce(Looper.getMainLooper())
        searchDebounce = SearchDebounce(Looper.getMainLooper())

        viewModel.getSearchScreenLiveData().observe(this) { state ->
            when (state) {
                is SearchScreenState.Content -> {
                    if (state.tracks.isEmpty()) {
                        binding.setState(SearchActivityState.EMPTY)
                    } else {
                        trackAdapter.setItems(state.tracks)
                        binding.setState(SearchActivityState.TRACK_LIST)
                    }
                }
                is SearchScreenState.History -> {
                    if (state.tracks.isEmpty()) {
                        binding.setState(SearchActivityState.EMPTY)
                    } else {
                        trackHistoryAdapter.setItems(state.tracks)
                        binding.setState(SearchActivityState.HISTORY)
                    }
                }
                is SearchScreenState.Loading -> binding.setState(SearchActivityState.SEARCHING)
                is SearchScreenState.Error -> {
                    when (state.type) {
                        ErrorType.TRACK_NOT_FOUND -> binding.setState(SearchActivityState.TRACK_NOT_FOUND)
                        ErrorType.NETWORK_PROBLEM -> binding.setState(SearchActivityState.NETWORK_PROBLEM)
                    }
                }
            }
        }

        initTrackAdapter(this::onTrackClick)
        initTrackHistoryAdapter(this::onTrackClick)

        binding.toolbarSearch.setOnClickListener {
            finish()
        }

        binding.imageViewClear.setOnClickListener {
            trackAdapter.setItems(emptyList())
            binding.editTextSearch.setText("")
            binding.editTextSearch.clearFocus()
            val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            manager.hideSoftInputFromWindow(binding.editTextSearch.windowToken, 0)
            binding.setState(SearchActivityState.EMPTY)
        }

        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchedValue = s.toString()
                binding.imageViewClear.isVisible = if (s.isNullOrEmpty()) {
                    searchDebounce.remove(searchTask)
                    false
                } else {
                    searchDebounce.execute(searchTask)
                    true
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        binding.editTextSearch.addTextChangedListener(searchTextWatcher)

        binding.editTextSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                Log.i(TAG, "Search action editor click")
                searchDebounce.execute(searchTask, NONE_DELAY)
            }
            false
        }

        binding.placeholderButton.setOnClickListener {
            Log.i(TAG, "Search action button click")
            searchDebounce.execute(searchTask, NONE_DELAY)
        }

        binding.editTextSearch.setOnFocusChangeListener { v, hasFocus ->
            if (binding.editTextSearch.hasFocus() && binding.editTextSearch.text.isEmpty()) {
                viewModel.displayHistory()
            } else {
                binding.setState(SearchActivityState.TRACK_LIST)
            }
        }

        binding.editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (binding.editTextSearch.hasFocus() && binding.editTextSearch.text.isEmpty()) {
                    viewModel.displayHistory()
                } else {
                    binding.setState(SearchActivityState.TRACK_LIST)
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.clearHistoryButton.setOnClickListener {
            viewModel.clearHistory()
        }
    }

    private fun onTrackClick(track: Track) {
        viewModel.addTrackToHistory(track)
        PlayerActivity.show(this@SearchActivity, track)
    }

    private fun initTrackAdapter(onTrackClick: (Track) -> Unit) {
        val onSearchedTrackDebounceClick = {track: Track ->
            clickDebounce.execute(
                { onTrackClick(track) },
                binding.recyclerViewTrack.id
            )
        }
        trackAdapter = TrackAdapter(onSearchedTrackDebounceClick)
        binding.recyclerViewTrack.adapter = trackAdapter
    }

    private fun initTrackHistoryAdapter(onTrackClick: (Track) -> Unit) {
        val onHistoryTrackDebounceClick = {track: Track ->
            clickDebounce.execute(
                { onTrackClick(track) },
                binding.recyclerViewHistoryTrack.id
            )
        }
        trackHistoryAdapter = TrackAdapter(onHistoryTrackDebounceClick)
        binding.recyclerViewHistoryTrack.adapter = trackHistoryAdapter
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
        binding.editTextSearch.setText(searchedValue)
    }

    private fun ActivitySearchBinding.setState(state: SearchActivityState) {
        when (state) {
            SearchActivityState.EMPTY -> {
                hideViews()
                Log.i(TAG, "Activity state is EMPTY")
            }
            SearchActivityState.TRACK_LIST -> {
                hideViews()
                recyclerViewTrack.isVisible = true
                Log.i(TAG, "Activity state is TRACK_LIST")
            }
            SearchActivityState.HISTORY -> {
                hideViews()
                historyViewGroup.isVisible = true
                Log.i(TAG, "Activity state is HISTORY")
            }
            SearchActivityState.TRACK_NOT_FOUND -> {
                hideViews()
                placeholderImage.isVisible = true
                placeholderImage.setImageResource(R.drawable.track_not_found)
                placeholderMessage.isVisible = true
                placeholderMessage.setText(R.string.placeholder_message_not_found)
                Log.i(TAG, "Activity state is TRACK_NOT_FOUND")
            }
            SearchActivityState.NETWORK_PROBLEM -> {
                hideViews()
                placeholderImage.isVisible = true
                placeholderImage.setImageResource(R.drawable.network_problem)
                placeholderMessage.isVisible = true
                placeholderMessage.setText(R.string.placeholder_message_network_problem)
                placeholderAdditionalMessage.isVisible = true
                placeholderAdditionalMessage.setText(R.string.placeholder_aditional_message_network_problems)
                placeholderButton.isVisible = true
                placeholderButton.setText(R.string.update)
                Log.i(TAG, "Activity state is NETWORK_PROBLEM")
            }
            SearchActivityState.SEARCHING -> {
                hideViews()
                progressBar.isVisible = true
                Log.i(TAG, "Activity state is SEARCHING")
            }
        }
    }

    private fun ActivitySearchBinding.hideViews() {
        recyclerViewTrack.isVisible = false
        placeholderImage.isVisible = false
        placeholderMessage.isVisible = false
        placeholderAdditionalMessage.isVisible = false
        placeholderButton.isVisible = false
        historyViewGroup.isVisible = false
        progressBar.isVisible = false
    }

    companion object {
        private const val SEARCHED_VALUE_KEY = "SEARCH_VALUE"
        private const val DEFAULT_SEARCHED_VALUE = ""
        private val TAG = SearchActivity::class.simpleName
    }
}