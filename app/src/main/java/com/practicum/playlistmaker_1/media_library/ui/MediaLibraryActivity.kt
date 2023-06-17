package com.practicum.playlistmaker_1.media_library.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.practicum.playlistmaker_1.databinding.ActivityMediaLibraryBinding

class MediaLibraryActivity : AppCompatActivity() {

    private lateinit var mediaBinding: ActivityMediaLibraryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mediaBinding = ActivityMediaLibraryBinding.inflate(layoutInflater)
        setContentView(mediaBinding.root)
    }
}