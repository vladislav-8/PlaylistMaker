package com.practicum.playlistmaker_1.sharing.domain.models

data class EmailData (
    val mail: String,
    val mailTo:String = "mailto:"
)