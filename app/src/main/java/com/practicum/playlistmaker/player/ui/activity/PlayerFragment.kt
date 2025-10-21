package com.practicum.playlistmaker.player.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.common.domain.models.Playlist
import com.practicum.playlistmaker.common.domain.models.Track
import com.practicum.playlistmaker.common.ui.debounceClick
import com.practicum.playlistmaker.common.ui.dpToPx
import com.practicum.playlistmaker.databinding.FragmentPlayerBinding
import com.practicum.playlistmaker.player.domain.model.PlayerState
import com.practicum.playlistmaker.player.domain.model.TrackAddedToPlaylistToastState
import com.practicum.playlistmaker.player.domain.model.TrackNotAvailableToastState
import com.practicum.playlistmaker.player.ui.view_model.MediaPlayerViewModel
import org.koin.android.ext.android.getKoin
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerFragment : Fragment() {

    private lateinit var viewModel: MediaPlayerViewModel

    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!

    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }

    private lateinit var playlistBottomSheetAdapter: PlaylistBottomSheetAdapter

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        val track: Track = requireArguments().getParcelable(TRACK_KEY)!!

        track.artworkUrl512?.let {
            Glide.with(this)
                .load(it)
                .placeholder(R.drawable.placeholder_track_label)
                .transform(RoundedCorners(dpToPx(8F, requireContext())))
                .into(binding.trackLabel)
        }
        with(binding) {
            this.trackName.text = track.trackName ?: UNKNOWN_TRACK_NAME
            this.artistName.text = track.artistName ?: UNKNOWN_ARTIST_NAME
            durationValue.text = track.trackTime ?: UNKNOWN_VALUE
            albumValue.text = track.collectionName ?: UNKNOWN_VALUE
            yearValue.text = track.releaseYear ?: UNKNOWN_VALUE
            genreValue.text = track.primaryGenreName ?: UNKNOWN_VALUE
            countryValue.text = track.country ?: UNKNOWN_VALUE
        }
        renderLikeTrackButton(track.isFavorite)

        viewModel = getKoin().get<MediaPlayerViewModel> {
            parametersOf(track)
        }
        viewModel.getPlayerLiveData().observe(viewLifecycleOwner) { status ->
            renderPlayerStatus(status)
        }
        viewModel.getToastLiveData().observe(viewLifecycleOwner) { status ->
            when (status) {
                is TrackNotAvailableToastState.Show -> {
                    Toast.makeText(requireContext(), getString(R.string.track_not_available), Toast.LENGTH_SHORT).show()
                    viewModel.toastWasShow()
                }
                is TrackNotAvailableToastState.None -> Unit
            }
        }
        viewModel.getPlaylistsLiveData().observe(viewLifecycleOwner) { playlists ->
            playlistBottomSheetAdapter.setItems(playlists)
        }
        viewModel.getTrackAddedToPlaylistLiveData().observe(viewLifecycleOwner) { status ->
            when (status) {
                is TrackAddedToPlaylistToastState.ShowNewAdded -> {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

                    val message = getString(R.string.added_to_playlist, status.trackName)
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    viewModel.toastWasShow()

                    viewModel.loadPlaylists()
                }
                is TrackAddedToPlaylistToastState.ShowAlreadyAdded -> {
                    val message = getString(R.string.track_already_in_playlist, status.trackName)
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    viewModel.toastWasShow()
                }
                is TrackAddedToPlaylistToastState.None -> Unit
            }
        }

        binding.playButton.setOnClickListener {
            viewModel.switchBetweenPlayAndPause()
        }
        binding.likeTrackButton.setOnClickListener {
            viewModel.onFavouriteClick()
        }

        initPlaylistRecyclerView(this::onPlaylistClick)

        initBottomSheetBehavior()

        binding.addTrackButton.setOnClickListener  {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.newPlaylistButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_playerFragment_to_addPlaylistFragment
            )
        }
    }

    private fun onPlaylistClick(playlist: Playlist) {
        viewModel.addCurrentTrackToPlaylist(playlist)
    }

    private fun initPlaylistRecyclerView(onPlaylistClick: (Playlist) -> Unit) {
        val onPlaylistDebounceClick = debounceClick(
            coroutineScope = viewLifecycleOwner.lifecycleScope,
            action = onPlaylistClick
        )
        playlistBottomSheetAdapter = PlaylistBottomSheetAdapter(onPlaylistDebounceClick)
        binding.recyclerViewPlaylist.adapter = playlistBottomSheetAdapter
    }

    private fun initBottomSheetBehavior() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        bottomSheetBehavior.peekHeight = (resources.displayMetrics.heightPixels * 0.65).toInt()

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.isVisible = false
                    }
                    else -> {
                        binding.overlay.isVisible = true
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // nothing
            }
        })
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        viewModel.loadPlaylists()
    }

    private fun renderPlayerStatus(status: PlayerState) {
        if (status.isLoading) {
            binding.setVisibleViews(isVisible = false)
            binding.progressBar.isVisible = true
            return
        }

        binding.setVisibleViews(isVisible = true)
        binding.progressBar.isVisible = false

        if (status.isPlaying) {
            binding.playButton.setImageResource(R.drawable.ic_pause_track)
        } else {
            binding.playButton.setImageResource(R.drawable.ic_play_track)
        }

        binding.playtime.text = dateFormat.format(status.progress)

        renderLikeTrackButton(status.isFavourite)
    }

    private fun renderLikeTrackButton(isFavourite: Boolean) {
        if (isFavourite) {
            binding.likeTrackButton.setImageResource(R.drawable.ic_liked_track)
        } else {
            binding.likeTrackButton.setImageResource(R.drawable.ic_like_track)
        }
    }

    private fun FragmentPlayerBinding.setVisibleViews(isVisible: Boolean) {
        progressBar.isVisible = isVisible
        trackLabel.isVisible = isVisible
        trackName.isVisible = isVisible
        artistName.isVisible = isVisible
        playButton.isVisible = isVisible
        addTrackButton.isVisible = isVisible
        likeTrackButton.isVisible = isVisible
        playtime.isVisible = isVisible
        durationLabel.isVisible = isVisible
        durationValue.isVisible = isVisible
        albumLabel.isVisible = isVisible
        albumValue.isVisible = isVisible
        yearLabel.isVisible = isVisible
        yearValue.isVisible = isVisible
        genreLabel.isVisible = isVisible
        genreValue.isVisible = isVisible
        countryLabel.isVisible = isVisible
        countryValue.isVisible = isVisible
    }

    override fun onPause() {
        viewModel.pause()
        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val UNKNOWN_TRACK_NAME = "Track Unknown"
        private const val UNKNOWN_ARTIST_NAME = "Artist Unknown"
        private const val UNKNOWN_VALUE = "-"

        private const val TRACK_KEY = "TRACK_KEY"

        fun createArgs(track: Track): Bundle =
            bundleOf(
                TRACK_KEY to track
            )
    }
}