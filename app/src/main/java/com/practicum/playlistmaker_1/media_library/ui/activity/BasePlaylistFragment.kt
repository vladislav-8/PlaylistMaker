package com.practicum.playlistmaker_1.media_library.ui.activity


import android.app.Activity.RESULT_OK
import android.content.Intent
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


    val viewModel by viewModel<BasePlaylistViewModel>()



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
            if (p0?.isNotEmpty()!!) {
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBasePlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //if ok user selected a file
        if (resultCode == RESULT_OK) {
            val sourceTreeUri = data?.data
            if (sourceTreeUri != null) {
                getContext()?.getContentResolver()?.takePersistableUriPermission(
                    sourceTreeUri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )
            }
        }
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