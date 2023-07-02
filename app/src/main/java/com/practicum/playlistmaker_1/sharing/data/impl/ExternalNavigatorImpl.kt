package com.practicum.playlistmaker_1.sharing.data.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import com.practicum.playlistmaker_1.R
import com.practicum.playlistmaker_1.sharing.data.ExternalNavigator
import com.practicum.playlistmaker_1.sharing.domain.models.EmailData

class ExternalNavigatorImpl (private val context: Context) : ExternalNavigator {

    private val emailSubject = context.getString(R.string.subject)
    private val emailText = context.getString(R.string.message)

    override fun shareLink(shareAppLink: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareAppLink)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(context, intent, null)
    }

    override fun openLink(termsLink: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(termsLink)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(context, intent, null)
    }

    override fun openEmail(supportEmailData: EmailData) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(supportEmailData.mail))
            putExtra(Intent.EXTRA_SUBJECT, emailSubject)
            putExtra(Intent.EXTRA_TEXT, emailText)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(context, intent, null)
    }
}