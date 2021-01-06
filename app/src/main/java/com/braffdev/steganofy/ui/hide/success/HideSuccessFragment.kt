package com.braffdev.steganofy.ui.hide.success

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.braffdev.steganofy.R
import com.braffdev.steganofy.databinding.HideSuccessFragmentBinding
import com.braffdev.steganofy.ui.common.file.saver.FileSavedListener
import com.braffdev.steganofy.ui.common.file.saver.FileSaverFragment
import com.braffdev.steganofy.ui.common.message.MessageFragment
import org.koin.android.scope.ScopeActivity

class HideSuccessFragment : Fragment() {

    private lateinit var binding: HideSuccessFragmentBinding
    private val viewModel: HideSuccessViewModel by lazy { (requireActivity() as ScopeActivity).scope.get() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = HideSuccessFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.initialize()
        viewModel.liveDataStatisticsBytes.observe(this) { binding.textViewStatisticsBytes.text = it }
        viewModel.liveDataStatisticsHideTime.observe(this) { binding.textViewStatisticsHideTime.text = it }
        viewModel.liveDataStatisticsImageProcessing.observe(this) { binding.textViewStatisticsImageProcessing.text = it }
        viewModel.liveDataStatisticsTotalTime.observe(this) { binding.textViewStatisticsTotalTime.text = it }
        viewModel.liveEventFinish.observe(this) { requireActivity().finish() }

        val fragment = FileSaverFragment.newInstance(viewModel.getTemporaryFileUri(), R.string.file_save_image, "image/png")
        fragment.listener = object : FileSavedListener {
            override fun onFileSaved(uri: Uri) {
                Toast.makeText(requireContext(), R.string.file_saved, Toast.LENGTH_LONG).show()
                viewModel.imageSaved()
            }
        }
        requireFragmentManager().beginTransaction().replace(R.id.containerSaveFile, fragment).commit()

        val messageFragment = MessageFragment.createInfoMessage(R.string.hide_image_do_not_alter)
        requireFragmentManager().beginTransaction().replace(R.id.containerImageInfo, messageFragment).commit()
    }

    companion object {
        fun newInstance() = HideSuccessFragment()
    }
}