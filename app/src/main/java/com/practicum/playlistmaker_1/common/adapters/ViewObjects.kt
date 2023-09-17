package com.practicum.playlistmaker_1.common.adapters

sealed class ViewObjects {
    object Horizontal: ViewObjects()
    object Vertical: ViewObjects()
    object VerticalTracks: ViewObjects()
}
