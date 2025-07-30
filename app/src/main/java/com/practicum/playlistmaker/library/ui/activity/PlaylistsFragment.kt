package com.practicum.playlistmaker.library.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.FragmentPlaylistsBinding
import com.practicum.playlistmaker.library.domain.model.PlaylistsScreenState
import com.practicum.playlistmaker.library.ui.view_model.PlaylistsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {

    private val viewModel by viewModel<PlaylistsViewModel>()

    private lateinit var binding: FragmentPlaylistsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getLiveData().observe(viewLifecycleOwner) { state ->
            when (state) {
                is PlaylistsScreenState.Content -> {
                    if (state.playlists.isEmpty()) {
                        binding.setState(PlaylistsFragmentState.PLAYLISTS_NOT_FOUND)
                    } else {
                        // render
                    }
                }
            }
        }
    }

    private fun FragmentPlaylistsBinding.setState(state: PlaylistsFragmentState) {
        when (state) {
            PlaylistsFragmentState.PLAYLISTS_NOT_FOUND -> {
                hideViews()
                placeholderButton.isVisible = true
                placeholderImage.isVisible = true
                placeholderMessage.isVisible = true
            }
        }
    }

    private fun FragmentPlaylistsBinding.hideViews() {
        placeholderButton.isVisible = false
        placeholderImage.isVisible = false
        placeholderMessage.isVisible = false
    }
}