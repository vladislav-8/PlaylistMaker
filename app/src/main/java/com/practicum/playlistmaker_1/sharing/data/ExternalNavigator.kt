package com.practicum.playlistmaker_1.sharing.data

import com.practicum.playlistmaker_1.sharing.domain.models.EmailData

interface ExternalNavigator {
    fun shareLink(shareAppLink:String)
    fun openLink(termsLink:String)
    fun openEmail(supportEmailData: EmailData)
}