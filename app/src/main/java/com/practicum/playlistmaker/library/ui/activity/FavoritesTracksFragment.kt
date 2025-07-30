package com.practicum.playlistmaker.library.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.FragmentFavoritesTracksBinding
import com.practicum.playlistmaker.library.domain.model.FavoritesTracksScreenState
import com.practicum.playlistmaker.library.ui.view_model.FavoritesTracksViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesTracksFragment : Fragment() {

    private val viewModel by viewModel<FavoritesTracksViewModel>()

    private lateinit var binding: FragmentFavoritesTracksBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getLiveData().observe(viewLifecycleOwner) { state ->
            when (state) {
                is FavoritesTracksScreenState.Content -> {
                    if (state.tracks.isEmpty()) {
                        binding.setState(FavoritesTracksFragmentState.TRACKS_NOT_FOUND)
                    } else {
                        // render
                    }
                }
            }
        }
    }

    private fun FragmentFavoritesTracksBinding.setState(state: FavoritesTracksFragmentState) {
        when (state) {
            FavoritesTracksFragmentState.TRACKS_NOT_FOUND -> {
                hideViews()
                placeholderImage.isVisible = true
                placeholderMessage.isVisible = true
            }
        }
    }

    private fun FragmentFavoritesTracksBinding.hideViews() {
        placeholderImage.isVisible = false
        placeholderMessage.isVisible = false
    }
}