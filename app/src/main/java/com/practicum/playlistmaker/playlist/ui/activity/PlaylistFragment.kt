package com.practicum.playlistmaker.playlist.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.common.domain.models.Playlist
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

        val playlist: Playlist = requireArguments().getParcelable(PLAYLIST_KEY)!!

        playlist.label?.let {
            Glide.with(this)
                .load(it)
                .placeholder(R.drawable.placeholder_track_label)
                .transform(CenterCrop())
                .into(binding.playlistLabel)
        }
        with(binding) {
            playlistName.text = playlist.name
            playlistDescription.text = playlist.description
            playlistDuration.text = "0 минут"
            val numberOfTracks = playlist.trackIds.size
            playlistNumberOfTracks.text = resources.getQuantityString(
                R.plurals.number_of_tracks,
                numberOfTracks,
                numberOfTracks
            )
        }

        viewModel = getKoin().get<PlaylistViewModel> {
            parametersOf(playlist.id)
        }
        viewModel.getScreenLiveData().observe(viewLifecycleOwner) { status ->
            when (status) {
                is PlaylistScreenState.Loading -> {
                    binding.playlistName.text = "Loading"
                }
                is PlaylistScreenState.Content -> {
                    binding.playlistName.text = "Loaded"
                }
            }
        }
    }

    companion object {
        private const val PLAYLIST_KEY = "PLAYLIST_KEY"

        fun createArgs(playlist: Playlist): Bundle =
            bundleOf(
                PLAYLIST_KEY to playlist
            )
    }
}