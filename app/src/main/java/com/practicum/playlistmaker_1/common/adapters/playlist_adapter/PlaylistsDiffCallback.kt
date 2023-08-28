package com.practicum.playlistmaker_1.common.adapters.playlist_adapter

import androidx.recyclerview.widget.DiffUtil
import com.practicum.playlistmaker_1.media_library.domain.models.PlaylistModel

class PlaylistsDiffCallback(
    private val oldList: List<PlaylistModel>,
    private val newList: List<PlaylistModel>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size
    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldTrack = oldList[oldItemPosition]
        val newTrack = newList[oldItemPosition]
        return oldTrack.id == newTrack.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldTrack = oldList[oldItemPosition]
        val newTrack = newList[oldItemPosition]
        return oldTrack == newTrack
    }
}