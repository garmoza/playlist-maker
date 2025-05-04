package com.practicum.playlistmaker.palyer.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.common.domain.models.Track
import com.practicum.playlistmaker.common.ui.ARTIST_NAME_EXTRA
import com.practicum.playlistmaker.common.ui.ARTWORK_URL_512_EXTRA
import com.practicum.playlistmaker.common.ui.COLLECTION_NAME_EXTRA
import com.practicum.playlistmaker.common.ui.COUNTRY_EXTRA
import com.practicum.playlistmaker.common.ui.PREVIEW_URL_EXTRA
import com.practicum.playlistmaker.common.ui.PRIMARY_GENRE_NAME_EXTRA
import com.practicum.playlistmaker.common.ui.RELEASE_YEAR_EXTRA
import com.practicum.playlistmaker.common.ui.TRACK_NAME_EXTRA
import com.practicum.playlistmaker.common.ui.TRACK_TIME_EXTRA
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker.common.ui.dpToPx
import com.practicum.playlistmaker.palyer.domain.model.PlayerState
import com.practicum.playlistmaker.palyer.domain.model.TrackNotAvailableToastState
import com.practicum.playlistmaker.palyer.ui.view_model.PlayerViewModel
import org.koin.android.ext.android.getKoin
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    private lateinit var viewModel: PlayerViewModel

    private lateinit var binding: ActivityPlayerBinding

    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setOnClickListener {
            finish()
        }

        val artworkUrl512 = intent.getStringExtra(ARTWORK_URL_512_EXTRA)
        artworkUrl512?.let {
            Glide.with(this)
                .load(it)
                .placeholder(R.drawable.placeholder_track_label)
                .transform(RoundedCorners(dpToPx(8F, this)))
                .into(binding.trackLabel)
        }

        val trackName = intent.getStringExtra(TRACK_NAME_EXTRA)?: UNKNOWN_TRACK_NAME
        val artistName = intent.getStringExtra(ARTIST_NAME_EXTRA)?: UNKNOWN_ARTIST_NAME
        val collectionName = intent.getStringExtra(COLLECTION_NAME_EXTRA)?: UNKNOWN_VALUE
        val releaseYear = intent.getStringExtra(RELEASE_YEAR_EXTRA)?: UNKNOWN_VALUE
        val primaryGenreName = intent.getStringExtra(PRIMARY_GENRE_NAME_EXTRA)?: UNKNOWN_VALUE
        val country = intent.getStringExtra(COUNTRY_EXTRA)?: UNKNOWN_VALUE
        val trackTime = intent.getStringExtra(TRACK_TIME_EXTRA)?: UNKNOWN_VALUE
        with(binding) {
            this.trackName.text = trackName
            this.artistName.text = artistName
            durationValue.text = trackTime
            albumValue.text = collectionName
            yearValue.text = releaseYear
            genreValue.text = primaryGenreName
            countryValue.text = country
        }

        val previewUrl = intent.getStringExtra(PREVIEW_URL_EXTRA)
        viewModel = getKoin().get<PlayerViewModel> {
            parametersOf(previewUrl)
        }
        viewModel.getPlayerLiveData().observe(this) { status ->
            renderPlayerStatus(status)
        }
        viewModel.getToastLiveData().observe(this) { status ->
            when (status) {
                is TrackNotAvailableToastState.Show -> {
                    Toast.makeText(this, getString(R.string.track_not_available), Toast.LENGTH_SHORT).show()
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

    companion object {
        private const val UNKNOWN_TRACK_NAME = "Track Unknown"
        private const val UNKNOWN_ARTIST_NAME = "Artist Unknown"
        private const val UNKNOWN_VALUE = "-"

        fun show(context: Context, track: Track) {
            val intent = Intent(context, PlayerActivity::class.java)
            intent.putExtra(ARTWORK_URL_512_EXTRA, track.artworkUrl512)
            intent.putExtra(TRACK_NAME_EXTRA, track.trackName)
            intent.putExtra(ARTIST_NAME_EXTRA, track.artistName)
            intent.putExtra(COLLECTION_NAME_EXTRA, track.collectionName)
            intent.putExtra(RELEASE_YEAR_EXTRA, track.releaseYear)
            intent.putExtra(PRIMARY_GENRE_NAME_EXTRA, track.primaryGenreName)
            intent.putExtra(COUNTRY_EXTRA, track.country)
            intent.putExtra(TRACK_TIME_EXTRA, track.trackTime)
            intent.putExtra(PREVIEW_URL_EXTRA, track.previewUrl)

            context.startActivity(intent)
        }
    }
}