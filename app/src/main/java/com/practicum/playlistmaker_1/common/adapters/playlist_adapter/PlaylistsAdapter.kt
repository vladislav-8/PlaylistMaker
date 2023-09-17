package com.practicum.playlistmaker_1.common.adapters.playlist_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker_1.R
import com.practicum.playlistmaker_1.common.adapters.ViewObjects
import com.practicum.playlistmaker_1.media_library.domain.models.Playlist

class PlaylistsAdapter(private val viewObject: ViewObjects) :
    RecyclerView.Adapter<PlaylistsViewHolder>() {

    var onPlayListClicked: ((playlist: Playlist) -> Unit)? = null

    var playlists = mutableListOf<Playlist>()
        set(newPlaylists) {
            val diffCallback = PlaylistsDiffCallback(field, newPlaylists)
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            field = newPlaylists
            diffResult.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistsViewHolder {
        return if (viewObject == ViewObjects.Horizontal) {
            PlaylistsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.playlist_item, parent, false))
        } else {
            PlaylistsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.bottom_sheet_playlists_item, parent, false))
        }
    }

    override fun getItemCount() = playlists.size

    override fun onBindViewHolder(holder: PlaylistsViewHolder, position: Int) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener {
            onPlayListClicked?.invoke(playlists[holder.adapterPosition])
        }
    }
}