package com.practicum.playlistmaker_1.media_library.ui.activity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker_1.R
import com.practicum.playlistmaker_1.databinding.FragmentNewPlaylistBinding
import com.practicum.playlistmaker_1.media_library.domain.models.PlaylistModel
import com.practicum.playlistmaker_1.media_library.ui.viewmodel.NewPlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream

class NewPlaylistFragment : Fragment() {

    private var _binding: FragmentNewPlaylistBinding? = null
    private val binding get() = _binding!!

    private var imageUri: Uri? = null

    private val viewModel by viewModel<NewPlaylistViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initPickMediaRegister()
        initListeners()
    }

    private fun initListeners() {

        binding.buttonCreatePlaylist.setOnClickListener {
            val playlist = PlaylistModel(
                title = binding.editTextPlaylistTitle.text.toString(),
                description = binding.editTextPlaylistDescription.text.toString(),
                imageUri = imageUri,
                trackList = "",
                size = 0
            )

            viewModel.savePlaylist(playlist)

            imageUri?.let { saveImageToPrivateStorage(uri = it) }

            Toast.makeText(
                requireContext(),
                String.format(getString(R.string.playlist_created), playlist.title),
                Toast.LENGTH_SHORT
            ).show()

            findNavController().popBackStack()
        }

        binding.newPlaylistToolbar.setOnClickListener {
            if (binding.editTextPlaylistTitle.text.toString().isNotEmpty() ||
                binding.editTextPlaylistDescription.text.toString()
                    .isNotEmpty() || imageUri != null
            )
                context?.let { context ->
                    MaterialAlertDialogBuilder(context)
                        .setTitle(R.string.stop_creating_playlist)
                        .setMessage(R.string.unsaved_data_will_be_lost)
                        .setNeutralButton(R.string.cancel) { dialog, which ->
                        }
                        .setPositiveButton(R.string.finish) { dialog, which ->
                            findNavController().popBackStack()
                        }
                        .show()
                }
            else {
                findNavController().popBackStack()
            }
        }

        binding.editTextPlaylistTitle.doOnTextChanged { text, _, _, _ ->
            if (text != null) {
                if (text.isNotEmpty()) {
                    binding.buttonCreatePlaylist.isEnabled = true
                    context?.let {
                        ContextCompat.getColor(
                            it,
                            R.color.switcher
                        )
                    }?.let {
                        binding.buttonCreatePlaylist.setBackgroundColor(
                            it
                        )
                    }
                } else {
                    binding.buttonCreatePlaylist.isEnabled = false
                    context?.let {
                        ContextCompat.getColor(
                            it,
                            R.color.main_grey_color
                        )
                    }?.let {
                        binding.buttonCreatePlaylist.setBackgroundColor(
                            it
                        )
                    }
                }
            }
        }
    }

    private fun initPickMediaRegister() {
        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    binding.pickImage.setImageURI(uri)

                    context?.let {
                        Glide.with(it)
                            .load(uri)
                            .circleCrop()
                            .transform(RoundedCorners(binding.pickImage.resources.getDimensionPixelSize(R.dimen.corner_radius_8)))
                            .into(binding.pickImage)
                    }
                    imageUri = uri
                }
            }

        binding.pickImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun saveImageToPrivateStorage(uri: Uri) {
        val filePath = File(
            requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            getString(R.string.playlists)
        )
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val file = File(filePath, "imageName")
        val inputStream = requireActivity().contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)

        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, QUALITY_IMAGE, outputStream)

        imageUri = file.toUri()
    }

    companion object {
        private const val QUALITY_IMAGE = 30
    }
}