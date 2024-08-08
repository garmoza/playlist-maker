package com.practicum.playlistmaker.activity

import android.os.Bundle
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.R

class PlayerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val toolbarSettings = findViewById<Toolbar>(R.id.toolbar)
        toolbarSettings.setOnClickListener {
            finish()
        }
    }
}