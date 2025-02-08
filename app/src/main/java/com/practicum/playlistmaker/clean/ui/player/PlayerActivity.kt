package com.practicum.playlistmaker.clean.ui.player

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.clean.presentation.player.Player
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.clean.Creator
import com.practicum.playlistmaker.clean.domain.models.Track
import com.practicum.playlistmaker.clean.ui.ARTIST_NAME_EXTRA
import com.practicum.playlistmaker.clean.ui.ARTWORK_URL_512_EXTRA
import com.practicum.playlistmaker.clean.ui.COLLECTION_NAME_EXTRA
import com.practicum.playlistmaker.clean.ui.COUNTRY_EXTRA
import com.practicum.playlistmaker.clean.ui.PREVIEW_URL_EXTRA
import com.practicum.playlistmaker.clean.ui.PRIMARY_GENRE_NAME_EXTRA
import com.practicum.playlistmaker.clean.ui.RELEASE_YEAR_EXTRA
import com.practicum.playlistmaker.clean.ui.TRACK_NAME_EXTRA
import com.practicum.playlistmaker.clean.ui.TRACK_TIME_EXTRA
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker.clean.presentation.dpToPx

class PlayerActivity : AppCompatActivity() {

    private var player: Player? = null

    private lateinit var binding: ActivityPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbarSettings = findViewById<Toolbar>(R.id.toolbar)
        toolbarSettings.setOnClickListener {
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
        previewUrl?.let {
            player = Creator.providePlayer(binding.playButton, binding.playtime)
            player?.prepare(it)
        }

        binding.playButton.setOnClickListener {
            if (previewUrl == null) {
                Toast.makeText(this, getString(R.string.track_not_available), Toast.LENGTH_SHORT).show()
            } else {
                player?.switchBetweenPlayAndPause()
            }
        }
    }

    override fun onPause() {
        player?.pause()
        super.onPause()
    }

    override fun onDestroy() {
        player?.release()
        super.onDestroy()
    }

    companion object {
        const val UNKNOWN_TRACK_NAME = "Track Unknown"
        const val UNKNOWN_ARTIST_NAME = "Artist Unknown"
        const val UNKNOWN_VALUE = "-"

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