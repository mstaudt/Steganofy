package com.braffdev.steganofy.ui.reveal.wizard

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import com.braffdev.steganofy.R
import com.braffdev.steganofy.databinding.RevealWizardActivityBinding
import com.braffdev.steganofy.ui.common.TextWatcherAdapter
import com.braffdev.steganofy.ui.common.VisibilityUtils
import com.braffdev.steganofy.ui.common.file.picker.FilePickedListener
import com.braffdev.steganofy.ui.common.file.picker.FilePickerFragment
import com.braffdev.steganofy.ui.common.message.MessageFragment
import com.braffdev.steganofy.ui.reveal.wizard.info.SteganoInfoFragment
import org.koin.android.scope.ScopeActivity

class RevealWizardActivity : ScopeActivity() {

    private lateinit var binding: RevealWizardActivityBinding
    private val viewModel: RevealWizardViewModel by scope.inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = RevealWizardActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        setTitle(R.string.main_reveal)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_close_24)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        viewModel.liveDataStartEnabled.observe(this) { binding.buttonStart.isEnabled = it }
        viewModel.liveDataStart.observe(this) { start(it) }
        viewModel.liveDataDecryptionPasswordShown.observe(this) {
            binding.editTextDecryptionPasswordLayout.visibility = VisibilityUtils.toVisibility(it)
        }
        viewModel.liveDataInputState.observe(this) { showInputState(it) }

        val filePickerFragment = FilePickerFragment.newImageInstance()
        filePickerFragment.listener = object : FilePickedListener {
            override fun onFileChanged(uri: Uri) {
                viewModel.imageUriChanged(uri)
            }
        }

        supportFragmentManager.beginTransaction().replace(R.id.containerFilePickerFragment, filePickerFragment).commit()
        binding.buttonStart.setOnClickListener { viewModel.startClicked() }
        binding.editTextDecryptionPassword.addTextChangedListener(object : TextWatcherAdapter() {
            override fun afterTextChanged(s: Editable?) {
                viewModel.decryptionPasswordChanged(s.toString().toCharArray())
            }
        })
    }

    private fun showInputState(state: RevealWizardState) {
        if (state == RevealWizardState.NO_INPUT) {
            // hide any message
            supportFragmentManager.findFragmentById(R.id.containerMessage)
                ?.let { containedFragment -> supportFragmentManager.beginTransaction().remove(containedFragment).commit() }
        } else {
            val fragment = if (state == RevealWizardState.INPUT_INVALID) {
                MessageFragment.createErrorMessage(R.string.reveal_file_invalid)
            } else {
                SteganoInfoFragment.newInstance(viewModel.steganoInfo!!)
            }

            supportFragmentManager.beginTransaction().replace(R.id.containerMessage, fragment).commit()
        }
    }

    private fun start(intent: Intent) {
        startActivity(intent)
        finish()
    }
}