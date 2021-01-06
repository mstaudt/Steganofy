package com.braffdev.steganofy.ui.hide.wizard.image

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.braffdev.steganofy.R
import com.braffdev.steganofy.databinding.HideWizardImageFragmentBinding
import com.braffdev.steganofy.ui.common.file.picker.FilePickedListener
import com.braffdev.steganofy.ui.common.file.picker.FilePickerFragment
import com.braffdev.steganofy.ui.common.message.MessageFragment
import org.koin.android.scope.ScopeActivity


class HideWizardImageFragment : Fragment() {

    private lateinit var binding: HideWizardImageFragmentBinding
    private val viewModel: HideWizardImageViewModel by lazy { (requireActivity() as ScopeActivity).scope.get() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = HideWizardImageFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.liveDataNextButtonEnabled.observe(this) { binding.next.isEnabled = it }
        viewModel.liveDataShowImageTooSmall.observe(this) {
            if (it) {
                showFragment(MessageFragment.createErrorMessage(R.string.hide_image_too_small))
            } else {
                showFragment(null)
            }
        }
        viewModel.liveDataShowImageTooLarge.observe(this) {
            if (it) {
                showFragment(MessageFragment.createErrorMessage(R.string.hide_image_too_large))
            } else {
                showFragment(null)
            }
        }

        binding.next.setOnClickListener { viewModel.nextClicked() }
        binding.previous.setOnClickListener { viewModel.previousClicked() }

        val filePickerFragment = FilePickerFragment.newImageInstance()
        filePickerFragment.listener = object : FilePickedListener {
            override fun onFileChanged(uri: Uri) {
                viewModel.imageUriChanged(uri)
            }
        }

        requireFragmentManager().beginTransaction().replace(R.id.containerFilePickerImage, filePickerFragment).commit()
    }

    override fun onResume() {
        super.onResume()
        viewModel.resume()
    }

    private fun showFragment(fragment: Fragment?) {
        if (fragment != null) {
            requireFragmentManager().beginTransaction().replace(R.id.containerMessage, fragment).commit()
        } else {
            requireFragmentManager().findFragmentById(R.id.containerMessage)
                ?.let { shownFragment -> requireFragmentManager().beginTransaction().remove(shownFragment).commit() }
        }
    }

    companion object {
        fun newInstance() = HideWizardImageFragment()
    }

}