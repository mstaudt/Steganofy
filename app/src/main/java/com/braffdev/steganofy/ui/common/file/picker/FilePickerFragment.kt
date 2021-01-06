package com.braffdev.steganofy.ui.common.file.picker

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.braffdev.steganofy.R
import com.braffdev.steganofy.databinding.FilePickerFragmentBinding
import org.koin.android.ext.android.inject

class FilePickerFragment : Fragment() {

    private lateinit var binding: FilePickerFragmentBinding
    private val viewModel: FilePickerViewModel by inject()
    lateinit var listener: FilePickedListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FilePickerFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.initialize(getMimeType(), getPickerTextRes())
        viewModel.liveDataIntent.observe(this) { startActivityForResult(it, 1) }
        viewModel.liveDataFileName.observe(this) { binding.textViewFileName.text = it }
        viewModel.liveDataFileInfo.observe(this) {
            binding.textViewFileInfo.visibility = View.VISIBLE
            binding.textViewFileInfo.text = it
        }

        binding.selectFile.setOnClickListener { viewModel.selectFileClicked() }
        binding.selectFile.setText(getPickerTextRes())
        binding.textViewFileName.setText(getNothingSelectedTextRes())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            viewModel.onFileUriChanged(data.data!!)
            listener.onFileChanged(data.data!!)
        }
    }

    private fun getMimeType(): String {
        return requireArguments().getString(EXTRA_MIME_TYPE)!!
    }

    private fun getPickerTextRes(): Int {
        return requireArguments().getInt(EXTRA_PICKER_TEXT_RES)
    }

    private fun getNothingSelectedTextRes(): Int {
        return requireArguments().getInt(EXTRA_NOTHING_SELECTED_TEXT_RES)
    }

    companion object {
        private const val EXTRA_MIME_TYPE = "mimeType"
        private const val EXTRA_PICKER_TEXT_RES = "pickerTextRes"
        private const val EXTRA_NOTHING_SELECTED_TEXT_RES = "nothingSelectedTextRes"

        fun newImageInstance(): FilePickerFragment {
            return newInstance("image/*", R.string.file_select_image, R.string.file_select_image_none)
        }

        fun newFileInstance(): FilePickerFragment {
            return newInstance("*/*", R.string.file_select, R.string.file_select_none)
        }

        private fun newInstance(mimeType: String, @StringRes pickerTextRes: Int, @StringRes nothingSelectedTextRes: Int): FilePickerFragment {
            val bundle = Bundle()
            bundle.putString(EXTRA_MIME_TYPE, mimeType)
            bundle.putInt(EXTRA_PICKER_TEXT_RES, pickerTextRes)
            bundle.putInt(EXTRA_NOTHING_SELECTED_TEXT_RES, nothingSelectedTextRes)

            val fragment = FilePickerFragment()
            fragment.arguments = bundle

            return fragment
        }
    }
}