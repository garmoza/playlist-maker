package com.practicum.playlistmaker.settings.ui.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding
import com.practicum.playlistmaker.settings.domain.model.ThemeMode
import com.practicum.playlistmaker.settings.ui.view_model.SettingsViewModel
import com.practicum.playlistmaker.sharing.domain.model.EmailData

class SettingsActivity : AppCompatActivity() {

    private val viewModel by viewModels<SettingsViewModel> { SettingsViewModel.getViewModelFactory() }

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarSettings.setOnClickListener {
            finish()
        }

        viewModel.getThemeSettingsLiveData().observe(this) { state ->
            binding.themeSwitcher.isChecked = (state.themeMode == ThemeMode.DARK)
        }
        binding.themeSwitcher.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setThemeMode(when (isChecked) {
                true -> ThemeMode.DARK
                false -> ThemeMode.LIGHT
            })
        }

        binding.textViewShareApp.setOnClickListener {
            viewModel.shareApp(getString(R.string.ya_practicum_android_course_url))
        }

        binding.textViewSupport.setOnClickListener {
            viewModel.openSupport(EmailData(
                sendToAddresses = listOf(getString(R.string.developer_email)),
                subject = getString(R.string.support_email_subject),
                body = getString(R.string.support_email_body)
            ))
        }

        binding.textViewLicenseAgreement.setOnClickListener {
            viewModel.openTerms(getString(R.string.ya_practicum_license_agreement_url))
        }
    }
}