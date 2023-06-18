package com.practicum.playlistmaker_1.sharing.data.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.practicum.playlistmaker_1.R
import com.practicum.playlistmaker_1.sharing.data.ExternalNavigator
import com.practicum.playlistmaker_1.sharing.domain.models.EmailData

class ExternalNavigatorImpl (private val context: Context) : ExternalNavigator {

    val emailSubject = context.getString(R.string.subject)
    val emailText = context.getString(R.string.message)

    override fun shareLink(shareAppLink: String) {
        Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareAppLink)
            type = "text/plain"
            context.startActivity(Intent.createChooser(this, null))
        }
    }

    override fun openLink(termsLink: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(termsLink))
        context.startActivity(browserIntent)
    }

    override fun openEmail(supportEmailData: EmailData) {
        Intent().apply {
            action = Intent.ACTION_SENDTO
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(supportEmailData.mail))
            putExtra(Intent.EXTRA_SUBJECT, emailSubject)
            putExtra(Intent.EXTRA_TEXT, emailText)
            context.startActivity(this)
        }
    }
}