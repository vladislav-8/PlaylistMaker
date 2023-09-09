package com.practicum.playlistmaker_1.common.adapters.tracks_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker_1.R
import com.practicum.playlistmaker_1.search.domain.models.Track

class TrackAdapter(
    private val clickListener: TrackClickListener,
    private val longClick: LongTrackClickListener
) :
    RecyclerView.Adapter<TrackViewHolder>() {

    var trackOnLongClickListener: ((Track, Int) -> Unit)? = null

    var tracks = mutableListOf<Track>()
        set(newTracks) {
            val diffCallback = TracksDiffCallback(field, newTracks)
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            field = newTracks
            diffResult.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return TrackViewHolder(view)
    }

    override fun getItemCount() = tracks.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            clickListener.onTrackClick(tracks[holder.adapterPosition])
        }
        holder.itemView.setOnLongClickListener {
            longClick.onTrackLongClick(tracks[holder.adapterPosition])
            true
        }
    }

    fun interface TrackClickListener {
        fun onTrackClick(track: Track)
    }

    fun interface LongTrackClickListener {
        fun onTrackLongClick(track: Track)
    }

    fun clearTracks() {
        tracks = ArrayList()
    }
}