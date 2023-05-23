package com.practicum.playlistmaker_1.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore.Audio.AudioColumns.TRACK
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.practicum.playlistmaker_1.R
import com.practicum.playlistmaker_1.data.repository.PlayerInteractorRepositoryImpl
import com.practicum.playlistmaker_1.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker_1.domain.models.PlayerState
import com.practicum.playlistmaker_1.domain.models.Track
import com.practicum.playlistmaker_1.domain.use_case.PlayerInteractor
import java.text.SimpleDateFormat
import java.util.*

class PlayerActivity : AppCompatActivity() {

    private val interactor = PlayerInteractorRepositoryImpl()
    private val player = PlayerInteractor(interactor)
    private val handler = Handler(Looper.getMainLooper())
    private val time = startTimer()

    private val playerBinding: ActivityPlayerBinding by lazy {
        ActivityPlayerBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(playerBinding.root)

        interactor.setOnStateChangeListener { state ->
            playerBinding.playButton.setOnClickListener {
                controller(state)
            }
            if (state == PlayerState.STATE_COMPLETE) {
                playerBinding.time.text = CURRENT_TIME_ZERO
                handler.removeCallbacks(time)
                setPlayIcon()
            }
        }

        playerBinding.toolbarInclude.toolbar.apply {
            title = ""
            setSupportActionBar(this)
            setNavigationOnClickListener {
                finish()
            }
        }

        val track = Gson().fromJson(intent.getStringExtra(TRACK), Track::class.java)

        Glide
            .with(playerBinding.mediaTrackImage)
            .load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.corner_radius_8)))
            .into(playerBinding.mediaTrackImage)

        playerBinding.trackName.text = track.trackName
        playerBinding.artistName.text = track.artistName
        playerBinding.primaryGenreName.text = track.primaryGenreName
        playerBinding.country.text = track.country

        playerBinding.trackTime.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)

        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(track.releaseDate)
        if (date != null) {
            val formattedDatesString = SimpleDateFormat("yyyy", Locale.getDefault()).format(date)
            playerBinding.releaseDate.text = formattedDatesString
        }

        if (track.collectionName.isNotEmpty()) {
            playerBinding.collectionName.text = track.collectionName
        } else {
            playerBinding.collectionName.visibility = View.GONE
            playerBinding.trackAlbum.visibility = View.GONE
        }

        player.preparePlayer(track.previewUrl)

    }

    private fun controller(state: PlayerState) {
        when (state) {
            PlayerState.STATE_PREPARED, PlayerState.STATE_COMPLETE, PlayerState.STATE_PAUSED -> {
                player.startPlayer()
                setPauseIcon()
                handler.post(time)
            }

            PlayerState.STATE_PLAYING -> {
                player.pausePlayer()
                setPlayIcon()
                handler.removeCallbacks(time)
            }
        }
    }

    private fun setPlayIcon() {
            playerBinding.playButton.setImageResource(R.drawable.ic_play)
    }

    private fun setPauseIcon() {
        playerBinding.playButton.setImageResource(R.drawable.ic_pause)
    }

    override fun onPause() {
        super.onPause()
        player.pausePlayer()
        handler.removeCallbacks(time)
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
        handler.removeCallbacks(time)
    }

    private fun startTimer(): Runnable {
        return object : Runnable {
            override fun run() {
                val position = player.getPosition()
                playerBinding.time.text = TimeFormat.formatTime(position)
                handler.postDelayed(this, PLAYTIME_DELAY)
            }
        }
    }

    object TimeFormat {
        fun formatTime(time: Long): String {
            return SimpleDateFormat(
                "mm:ss",
                Locale.getDefault()
            ).format(time)
        }
    }

    companion object {
        const val PLAYTIME_DELAY = 250L
        const val CURRENT_TIME_ZERO = "00:00"
    }
}
