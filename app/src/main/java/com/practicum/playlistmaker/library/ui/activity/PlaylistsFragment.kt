package com.practicum.playlistmaker.library.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.common.domain.models.Playlist
import com.practicum.playlistmaker.common.ui.debounceClick
import com.practicum.playlistmaker.library.ui.compose.CreatedPlaylists
import com.practicum.playlistmaker.main.ui.theme.Theme
import com.practicum.playlistmaker.playlist.ui.activity.AddPlaylistFragment
import com.practicum.playlistmaker.playlist.ui.activity.PlaylistFragment

class PlaylistsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                Theme {
                    CreatedPlaylists(
                        onNewPlaylistButtonClick = {
                            findNavController().navigate(
                                R.id.action_libraryFragment_to_addPlaylistFragment
                            )
                        },
                        onPlaylistClick = debounceClick(
                            coroutineScope = viewLifecycleOwner.lifecycleScope,
                            action = { playlist -> onPlaylistClick(playlist) }
                        )
                    )
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        findNavController().getBackStackEntry(R.id.libraryFragment).savedStateHandle
            .getLiveData<String>(AddPlaylistFragment.PLAYLIST_CREATED_LIVE_DATA_KEY)
            .observe(viewLifecycleOwner) { playlistName ->
                findNavController().getBackStackEntry(R.id.libraryFragment).savedStateHandle
                    .remove<String>(AddPlaylistFragment.PLAYLIST_CREATED_LIVE_DATA_KEY)

                showPlaylistCreatedToast(playlistName)
            }
    }

    private fun onPlaylistClick(playlist: Playlist) {
        findNavController().navigate(
            R.id.action_libraryFragment_to_playlistFragment,
            PlaylistFragment.createArgs(playlist.id!!)
        )
    }

    private fun Fragment.showPlaylistCreatedToast(playlistName: String) {
        val message = getString(R.string.playlist_has_been_crated, playlistName)
        val toast = Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT)

        toast.show()
    }

    companion object {
        fun newInstance() = PlaylistsFragment()
    }
}