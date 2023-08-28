package com.practicum.playlistmaker_1.common.adapters.playlist_adapter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker_1.R
import com.practicum.playlistmaker_1.media_library.domain.models.PlaylistModel

class PlaylistsAdapter :
    RecyclerView.Adapter<PlaylistsViewHolder>() {

    var playlists = mutableListOf<PlaylistModel>()
        set(newPlaylists) {
            val diffCallback = PlaylistsDiffCallback(field, newPlaylists)
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            field = newPlaylists
            diffResult.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.playlist_item, parent, false)
        return PlaylistsViewHolder(view)
    }

    override fun getItemCount() = playlists.size

    override fun onBindViewHolder(holder: PlaylistsViewHolder, position: Int) {
        holder.bind(playlists[position])
        holder.adapterPosition
    }

    fun clearPlaylists() {
        playlists = ArrayList()
    }
}