package com.practicum.playlistmaker.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toolbar
import com.google.android.material.switchmaterial.SwitchMaterial
import com.practicum.playlistmaker.preferences.App
import com.practicum.playlistmaker.R

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val toolbarSettings = findViewById<Toolbar>(R.id.toolbarSettings)
        toolbarSettings.setOnClickListener {
            finish()
        }

        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)
        themeSwitcher.isChecked = (applicationContext as App).darkTheme
        themeSwitcher.setOnCheckedChangeListener { _, isChecked ->
            (applicationContext as App).switchTheme(isChecked)
        }

        val textViewShareApp = findViewById<TextView>(R.id.textViewShareApp)
        textViewShareApp.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.setType("text/plain")
            shareIntent.putExtra(
                Intent.EXTRA_TEXT,
                getString(R.string.ya_practicum_android_course_url)
            )
            startActivity(shareIntent)
        }

        val textViewSupport = findViewById<TextView>(R.id.textViewSupport)
        textViewSupport.setOnClickListener {
            val sendToAddresses = arrayOf(
                getString(R.string.developer_email)
            )

            val sendMailIntent = Intent(Intent.ACTION_SENDTO)
            sendMailIntent.setData(Uri.parse("mailto:"))
            sendMailIntent.putExtra(Intent.EXTRA_EMAIL, sendToAddresses)
            sendMailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_email_subject))
            sendMailIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.support_email_body))
            startActivity(sendMailIntent)
        }

        val textViewLicenseAgreement = findViewById<TextView>(R.id.textViewLicenseAgreement)
        textViewLicenseAgreement.setOnClickListener {
            val uri = Uri.parse(getString(R.string.ya_practicum_license_agreement_url))
            val browseIntent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(browseIntent)
        }
    }
}