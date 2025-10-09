package com.practicum.playlistmaker.playlist.ui.activity

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
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.common.domain.models.PlaylistWithTracks
import com.practicum.playlistmaker.common.domain.models.Track
import com.practicum.playlistmaker.common.ui.debounceClick
import com.practicum.playlistmaker.common.ui.dpToPx
import com.practicum.playlistmaker.databinding.FragmentPlaylistBinding
import com.practicum.playlistmaker.player.ui.activity.PlayerFragment
import com.practicum.playlistmaker.playlist.domain.model.PlaylistScreenState
import com.practicum.playlistmaker.playlist.domain.model.PlaylistToastState
import com.practicum.playlistmaker.playlist.ui.view_model.PlaylistViewModel
import com.practicum.playlistmaker.search.ui.activity.TrackAdapter
import org.koin.android.ext.android.getKoin
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistFragment : Fragment() {

    private lateinit var viewModel: PlaylistViewModel

    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!

    private val durationFormat by lazy { SimpleDateFormat("mm", Locale.getDefault()) }

    private lateinit var trackAdapter: TrackAdapter

    private lateinit var bottomSheetPlaylistMenuBehavior: BottomSheetBehavior<LinearLayout>

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
        viewModel.getToastLiveData().observe(viewLifecycleOwner) { state ->
            when (state) {
                is PlaylistToastState.CantShareEmptyPlaylist -> {
                    Toast.makeText(requireContext(), getString(R.string.cant_share_empty_playlist), Toast.LENGTH_SHORT).show()
                    viewModel.toastWasShow()
                }
                is PlaylistToastState.None -> Unit
            }
        }

        initTrackAdapter(this::onTrackClick, this::onLongTrackClick)

        binding.shareButton.setOnClickListener {
            viewModel.sharePlaylist()
        }

        initBottomSheetPlaylistMenu()

        binding.menuButton.setOnClickListener {
            bottomSheetPlaylistMenuBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.bottomSheetShare.setOnClickListener {
            viewModel.sharePlaylist()
        }

        binding.bottomSheetEdit.setOnClickListener {
            findNavController().navigate(
                R.id.action_playlistFragment_to_editPlaylistFragment,
                EditPlaylistFragment.createArgs(viewModel.getPlaylist())
            )
        }

        binding.bottomSheetDelete.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext(), R.style.CustomDialogTheme)
                .setTitle(
                    getString(R.string.want_to_delete_playlist, binding.playlistName.text)
                )
                .setPositiveButton(R.string.yes) { _, _ ->
                    viewModel.deletePlaylist()
                    findNavController().navigateUp()
                }.setNeutralButton(R.string.no) { _, _ ->
                    // nothing
                }.show()
        }
    }

    private fun onTrackClick(track: Track) {
        findNavController().navigate(
            R.id.action_playlistFragment_to_playerFragment,
            PlayerFragment.createArgs(track)
        )
    }

    private fun onLongTrackClick(track: Track) {
        MaterialAlertDialogBuilder(requireContext(), R.style.CustomDialogTheme)
            .setTitle(R.string.want_to_delete_track)
            .setPositiveButton(R.string.yes) { _, _ ->
                viewModel.deleteTrackFromPlaylist(track)
            }.setNeutralButton(R.string.no) { _, _ ->
                // nothing
            }.show()
    }

    private fun initTrackAdapter(
        onTrackClick: (Track) -> Unit,
        onLongTrackClick: (Track) -> Unit
    ) {
        val onTrackDebounceClick = debounceClick(
            coroutineScope = viewLifecycleOwner.lifecycleScope,
            action = onTrackClick
        )
        trackAdapter = TrackAdapter(onTrackDebounceClick, onLongTrackClick)
        binding.recyclerViewTrack.adapter = trackAdapter
    }

    private fun initBottomSheetPlaylistMenu() {
        bottomSheetPlaylistMenuBehavior = BottomSheetBehavior.from(binding.bottomSheetPlaylistMenu)
        bottomSheetPlaylistMenuBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        bottomSheetPlaylistMenuBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
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

        viewModel.loadContent()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun FragmentPlaylistBinding.setState(state: PlaylistScreenState) {
        when (state) {
            is PlaylistScreenState.Loading -> {
                hideViews()
                progressBar.isVisible = true
            }
            is PlaylistScreenState.Content -> {
                bindContent(state.playlistWithTracks)
                bindBottomSheetMenu(state.playlistWithTracks)
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

    private fun FragmentPlaylistBinding.bindContent(model: PlaylistWithTracks) {
        model.playlist.label?.let {
            Glide.with(this@PlaylistFragment)
                .load(it)
                .placeholder(R.drawable.placeholder_track_label)
                .transform(CenterCrop())
                .into(binding.playlistLabel)
        }
        playlistName.text = model.playlist.name
        playlistDescription.text = model.playlist.description
        val totalDuration = durationFormat.format(model.totalDurationMillis).toInt()
        playlistDuration.text = resources.getQuantityString(
            R.plurals.number_of_minutes,
            totalDuration,
            totalDuration
        )
        val numberOfTracks = model.tracks.size
        playlistNumberOfTracks.text = resources.getQuantityString(
            R.plurals.number_of_tracks,
            numberOfTracks,
            numberOfTracks
        )

        trackAdapter.setItems(model.tracks)
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

    private fun FragmentPlaylistBinding.bindBottomSheetMenu(model: PlaylistWithTracks) {
        Glide.with(this@PlaylistFragment)
            .load(model.playlist.label)
            .placeholder(R.drawable.placeholder_track_label)
            .transform(CenterCrop(),  RoundedCorners(dpToPx(2F, requireContext())))
            .into(bottomSheetPlaylistMenuInclude.label)
        bottomSheetPlaylistMenuInclude.playlistName.text = model.playlist.name

        val numberOfTracks = model.tracks.size
        bottomSheetPlaylistMenuInclude.tracksCount.text = resources.getQuantityString(
            R.plurals.number_of_tracks,
            numberOfTracks,
            numberOfTracks
        )
    }

    companion object {
        private const val PLAYLIST_ID_KEY = "PLAYLIST_ID_KEY"

        fun createArgs(playlistId: Long): Bundle =
            bundleOf(
                PLAYLIST_ID_KEY to playlistId
            )
    }
}