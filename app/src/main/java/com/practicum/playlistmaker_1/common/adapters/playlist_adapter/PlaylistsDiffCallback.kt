package com.practicum.playlistmaker_1.common.adapters.playlist_adapter

import androidx.recyclerview.widget.DiffUtil
import com.practicum.playlistmaker_1.media_library.domain.models.Playlist

class PlaylistsDiffCallback(
    private val oldList: List<Playlist>,
    private val newList: List<Playlist>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size
    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldPlaylist = oldList[oldItemPosition]
        val newPlaylist = newList[newItemPosition]
        return oldPlaylist.id == newPlaylist.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldPlaylist = oldList[oldItemPosition]
        val newPlaylist = newList[newItemPosition]
        return oldPlaylist == newPlaylist
    }
}