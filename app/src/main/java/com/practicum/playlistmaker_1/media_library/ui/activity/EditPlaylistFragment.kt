package com.practicum.playlistmaker_1.media_library.ui.activity

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.practicum.playlistmaker_1.R
import com.practicum.playlistmaker_1.databinding.FragmentBasePlaylistBinding
import com.practicum.playlistmaker_1.media_library.domain.models.Playlist
import java.io.Serializable

class EditPlaylistFragment : BasePlaylistFragment() {

    var playlist: Playlist? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBasePlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        playlist = requireArguments().getSerializableExtra(EDIT_PLAYLIST, Playlist::class.java)
        Log.d("TAG", "$playlist")


        with(binding) {
            editTextPlaylistTitle.setText(playlist?.title)
            editTextPlaylistDescription.setText(playlist?.description)
            imageUri = playlist?.imageUri

            if (playlist?.imageUri.toString() != "null") {
                binding.pickImage.setImageURI(playlist?.imageUri)
            } else {
                context?.let {
                    Glide
                        .with(it)
                        .load(R.drawable.placeholder)
                        .transform(CenterInside())
                        .into(binding.pickImage)
                }
            }
        }

        binding.buttonCreatePlaylist.text = getString(R.string.save)
        binding.newPlaylistToolbar.title = getString(R.string.editing)

        binding.buttonCreatePlaylist.setOnClickListener {
            if (playlist != null) {
                val updatedPlaylist = Playlist(
                    id = playlist!!.id,
                    title = binding.editTextPlaylistTitle.text.toString(),
                    description = binding.editTextPlaylistDescription.text.toString(),
                    imageUri = imageUri,
                    trackList = playlist?.trackList!!,
                    size = playlist?.size!!
                )
                viewModel.updatePlaylist(updatedPlaylist)
                imageUri?.let { viewModel.saveToLocalStorage(uri = it) }
            }

            findNavController().navigateUp()
        }

        binding.editTextPlaylistTitle.addTextChangedListener(textWatcher)

        binding.newPlaylistToolbar.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.pickImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun <T : Serializable?> Bundle.getSerializableExtra(key: String, m_class: Class<T>): T {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            this.getSerializable(key, m_class)!!
        else
            this.getSerializable(key) as T
    }

    companion object {
        const val EDIT_PLAYLIST = "EDIT_PLAYLIST"

        fun createArgs(playlist: Playlist): Bundle {
            return bundleOf(EDIT_PLAYLIST to playlist)
        }
    }
}