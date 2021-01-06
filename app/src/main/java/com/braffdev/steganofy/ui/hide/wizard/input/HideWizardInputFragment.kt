package com.braffdev.steganofy.ui.hide.wizard.input

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.braffdev.steganofy.R
import com.braffdev.steganofy.databinding.HideWizardInputFragmentBinding
import com.braffdev.steganofy.ui.common.TextWatcherAdapter
import com.braffdev.steganofy.ui.common.VisibilityUtils
import com.braffdev.steganofy.ui.common.file.picker.FilePickedListener
import com.braffdev.steganofy.ui.common.file.picker.FilePickerFragment
import org.koin.android.scope.ScopeActivity


class HideWizardInputFragment : Fragment() {

    private lateinit var binding: HideWizardInputFragmentBinding
    private val viewModel: HideWizardInputViewModel by lazy { (requireActivity() as ScopeActivity).scope.get() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = HideWizardInputFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.liveDataNextButtonEnabled.observe(this) { binding.next.isEnabled = it }
        viewModel.liveDataPlainTextShown.observe(this) {
            binding.layoutText.visibility = VisibilityUtils.toVisibility(it)
        }
        viewModel.liveDataSelectFileShown.observe(this) {
            binding.containerFilePickerInput.visibility = VisibilityUtils.toVisibility(it)
        }

        binding.radioGroup.setOnCheckedChangeListener { _, checkedId -> viewModel.radioButtonChecked(checkedId) }
        binding.editTextText.addTextChangedListener(object : TextWatcherAdapter() {
            override fun afterTextChanged(s: Editable?) {
                viewModel.payloadPlainTextChanged(s.toString())
            }
        })

        binding.next.setOnClickListener { viewModel.nextClicked() }

        val filePickerFragment = FilePickerFragment.newFileInstance()
        filePickerFragment.listener = object : FilePickedListener {
            override fun onFileChanged(uri: Uri) {
                viewModel.payloadFileChanged(uri)
            }
        }
        requireFragmentManager().beginTransaction().replace(R.id.containerFilePickerInput, filePickerFragment).commit()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_INPUT_FILE && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            viewModel.payloadFileChanged(data.data!!)
        }
    }

    companion object {
        private const val REQUEST_CODE_INPUT_FILE = 1

        fun newInstance() = HideWizardInputFragment()
    }

}