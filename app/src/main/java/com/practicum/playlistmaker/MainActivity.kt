package com.practicum.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonSearch = findViewById<Button>(R.id.buttonSearch)
        val onClickListener: OnClickListener = object : OnClickListener {
            override fun onClick(v: View?) {
                Toast.makeText(this@MainActivity, "Click on Search button!", Toast.LENGTH_SHORT).show()
            }
        }
        buttonSearch.setOnClickListener(onClickListener)

        val buttonLibrary = findViewById<Button>(R.id.buttonLibrary)
        buttonLibrary.setOnClickListener {
            Toast.makeText(this@MainActivity, "Click on Library button!", Toast.LENGTH_SHORT).show()
        }

        val buttonSettings = findViewById<Button>(R.id.buttonSettings)
        buttonSettings.setOnClickListener {
            Toast.makeText(this@MainActivity, "Click on Settings button!", Toast.LENGTH_SHORT).show()
        }
    }
}