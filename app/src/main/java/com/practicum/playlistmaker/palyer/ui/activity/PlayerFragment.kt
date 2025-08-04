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

        val artworkUrl512 = requireArguments().getString(ARTWORK_URL_512_EXTRA)
        artworkUrl512?.let {
            Glide.with(this)
                .load(it)
                .placeholder(R.drawable.placeholder_track_label)
                .transform(RoundedCorners(dpToPx(8F, requireContext())))
                .into(binding.trackLabel)
        }

        val trackName = requireArguments().getString(TRACK_NAME_EXTRA)?: UNKNOWN_TRACK_NAME
        val artistName = requireArguments().getString(ARTIST_NAME_EXTRA)?: UNKNOWN_ARTIST_NAME
        val collectionName = requireArguments().getString(COLLECTION_NAME_EXTRA)?: UNKNOWN_VALUE
        val releaseYear = requireArguments().getString(RELEASE_YEAR_EXTRA)?: UNKNOWN_VALUE
        val primaryGenreName = requireArguments().getString(PRIMARY_GENRE_NAME_EXTRA)?: UNKNOWN_VALUE
        val country = requireArguments().getString(COUNTRY_EXTRA)?: UNKNOWN_VALUE
        val trackTime = requireArguments().getString(TRACK_TIME_EXTRA)?: UNKNOWN_VALUE
        with(binding) {
            this.trackName.text = trackName
            this.artistName.text = artistName
            durationValue.text = trackTime
            albumValue.text = collectionName
            yearValue.text = releaseYear
            genreValue.text = primaryGenreName
            countryValue.text = country
        }

        val previewUrl = requireArguments().getString(PREVIEW_URL_EXTRA)
        viewModel = getKoin().get<PlayerViewModel> {
            parametersOf(previewUrl)
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

        private const val ARTWORK_URL_512_EXTRA = "ARTWORK_URL_512_EXTRA"
        private const val TRACK_NAME_EXTRA = "TRACK_NAME_EXTRA"
        private const val ARTIST_NAME_EXTRA = "ARTIST_NAME_EXTRA"
        private const val COLLECTION_NAME_EXTRA = "COLLECTION_NAME_EXTRA"
        private const val RELEASE_YEAR_EXTRA = "RELEASE_YEAR_EXTRA"
        private const val PRIMARY_GENRE_NAME_EXTRA = "PRIMARY_GENRE_NAME_EXTRA"
        private const val COUNTRY_EXTRA = "COUNTRY_EXTRA"
        private const val TRACK_TIME_EXTRA = "TRACK_TIME_EXTRA"
        private const val PREVIEW_URL_EXTRA = "PREVIEW_URL_EXTRA"

        fun createArgs(track: Track): Bundle =
            bundleOf(
                ARTWORK_URL_512_EXTRA to track.artworkUrl512,
                TRACK_NAME_EXTRA to track.trackName,
                ARTIST_NAME_EXTRA to track.artistName,
                COLLECTION_NAME_EXTRA to track.collectionName,
                RELEASE_YEAR_EXTRA to track.releaseYear,
                PRIMARY_GENRE_NAME_EXTRA to track.primaryGenreName,
                COUNTRY_EXTRA to track.country,
                TRACK_TIME_EXTRA to track.trackTime,
                PREVIEW_URL_EXTRA to track.previewUrl
            )
    }
}