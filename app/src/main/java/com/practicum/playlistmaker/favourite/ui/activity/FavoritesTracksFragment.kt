package com.practicum.playlistmaker.favourite.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.common.domain.models.Track
import com.practicum.playlistmaker.common.ui.debounceClick
import com.practicum.playlistmaker.databinding.FragmentFavoritesTracksBinding
import com.practicum.playlistmaker.favourite.domain.model.FavoritesTracksScreenState
import com.practicum.playlistmaker.favourite.ui.view_model.FavoritesTracksViewModel
import com.practicum.playlistmaker.player.ui.activity.PlayerFragment
import com.practicum.playlistmaker.search.ui.activity.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesTracksFragment : Fragment() {

    private val viewModel by viewModel<FavoritesTracksViewModel>()

    private var _binding: FragmentFavoritesTracksBinding? = null
    private val binding get() = _binding!!

    private lateinit var trackAdapter: TrackAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getLiveData().observe(viewLifecycleOwner) { state ->
            when (state) {
                is FavoritesTracksScreenState.Loading -> {
                    binding.setState(FavoritesTracksFragmentState.LOADING)
                }
                is FavoritesTracksScreenState.Content -> {
                    if (state.tracks.isEmpty()) {
                        binding.setState(FavoritesTracksFragmentState.TRACKS_NOT_FOUND)
                    } else {
                        trackAdapter.setItems(state.tracks)
                        binding.setState(FavoritesTracksFragmentState.TRACK_LIST)
                    }
                }
            }
        }

        initTrackAdapter(this::onTrackClick)

        viewModel.getFavouriteTracks()
    }

    private fun onTrackClick(track: Track) {
        findNavController().navigate(
            R.id.action_libraryFragment_to_playerFragment,
            PlayerFragment.createArgs(track)
        )
    }

    private fun initTrackAdapter(onTrackClick: (Track) -> Unit) {
        val onFavouriteTrackDebounceClick = debounceClick(
            coroutineScope = viewLifecycleOwner.lifecycleScope,
            action = onTrackClick
        )
        trackAdapter = TrackAdapter(onFavouriteTrackDebounceClick)
        binding.recyclerViewTrack.adapter = trackAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun FragmentFavoritesTracksBinding.setState(state: FavoritesTracksFragmentState) {
        when (state) {
            FavoritesTracksFragmentState.LOADING -> {
                hideViews()
                progressBar.isVisible = true
            }
            FavoritesTracksFragmentState.TRACKS_NOT_FOUND -> {
                hideViews()
                placeholderImage.isVisible = true
                placeholderImage.setImageResource(R.drawable.track_not_found)
                placeholderMessage.isVisible = true
                placeholderMessage.setText(R.string.your_library_is_empty)
            }
            FavoritesTracksFragmentState.TRACK_LIST -> {
                hideViews()
                recyclerViewTrack.isVisible = true
            }
        }
    }

    private fun FragmentFavoritesTracksBinding.hideViews() {
        placeholderImage.isVisible = false
        placeholderMessage.isVisible = false
        progressBar.isVisible = false
        recyclerViewTrack.isVisible = false
    }

    companion object {
        fun newInstance() = FavoritesTracksFragment()
    }
}