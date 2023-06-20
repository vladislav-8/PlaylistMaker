package com.practicum.playlistmaker_1.util

import android.icu.text.SimpleDateFormat
import java.util.Locale

fun Long.formatAsTime(): String {
    return SimpleDateFormat("mm:ss", Locale.getDefault()).format(this)
}