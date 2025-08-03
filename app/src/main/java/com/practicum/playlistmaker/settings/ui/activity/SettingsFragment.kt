package com.practicum.playlistmaker.settings.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentSettingsBinding
import com.practicum.playlistmaker.settings.domain.model.ThemeMode
import com.practicum.playlistmaker.settings.ui.view_model.SettingsViewModel
import com.practicum.playlistmaker.sharing.domain.model.EmailData
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private val viewModel by viewModel<SettingsViewModel>()

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbarSettings.setOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.getThemeSettingsLiveData().observe(viewLifecycleOwner) { state ->
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
            viewModel.openSupport(
                EmailData(
                    sendToAddresses = listOf(getString(R.string.developer_email)),
                    subject = getString(R.string.support_email_subject),
                    body = getString(R.string.support_email_body)
                )
            )
        }

        binding.textViewLicenseAgreement.setOnClickListener {
            viewModel.openTerms(getString(R.string.ya_practicum_license_agreement_url))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}