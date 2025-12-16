package com.practicum.playlistmaker.common.ui.compose

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.main.ui.theme.Theme

@Composable
fun ContentPlaceholder(
    modifier: Modifier = Modifier,
    @DrawableRes imageId: Int,
    @StringRes messageId: Int,
    @StringRes additionalMessageId: Int? = null
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(imageId),
            contentDescription = stringResource(messageId)
        )

        Text(
            modifier = Modifier.padding(16.dp),
            textAlign = TextAlign.Center,
            text = stringResource(messageId),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        additionalMessageId?.let {
            Text(
                modifier = Modifier.padding(22.dp),
                textAlign = TextAlign.Center,
                text = stringResource(it),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun ContentPlaceholderPreview() {
    Theme {
        ContentPlaceholder(
            imageId = R.drawable.track_not_found,
            messageId = R.string.your_library_is_empty,
            additionalMessageId = R.string.placeholder_aditional_message_network_problems
        )
    }
}