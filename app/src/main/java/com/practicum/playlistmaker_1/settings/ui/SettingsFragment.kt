package com.practicum.playlistmaker_1.settings.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.practicum.playlistmaker_1.databinding.FragmentSettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private lateinit var settingsBinding: FragmentSettingsBinding
    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        settingsBinding = FragmentSettingsBinding.inflate(inflater, container, false)
        return settingsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
        viewModel.themeSettingsState.observe(viewLifecycleOwner) { themeSettings ->
            settingsBinding.switch1.isChecked = themeSettings.darkTheme
        }
    }

    private fun initListeners() {
        settingsBinding.shareTextView.setOnClickListener {
            viewModel.shareApp()
        }
        settingsBinding.callTextView.setOnClickListener {
            viewModel.supportEmail()
        }
        settingsBinding.agreementTextView.setOnClickListener {
            viewModel.openAgreement()
        }
        settingsBinding.switch1.setOnCheckedChangeListener { _, checked ->
            viewModel.switchTheme(checked)
        }
    }
}