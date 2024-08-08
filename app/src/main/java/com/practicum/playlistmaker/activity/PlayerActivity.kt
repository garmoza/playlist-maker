package com.practicum.playlistmaker.activity

import android.os.Bundle
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker.dpToPx

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbarSettings = findViewById<Toolbar>(R.id.toolbar)
        toolbarSettings.setOnClickListener {
            finish()
        }

        val artworkUrl512 = intent.getStringExtra("artworkUrl512")
        artworkUrl512?.let {
            Glide.with(this)
                .load(it)
                .placeholder(R.drawable.placeholder_track_label)
                .transform(RoundedCorners(dpToPx(8F, this)))
                .into(binding.trackLabel)
        }

        val trackName = intent.getStringExtra("trackName")?: UNKNOWN_TRACK_NAME
        val artistName = intent.getStringExtra("artistName")?: UNKNOWN_ARTIST_NAME
        val collectionName = intent.getStringExtra("collectionName")?: UNKNOWN_VALUE
        val releaseYear = intent.getStringExtra("releaseYear")?: UNKNOWN_VALUE
        val primaryGenreName = intent.getStringExtra("primaryGenreName")?: UNKNOWN_VALUE
        val country = intent.getStringExtra("country")?: UNKNOWN_VALUE
        val trackTime = intent.getStringExtra("trackTime")?: UNKNOWN_VALUE
        with(binding) {
            this.trackName.text = trackName
            this.artistName.text = artistName
            durationValue.text = trackTime
            albumValue.text = collectionName
            yearValue.text = releaseYear
            genreValue.text = primaryGenreName
            countryValue.text = country
        }
    }

    companion object {
        const val UNKNOWN_TRACK_NAME = "Track Unknown"
        const val UNKNOWN_ARTIST_NAME = "Artist Unknown"
        const val UNKNOWN_VALUE = "-"
    }
}