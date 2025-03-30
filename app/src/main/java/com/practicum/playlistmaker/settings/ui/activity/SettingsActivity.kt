package com.practicum.playlistmaker.settings.ui.activity

import android.os.Bundle
import android.widget.TextView
import android.widget.Toolbar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.switchmaterial.SwitchMaterial
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.settings.domain.model.ThemeMode
import com.practicum.playlistmaker.settings.ui.view_model.SettingsViewModel
import com.practicum.playlistmaker.sharing.domain.model.EmailData

class SettingsActivity : AppCompatActivity() {

    private val viewModel by viewModels<SettingsViewModel> { SettingsViewModel.getViewModelFactory() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val toolbarSettings = findViewById<Toolbar>(R.id.toolbarSettings)
        toolbarSettings.setOnClickListener {
            finish()
        }

        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)
        viewModel.getThemeSettingsLiveData().observe(this) { state ->
            themeSwitcher.isChecked = (state.themeMode == ThemeMode.DARK)
        }
        themeSwitcher.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setThemeMode(when (isChecked) {
                true -> ThemeMode.DARK
                false -> ThemeMode.LIGHT
            })
        }

        val textViewShareApp = findViewById<TextView>(R.id.textViewShareApp)
        textViewShareApp.setOnClickListener {
            viewModel.shareApp(getString(R.string.ya_practicum_android_course_url))
        }

        val textViewSupport = findViewById<TextView>(R.id.textViewSupport)
        textViewSupport.setOnClickListener {
            viewModel.openSupport(EmailData(
                sendToAddresses = listOf(getString(R.string.developer_email)),
                subject = getString(R.string.support_email_subject),
                body = getString(R.string.support_email_body)
            ))
        }

        val textViewLicenseAgreement = findViewById<TextView>(R.id.textViewLicenseAgreement)
        textViewLicenseAgreement.setOnClickListener {
            viewModel.openTerms(getString(R.string.ya_practicum_license_agreement_url))
        }
    }
}