package com.practicum.playlistmaker_1.settings.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker_1.databinding.ActivitySettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var settingsBinding: ActivitySettingsBinding
    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settingsBinding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(settingsBinding.root)

        initListeners()

        settingsBinding.settingsToolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        viewModel.themeSettingsState.observe(this) { themeSettings ->
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
