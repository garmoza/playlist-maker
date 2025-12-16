package com.practicum.playlistmaker.search.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.common.domain.models.Track
import com.practicum.playlistmaker.common.ui.DEBOUNCE_REQUEST_DELAY_DEFAULT
import com.practicum.playlistmaker.common.ui.compose.ContentPlaceholder
import com.practicum.playlistmaker.common.ui.compose.Loader
import com.practicum.playlistmaker.common.ui.compose.RoundedButton
import com.practicum.playlistmaker.common.ui.compose.Toolbar
import com.practicum.playlistmaker.common.ui.compose.Tracks
import com.practicum.playlistmaker.common.ui.debounceClick
import com.practicum.playlistmaker.common.ui.debounceRequest
import com.practicum.playlistmaker.main.ui.theme.AppBlack
import com.practicum.playlistmaker.main.ui.theme.AppBlue
import com.practicum.playlistmaker.main.ui.theme.Theme
import com.practicum.playlistmaker.search.domain.model.SearchScreenState
import com.practicum.playlistmaker.search.ui.view_model.SearchViewModel
import kotlinx.coroutines.Job
import org.koin.androidx.compose.koinViewModel

@Composable
fun Search(
    onTrackClick: (track: Track) -> Unit
) {
    val viewModel: SearchViewModel = koinViewModel()

    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleScope = lifecycleOwner.lifecycleScope

    val onTrackDebounceClick: (track: Track) -> Unit = remember(lifecycleScope) {
        debounceClick(
            coroutineScope = lifecycleScope,
            action = { track ->
                viewModel.addTrackToHistory(track)
                onTrackClick(track)
            }
        )
    }

    val screenState by viewModel.getSearchScreenLiveData()
        .observeAsState(SearchScreenState.Content(emptyList()))

    Search(
        screenState = screenState,
        onSearch = viewModel::searchTracks,
        onTrackClick = onTrackDebounceClick,
        displayHistory = viewModel::displayHistory
    )
}

@Composable
private fun Search(
    screenState: SearchScreenState,
    onSearch: (expression: String) -> Unit,
    onTrackClick: (track: Track) -> Unit,
    displayHistory: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface)
    ) {
        Toolbar(R.string.search)
        SearchTextField(onSearch, displayHistory)

        when (screenState) {
            is SearchScreenState.Loading -> Loader()
            is SearchScreenState.Content -> {
                if (screenState.tracks.isEmpty()) {
                    ContentPlaceholder(
                        modifier = Modifier.padding(top = 106.dp),
                        imageId = R.drawable.track_not_found,
                        messageId = R.string.placeholder_message_not_found
                    )
                } else {
                    Tracks(screenState.tracks, onTrackClick)
                }
            }
            is SearchScreenState.History -> {
                if (screenState.tracks.isNotEmpty()) {
                    Tracks(screenState.tracks, onTrackClick)
                    RoundedButton(
                        modifier = Modifier.padding(top = 24.dp),
                        textId = R.string.clear_history,
                        onClick =  {}
                    )
                }
            }
            is SearchScreenState.Error -> {}
        }
    }
}

@Composable
private fun SearchTextField(
    onSearch: (expression: String) -> Unit,
    displayHistory: () -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleScope = lifecycleOwner.lifecycleScope

    val debounceSearch = remember(lifecycleScope) {
        debounceRequest(lifecycleScope) { changedText: String ->
            onSearch(changedText)
        }
    }

    var searchText by remember { mutableStateOf("") }
    var searchJob: Job? = remember { null }

    val focusRequester = remember { FocusRequester() }
    var isFocused by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(36.dp)
                .background(
                    color = MaterialTheme.colorScheme.secondary,
                    shape = RoundedCornerShape(8.dp)
                )
        )

        OutlinedTextField(
            value = searchText,
            onValueChange = { newText ->
                searchText = newText
                searchJob?.cancel()
                if (isFocused && searchText.isEmpty()) {
                    displayHistory()
                } else {
                    searchJob = debounceSearch(searchText, DEBOUNCE_REQUEST_DELAY_DEFAULT)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .offset(x = (-16).dp)
                .focusRequester(focusRequester)
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused

                    if (isFocused && searchText.isEmpty()) {
                        displayHistory()
                    }
                },
            singleLine = true,
            placeholder = {
                Text(
                    text = stringResource(R.string.search),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSecondary
                )
            },
            leadingIcon = {
                Icon(
                    modifier = Modifier.offset(x = 16.dp),
                    painter = painterResource(R.drawable.ic_search),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            },
            trailingIcon = {
                if (searchText.isNotEmpty()) {
                    Icon(
                        modifier = Modifier.offset(x = 16.dp),
                        painter = painterResource(R.drawable.ic_clear),
                        contentDescription = stringResource(R.string.clear),
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                }
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = AppBlack,
                unfocusedTextColor = AppBlack,
                cursorColor = AppBlue
            ),
            textStyle = MaterialTheme.typography.bodyLarge,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = { /* Обработка Done */ }
            )
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun SearchLoadingPreviewLightTheme() {
    Theme(false) {
        Search(
            screenState = SearchScreenState.Loading,
            onSearch = {},
            onTrackClick = {},
            displayHistory = {}
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun SearchLoadingPreviewDarkTheme() {
    Theme(true) {
        Search(
            screenState = SearchScreenState.Loading,
            onSearch = {},
            onTrackClick = {},
            displayHistory = {}
        )
    }
}