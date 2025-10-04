package com.practicum.playlistmaker.library.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistsBinding
import com.practicum.playlistmaker.library.domain.model.PlaylistsScreenState
import com.practicum.playlistmaker.library.ui.view_model.PlaylistsViewModel
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

        initPlaylistRecyclerView()

        binding.newPlaylistButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_libraryFragment_to_addPlaylistFragment
            )
        }

        findNavController().getBackStackEntry(R.id.libraryFragment).savedStateHandle
            .getLiveData<Boolean>("playlist_created")
            .observe(viewLifecycleOwner) { playlistCreated ->
                if (playlistCreated) {
                    viewModel.loadPlaylists()
                }
            }
    }

    private fun initPlaylistRecyclerView() {
        playlistAdapter = PlaylistAdapter()
        binding.recyclerViewPlaylist.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerViewPlaylist.adapter = playlistAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun FragmentPlaylistsBinding.setState(state: PlaylistsFragmentState) {
        when (state) {
            PlaylistsFragmentState.PLAYLISTS_NOT_FOUND -> {
                hideViews()
                newPlaylistButton.isVisible = true
                newPlaylistButton.setText(R.string.new_playlist)
                placeholderImage.isVisible = true
                placeholderImage.setImageResource(R.drawable.track_not_found)
                placeholderMessage.isVisible = true
                placeholderMessage.setText(R.string.you_have_not_playlists)
            }
            PlaylistsFragmentState.PLAYLIST_LIST -> {
                hideViews()
                newPlaylistButton.isVisible = true
                newPlaylistButton.setText(R.string.new_playlist)
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