package com.practicum.playlistmaker_1.media_library.ui.activity


import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker_1.R
import com.practicum.playlistmaker_1.databinding.FragmentBasePlaylistBinding
import com.practicum.playlistmaker_1.media_library.ui.viewmodel.BasePlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

abstract class BasePlaylistFragment : Fragment() {

    var _binding: FragmentBasePlaylistBinding? = null
    val binding get() = _binding!!
    var imageUri: Uri? = null

    val viewModel by viewModel<BasePlaylistViewModel>()

    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            binding.pickImage.setImageURI(uri)
            imageUri = uri
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }

    val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            //
        }
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            if (p0 != null) {
                if (p0.isNotEmpty()) {
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
        override fun afterTextChanged(p0: Editable?) {
            //
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBasePlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun showConfirmDialog() {
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
    }
}