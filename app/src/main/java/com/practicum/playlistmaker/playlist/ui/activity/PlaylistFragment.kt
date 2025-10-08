package com.practicum.playlistmaker.playlist.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.common.domain.models.PlaylistWithTracks
import com.practicum.playlistmaker.databinding.FragmentPlaylistBinding
import com.practicum.playlistmaker.playlist.domain.model.PlaylistScreenState
import com.practicum.playlistmaker.playlist.ui.view_model.PlaylistViewModel
import org.koin.android.ext.android.getKoin
import org.koin.core.parameter.parametersOf

class PlaylistFragment : Fragment() {

    private lateinit var viewModel: PlaylistViewModel

    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        val playlistId: Long = requireArguments().getLong(PLAYLIST_ID_KEY)

        viewModel = getKoin().get<PlaylistViewModel> {
            parametersOf(playlistId)
        }
        viewModel.getScreenLiveData().observe(viewLifecycleOwner) { state ->
            binding.setState(state)
        }
    }

    private fun FragmentPlaylistBinding.setState(state: PlaylistScreenState) {
        when (state) {
            is PlaylistScreenState.Loading -> {
                hideViews()
                progressBar.isVisible = true
            }
            is PlaylistScreenState.Content -> {
                bindContent(state.playlist)
                hideViews()
                playlistLabel.isVisible = true
                playlistName.isVisible = true
                playlistDescription.isVisible = true
                playlistDuration.isVisible = true
                dot.isVisible = true
                playlistNumberOfTracks.isVisible = true
                shareButton.isVisible = true
                menuButton.isVisible = true
            }
        }
    }

    private fun FragmentPlaylistBinding.bindContent(playlist: PlaylistWithTracks) {
        playlist.label?.let {
            Glide.with(this@PlaylistFragment)
                .load(it)
                .placeholder(R.drawable.placeholder_track_label)
                .transform(CenterCrop())
                .into(binding.playlistLabel)
        }
        playlistName.text = playlist.name
        playlistDescription.text = playlist.description
        playlistDuration.text = "0 минут"
        val numberOfTracks = playlist.tracks.size
        playlistNumberOfTracks.text = resources.getQuantityString(
            R.plurals.number_of_tracks,
            numberOfTracks,
            numberOfTracks
        )
    }

    private fun FragmentPlaylistBinding.hideViews() {
        progressBar.isVisible = false
        playlistLabel.isVisible = false
        playlistName.isVisible = false
        playlistDescription.isVisible = false
        playlistDuration.isVisible = false
        dot.isVisible = false
        playlistNumberOfTracks.isVisible = false
        shareButton.isVisible = false
        menuButton.isVisible = false
    }

    companion object {
        private const val PLAYLIST_ID_KEY = "PLAYLIST_ID_KEY"

        fun createArgs(playlistId: Long): Bundle =
            bundleOf(
                PLAYLIST_ID_KEY to playlistId
            )
    }
}