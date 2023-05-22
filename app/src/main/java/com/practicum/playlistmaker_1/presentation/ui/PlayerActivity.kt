package com.practicum.playlistmaker_1.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore.Audio.AudioColumns.TRACK
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.practicum.playlistmaker_1.R
import com.practicum.playlistmaker_1.data.AudioPlayerRepository
import com.practicum.playlistmaker_1.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker_1.domain.models.Track
import java.text.SimpleDateFormat
import java.util.*

class PlayerActivity : AppCompatActivity() {

    private val audioPlayerRepository = AudioPlayerRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        audioPlayerRepository.playerBinding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(audioPlayerRepository.playerBinding.root)

        audioPlayerRepository.playerBinding.toolbarInclude.toolbar.apply {
            title = ""
            setSupportActionBar(this)
            setNavigationOnClickListener {
                finish()
            }
        }

        val track = Gson().fromJson(intent.getStringExtra(TRACK), Track::class.java)

        Glide
            .with(audioPlayerRepository.playerBinding.mediaTrackImage)
            .load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.corner_radius_8)))
            .into(audioPlayerRepository.playerBinding.mediaTrackImage)

        audioPlayerRepository.playerBinding.trackName.text = track.trackName
        audioPlayerRepository.playerBinding.artistName.text = track.artistName
        audioPlayerRepository.playerBinding.primaryGenreName.text = track.primaryGenreName
        audioPlayerRepository.playerBinding.country.text = track.country

        audioPlayerRepository.playerBinding.trackTime.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)

        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(track.releaseDate)
        if (date != null) {
            val formattedDatesString = SimpleDateFormat("yyyy", Locale.getDefault()).format(date)
            audioPlayerRepository.playerBinding.releaseDate.text = formattedDatesString
        }

        if (track.collectionName.isNotEmpty()) {
            audioPlayerRepository.playerBinding.collectionName.text = track.collectionName
        } else {
            audioPlayerRepository.playerBinding.collectionName.visibility = View.GONE
            audioPlayerRepository.playerBinding.trackAlbum.visibility = View.GONE
        }

        track?.previewUrl?.let { audioPlayerRepository.preparePlayer(it) }

        audioPlayerRepository.playerBinding.playButton.setOnClickListener {
            audioPlayerRepository.playbackControl()
        }
    }

    override fun onPause() {
        super.onPause()
        audioPlayerRepository.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        audioPlayerRepository.mediaPlayer.release()
        audioPlayerRepository.handler.removeCallbacks(audioPlayerRepository.updateTimer())
    }

    companion object {
        const val PLAYTIME_DELAY = 250L
        const val CURRENT_TIME_ZERO = "00:00"
    }
}