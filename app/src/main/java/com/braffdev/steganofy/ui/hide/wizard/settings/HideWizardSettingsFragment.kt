package com.braffdev.steganofy.ui.hide.wizard.settings

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.braffdev.steganofy.R
import com.braffdev.steganofy.databinding.HideWizardSettingsFragmentBinding
import com.braffdev.steganofy.ui.common.TextWatcherAdapter
import com.braffdev.steganofy.ui.common.VisibilityUtils
import com.braffdev.steganofy.ui.common.message.MessageFragment
import org.koin.android.scope.ScopeActivity

class HideWizardSettingsFragment : Fragment() {

    companion object {
        fun newInstance() = HideWizardSettingsFragment()
    }

    private lateinit var binding: HideWizardSettingsFragmentBinding
    private val viewModel: HideWizardSettingsViewModel by lazy { (requireActivity() as ScopeActivity).scope.get() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = HideWizardSettingsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.liveDataStart.observe(this) { start(it) }
        viewModel.liveDataStartButtonEnabled.observe(this) { binding.buttonStart.isEnabled = it }
        viewModel.liveDataEncryptionPasswordShown.observe(this) {
            binding.editTextEncryptionPasswordLayout.visibility = VisibilityUtils.toVisibility(it)
        }
        viewModel.liveDataShowFileTooSmall.observe(this) {
            if (it) {
                requireFragmentManager().beginTransaction()
                    .replace(R.id.containerMessage, MessageFragment.createErrorMessage(R.string.hide_image_too_small))
                    .commit()
            } else {
                requireFragmentManager().findFragmentById(R.id.containerMessage)
                    ?.let { fragment -> requireFragmentManager().beginTransaction().remove(fragment).commit() }
            }
        }

        binding.previous.setOnClickListener { viewModel.previousClicked() }
        binding.buttonStart.setOnClickListener { viewModel.start() }
        binding.radioGroupEncryption.setOnCheckedChangeListener { _, checkedId -> viewModel.radioButtonChecked(checkedId) }
        binding.editTextEncryptionPassword.addTextChangedListener(object : TextWatcherAdapter() {
            override fun afterTextChanged(s: Editable?) {
                viewModel.encryptionPasswordChanged(s.toString().toCharArray())
            }
        })
    }

    private fun start(intent: Intent) {
        startActivity(intent)
        requireActivity().finish()
    }
}