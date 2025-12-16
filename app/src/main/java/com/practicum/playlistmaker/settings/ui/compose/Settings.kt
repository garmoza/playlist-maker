package com.practicum.playlistmaker.settings.ui.compose

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.common.ui.compose.Toolbar
import com.practicum.playlistmaker.main.ui.theme.Theme
import com.practicum.playlistmaker.settings.domain.model.ThemeMode
import com.practicum.playlistmaker.settings.domain.model.ThemeSettingsState
import com.practicum.playlistmaker.settings.ui.view_model.SettingsViewModel
import com.practicum.playlistmaker.sharing.domain.model.EmailData
import org.koin.androidx.compose.koinViewModel

@Composable
fun Settings() {
    val viewModel: SettingsViewModel = koinViewModel()

    val themeSettingsState by viewModel.getThemeSettingsLiveData().observeAsState()

    Settings(
        themeSettingsState = themeSettingsState,
        onThemeSwitch = viewModel::setThemeMode,
        onShareAppClick = viewModel::shareApp,
        onSupportClick = viewModel::openSupport,
        onLicenseAgreementClick = viewModel::openTerms
    )
}

@Composable
private fun Settings(
    themeSettingsState: ThemeSettingsState?,
    onThemeSwitch: (themeMode: ThemeMode) -> Unit,
    onShareAppClick: (link: String) -> Unit,
    onSupportClick: (emailData: EmailData) -> Unit,
    onLicenseAgreementClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Toolbar(R.string.settings)
        Spacer(modifier = Modifier.padding(12.dp))
        ThemeSwitchMenuItem(themeSettingsState, onThemeSwitch)
        ShareAppMenuItem(onShareAppClick)
        SupportMenuItem(onSupportClick)
        LicenseAgreementMenuItem(onLicenseAgreementClick)
    }
}

@Composable
private fun ThemeSwitchMenuItem(
    themeSettingsState: ThemeSettingsState?,
    onThemeSwitch: (themeMode: ThemeMode) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp, 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.dark_theme),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Switch(
            checked = themeSettingsState?.themeMode == ThemeMode.DARK,
            onCheckedChange = {
                onThemeSwitch(if (themeSettingsState?.themeMode == ThemeMode.DARK) {
                    ThemeMode.LIGHT
                } else {
                    ThemeMode.DARK
                })
            }
        )
    }
}

@Composable
private fun ShareAppMenuItem(onShareAppClick: (link: String) -> Unit) {
    val courseUrl = stringResource(R.string.ya_practicum_android_course_url)

    SettingsMenuItem(
        labelId = R.string.share_app,
        iconId = R.drawable.ic_share,
        onClick = { onShareAppClick(courseUrl) }
    )
}

@Composable
private fun SupportMenuItem(onSupportClick: (emailData: EmailData) -> Unit) {
    val developerEmail = stringResource(R.string.developer_email)
    val subject = stringResource(R.string.support_email_subject)
    val body = stringResource(R.string.support_email_body)

    SettingsMenuItem(
        labelId = R.string.write_to_support,
        iconId = R.drawable.ic_support,
        onClick = { onSupportClick(
            EmailData(
                sendToAddresses = listOf(developerEmail),
                subject = subject,
                body = body
            )
        ) }
    )
}

@Composable
private fun LicenseAgreementMenuItem(onLicenseAgreementClick: (String) -> Unit) {
    val licenseAgreementUrl = stringResource(R.string.ya_practicum_license_agreement_url)

    SettingsMenuItem(
        labelId = R.string.license_agreement,
        iconId = R.drawable.ic_arrow_forward,
        onClick = { onLicenseAgreementClick(licenseAgreementUrl) }
    )
}

@Composable
private fun SettingsMenuItem(
    @StringRes labelId: Int,
    @DrawableRes iconId: Int,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { onClick() }
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp, 21.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(labelId),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Icon(
            painter = painterResource(iconId),
            contentDescription = stringResource(labelId),
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun SettingsPreviewLightTheme() {
    Theme(darkTheme = false) {
        Settings(
            themeSettingsState = ThemeSettingsState(ThemeMode.LIGHT),
            onThemeSwitch = {},
            onShareAppClick = {},
            onSupportClick = {},
            onLicenseAgreementClick = {}
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun SettingsPreviewDarkTheme() {
    Theme(darkTheme = true) {
        Settings(
            themeSettingsState = ThemeSettingsState(ThemeMode.DARK),
            onThemeSwitch = {},
            onShareAppClick = {},
            onSupportClick = {},
            onLicenseAgreementClick = {}
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun SettingsPreview() {
    Settings(
        themeSettingsState = null,
        onThemeSwitch = {},
        onShareAppClick = {},
        onSupportClick = {},
        onLicenseAgreementClick = {}
    )
}