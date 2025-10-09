package com.practicum.playlistmaker.sharing.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.practicum.playlistmaker.sharing.domain.ExternalNavigator
import com.practicum.playlistmaker.sharing.domain.model.EmailData

class ExternalNavigatorImpl(
    private val context: Context
) : ExternalNavigator {
    override fun shareText(text: String) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        shareIntent.setType("text/plain")
        shareIntent.putExtra(
            Intent.EXTRA_TEXT,
            text
        )
        context.startActivity(shareIntent)
    }

    override fun openLink(link: String) {
        val uri = Uri.parse(link)
        val browseIntent = Intent(Intent.ACTION_VIEW, uri)
        browseIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(browseIntent)
    }

    override fun openEmail(emailData: EmailData) {
        val sendMailIntent = Intent(Intent.ACTION_SENDTO)
        sendMailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        sendMailIntent.setData(Uri.parse("mailto:"))
        sendMailIntent.putExtra(Intent.EXTRA_EMAIL, emailData.sendToAddresses.toTypedArray())
        sendMailIntent.putExtra(Intent.EXTRA_SUBJECT, emailData.subject)
        sendMailIntent.putExtra(Intent.EXTRA_TEXT, emailData.body)
        context.startActivity(sendMailIntent)
    }
}