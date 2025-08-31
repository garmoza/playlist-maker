package com.practicum.playlistmaker.palyer.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.common.domain.models.Track
import com.practicum.playlistmaker.common.ui.dpToPx
import com.practicum.playlistmaker.databinding.FragmentPlayerBinding
import com.practicum.playlistmaker.palyer.domain.model.PlayerState
import com.practicum.playlistmaker.palyer.domain.model.TrackNotAvailableToastState
import com.practicum.playlistmaker.palyer.ui.view_model.PlayerViewModel
import org.koin.android.ext.android.getKoin
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerFragment : Fragment() {

    private lateinit var viewModel: PlayerViewModel

    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!

    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }

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

        binding.toolbar.setOnClickListener {
            findNavController().navigateUp()
        }

        val track: Track? = requireArguments().getParcelable(TRACK_KEY)

        track?.artworkUrl512?.let {
            Glide.with(this)
                .load(it)
                .placeholder(R.drawable.placeholder_track_label)
                .transform(RoundedCorners(dpToPx(8F, requireContext())))
                .into(binding.trackLabel)
        }
        with(binding) {
            this.trackName.text = track?.trackName ?: UNKNOWN_TRACK_NAME
            this.artistName.text = track?.artistName ?: UNKNOWN_ARTIST_NAME
            durationValue.text = track?.trackTime ?: UNKNOWN_VALUE
            albumValue.text = track?.collectionName ?: UNKNOWN_VALUE
            yearValue.text = track?.releaseYear ?: UNKNOWN_VALUE
            genreValue.text = track?.primaryGenreName ?: UNKNOWN_VALUE
            countryValue.text = track?.country ?: UNKNOWN_VALUE
        }

        viewModel = getKoin().get<PlayerViewModel> {
            parametersOf(track?.previewUrl)
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

        binding.playButton.setOnClickListener {
            viewModel.switchBetweenPlayAndPause()
        }
    }

    private fun renderPlayerStatus(status: PlayerState) {
        if (status.isPlaying) {
            binding.playButton.setImageResource(R.drawable.ic_pause_track)
        } else {
            binding.playButton.setImageResource(R.drawable.ic_play_track)
        }

        binding.playtime.text = dateFormat.format(status.progress)
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