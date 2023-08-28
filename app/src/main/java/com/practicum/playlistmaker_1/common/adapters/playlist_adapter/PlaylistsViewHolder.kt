package com.practicum.playlistmaker_1.common.adapters.playlist_adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker_1.R
import com.practicum.playlistmaker_1.media_library.domain.models.PlaylistModel

class PlaylistsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val title = itemView.findViewById<TextView>(R.id.playlist_title)
    private val size = itemView.findViewById<TextView>(R.id.playlist_size)
    private val image = itemView.findViewById<ImageView>(R.id.playlist_image)

    fun bind(model: PlaylistModel) {
        title.text = model.title
        size.text = pluralizeWord(model.size, "трек")

        Glide.with(itemView.context)
            .load(model.imageUri)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.corner_radius_8)))
            .into(image)
    }

    private fun pluralizeWord(number: Int, word: String): String {
        return when {
            number % 10 == 1 && number % 100 != 11 -> "$number $word"
            number % 10 in 2..4 && (number % 100 < 10 || number % 100 >= 20) -> "$number $word${if (word.endsWith('а')) "и" else "а"}"
            else -> "$number $word${if (word.endsWith('а')) "" else "ов"}"
        }
    }
}