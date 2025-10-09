package com.practicum.playlistmaker.playlist.ui.activity

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.common.domain.models.Playlist

class EditPlaylistFragment : AddPlaylistFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setTitle(R.string.edit)
        binding.buttonCreate.setText(R.string.save)

        val playlist: Playlist = requireArguments().getParcelable(PLAYLIST_KEY)!!

        playlist.label?.let {
            bindLabel(it)
            viewModel.onPickPlaylistLabel(it)
        }

        binding.playlistNameEditText.setText(playlist.name)
        viewModel.onNameChanged(playlist.name)

        val playlistDescription = playlist.description ?: EMPTY_DESCRIPTION
        binding.playlistDescriptionEditText.setText(playlistDescription)
        viewModel.onDescriptionChanged(playlistDescription)
    }

    override fun onNavigateUp() {
        findNavController().navigateUp()
    }

    companion object {
        private const val PLAYLIST_KEY = "PLAYLIST_KEY"
        private const val EMPTY_DESCRIPTION = ""

        fun createArgs(playlist: Playlist): Bundle =
            bundleOf(
                PLAYLIST_KEY to playlist
            )
    }
}