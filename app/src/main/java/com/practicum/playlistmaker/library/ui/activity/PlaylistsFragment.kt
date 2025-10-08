package com.practicum.playlistmaker.library.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.common.domain.models.Playlist
import com.practicum.playlistmaker.common.ui.debounceClick
import com.practicum.playlistmaker.databinding.FragmentPlaylistsBinding
import com.practicum.playlistmaker.library.domain.model.PlaylistsScreenState
import com.practicum.playlistmaker.library.ui.view_model.PlaylistsViewModel
import com.practicum.playlistmaker.playlist.ui.activity.AddPlaylistFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {

    private val viewModel by viewModel<PlaylistsViewModel>()

    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!

    private lateinit var playlistAdapter: PlaylistAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
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
                        playlistAdapter.setItems(state.playlists)
                        binding.setState(PlaylistsFragmentState.PLAYLIST_LIST)
                    }
                }
            }
        }

        initPlaylistRecyclerView(this::onPlaylistClick)

        binding.newPlaylistButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_libraryFragment_to_addPlaylistFragment
            )
        }

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
            R.id.action_libraryFragment_to_playlistFragment
        )
    }

    private fun initPlaylistRecyclerView(onPlaylistClick: (Playlist) -> Unit) {
        val onPlaylistDebounceClick = debounceClick(
            coroutineScope = viewLifecycleOwner.lifecycleScope,
            action = onPlaylistClick
        )
        playlistAdapter = PlaylistAdapter(onPlaylistDebounceClick)
        binding.recyclerViewPlaylist.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerViewPlaylist.adapter = playlistAdapter
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        viewModel.loadPlaylists()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun Fragment.showPlaylistCreatedToast(playlistName: String) {
        val message = getString(R.string.playlist_has_been_crated, playlistName)
        val toast = Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT)

        toast.show()
    }

    private fun FragmentPlaylistsBinding.setState(state: PlaylistsFragmentState) {
        when (state) {
            PlaylistsFragmentState.PLAYLISTS_NOT_FOUND -> {
                hideViews()
                newPlaylistButton.isVisible = true
                placeholderImage.isVisible = true
                placeholderImage.setImageResource(R.drawable.track_not_found)
                placeholderMessage.isVisible = true
                placeholderMessage.setText(R.string.you_have_not_playlists)
            }
            PlaylistsFragmentState.PLAYLIST_LIST -> {
                hideViews()
                newPlaylistButton.isVisible = true
                recyclerViewPlaylist.isVisible = true
            }
        }
    }

    private fun FragmentPlaylistsBinding.hideViews() {
        newPlaylistButton.isVisible = false
        placeholderImage.isVisible = false
        placeholderMessage.isVisible = false
        recyclerViewPlaylist.isVisible = false
    }

    companion object {
        fun newInstance() = PlaylistsFragment()
    }
}