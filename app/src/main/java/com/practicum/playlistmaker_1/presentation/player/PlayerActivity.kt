package com.practicum.playlistmaker_1.presentation.player

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore.Audio.AudioColumns.TRACK
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.practicum.playlistmaker_1.R
import com.practicum.playlistmaker_1.domain.impl.AudioPlayerInteractorImpl
import com.practicum.playlistmaker_1.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker_1.domain.models.Track
import java.text.SimpleDateFormat
import java.util.*

class PlayerActivity : AppCompatActivity() {

    private val audioPlayerInteractor = AudioPlayerInteractorImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        audioPlayerInteractor.playerBinding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(audioPlayerInteractor.playerBinding.root)

        audioPlayerInteractor.playerBinding.toolbarInclude.toolbar.apply {
            title = ""
            setSupportActionBar(this)
            setNavigationOnClickListener {
                finish()
            }
        }

        val track = Gson().fromJson(intent.getStringExtra(TRACK), Track::class.java)

        Glide
            .with(audioPlayerInteractor.playerBinding.mediaTrackImage)
            .load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.corner_radius_8)))
            .into(audioPlayerInteractor.playerBinding.mediaTrackImage)

        audioPlayerInteractor.playerBinding.trackName.text = track.trackName
        audioPlayerInteractor.playerBinding.artistName.text = track.artistName
        audioPlayerInteractor.playerBinding.primaryGenreName.text = track.primaryGenreName
        audioPlayerInteractor.playerBinding.country.text = track.country

        audioPlayerInteractor.playerBinding.trackTime.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)

        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(track.releaseDate)
        if (date != null) {
            val formattedDatesString = SimpleDateFormat("yyyy", Locale.getDefault()).format(date)
            audioPlayerInteractor.playerBinding.releaseDate.text = formattedDatesString
        }

        if (track.collectionName.isNotEmpty()) {
            audioPlayerInteractor.playerBinding.collectionName.text = track.collectionName
        } else {
            audioPlayerInteractor.playerBinding.collectionName.visibility = View.GONE
            audioPlayerInteractor.playerBinding.trackAlbum.visibility = View.GONE
        }

        track?.previewUrl?.let { audioPlayerInteractor.preparePlayer(it) }

        audioPlayerInteractor.playerBinding.playButton.setOnClickListener {
            audioPlayerInteractor.playbackControl()
        }
    }

    override fun onPause() {
        super.onPause()
        audioPlayerInteractor.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        audioPlayerInteractor.mediaPlayer.release()
        audioPlayerInteractor.handler.removeCallbacks(audioPlayerInteractor.updateTimer())
    }

    companion object {
        const val PLAYTIME_DELAY = 250L
        const val CURRENT_TIME_ZERO = "00:00"
    }
}