package com.braffdev.steganofy.ui.common.file.saver

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.braffdev.steganofy.databinding.FileSaverFragmentBinding
import org.koin.android.ext.android.inject

class FileSaverFragment : Fragment() {

    private lateinit var binding: FileSaverFragmentBinding
    private val viewModel: FileSaverViewModel by inject()
    lateinit var listener: FileSavedListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FileSaverFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.initialize(getTemporaryFileUri(), getMimeType(), getButtonTextRes())
        viewModel.liveDataIntentForResult.observe(this) { startActivityForResult(it, REQUEST_CODE_SAVE_FILE) }
        viewModel.liveDataIntent.observe(this) { startActivity(it) }

        binding.buttonShareFile.setOnClickListener { viewModel.shareFileClicked() }
        binding.buttonSaveFile.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(), WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(WRITE_EXTERNAL_STORAGE), REQUEST_CODE_PERMISSION)
            } else {
                viewModel.saveFileClicked()
            }
        }

        binding.buttonSaveFile.setText(getButtonTextRes())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_SAVE_FILE && data != null && data.data != null) {
            viewModel.onFileUriChanged(data.data!!)
            listener.onFileSaved(data.data!!)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE_PERMISSION && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            viewModel.saveFileClicked()
        }
    }

    private fun getTemporaryFileUri(): Uri {
        return requireArguments().getParcelable(EXTRA_TEMPORARY_FILE_URI)!!
    }

    private fun getButtonTextRes(): Int {
        return requireArguments().getInt(EXTRA_TEXT_RES)
    }

    private fun getMimeType(): String {
        return requireArguments().getString(EXTRA_MIME_TYPE)!!
    }

    companion object {
        private const val EXTRA_TEMPORARY_FILE_URI = "tmpFileUri"
        private const val EXTRA_MIME_TYPE = "mimeType"
        private const val EXTRA_TEXT_RES = "textRes"
        private const val REQUEST_CODE_PERMISSION = 1
        private const val REQUEST_CODE_SAVE_FILE = 2

        fun newInstance(temporaryFileUri: Uri, @StringRes textRes: Int, mimeType: String): FileSaverFragment {
            val bundle = Bundle()
            bundle.putParcelable(EXTRA_TEMPORARY_FILE_URI, temporaryFileUri)
            bundle.putInt(EXTRA_TEXT_RES, textRes)
            bundle.putString(EXTRA_MIME_TYPE, mimeType)

            val fragment = FileSaverFragment()
            fragment.arguments = bundle

            return fragment
        }

    }

}