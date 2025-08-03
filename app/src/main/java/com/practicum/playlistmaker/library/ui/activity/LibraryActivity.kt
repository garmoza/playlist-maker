package com.practicum.playlistmaker.library.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityLibraryBinding

class LibraryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLibraryBinding
    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarLibrary.setOnClickListener {
            finish()
        }

        val adapter = FavoritesTracksPagerAdapter(
            fragmentManager = supportFragmentManager,
            lifecycle = lifecycle
        )
        binding.viewPager.adapter = adapter
        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) {
            tab, position -> tab.text = when (position) {
                0 -> getString(R.string.favorites_tracks)
                else -> getString(R.string.playlists)
            }
        }
        tabMediator.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}