package com.practicum.playlistmaker.favourite.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.common.domain.models.Track
import com.practicum.playlistmaker.favourite.ui.compose.FavoritesTracks
import com.practicum.playlistmaker.main.ui.theme.Theme
import com.practicum.playlistmaker.player.ui.activity.PlayerFragment

class FavoritesTracksFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                Theme {
                    FavoritesTracks(
                        onTrackClick = { track -> onTrackClick(track) }
                    )
                }
            }
        }
    }

    private fun onTrackClick(track: Track) {
        findNavController().navigate(
            R.id.action_libraryFragment_to_playerFragment,
            PlayerFragment.createArgs(track)
        )
    }
}