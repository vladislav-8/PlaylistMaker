package com.practicum.playlistmaker_1.media_library.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlistmaker_1.R
import com.practicum.playlistmaker_1.databinding.ActivityMediaLibraryBinding
import com.practicum.playlistmaker_1.media_library.ui.MediaLibraryViewPagerAdapter

class MediaLibraryActivity : AppCompatActivity() {

    private lateinit var mediaBinding: ActivityMediaLibraryBinding
    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mediaBinding = ActivityMediaLibraryBinding.inflate(layoutInflater)
        setContentView(mediaBinding.root)

        mediaBinding.viewPager.adapter = MediaLibraryViewPagerAdapter(supportFragmentManager, lifecycle)

        mediaBinding.settingsToolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        tabMediator = TabLayoutMediator(mediaBinding.tabLayout, mediaBinding.viewPager) { tab, position ->
            when(position) {
                0 -> tab.text = getString(R.string.favourite_tracks)
                1 -> tab.text = getString(R.string.playlists)
            }
        }
        tabMediator.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}