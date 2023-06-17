package com.practicum.playlistmaker_1.player.ui.models

sealed class NavigationState {
    object SearchScreen: NavigationState()
    object MediaLibraryScreen: NavigationState()
    object SettingsScreen: NavigationState()
}
