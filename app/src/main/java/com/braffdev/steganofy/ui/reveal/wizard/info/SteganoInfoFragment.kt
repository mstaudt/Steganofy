package com.braffdev.steganofy.ui.reveal.wizard.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.braffdev.steganofy.databinding.SteganoInfoFragmentBinding
import com.braffdev.steganofy.lib.domain.SteganoInfo
import org.koin.android.ext.android.inject

class SteganoInfoFragment : Fragment() {

    private lateinit var binding: SteganoInfoFragmentBinding
    private val viewModel: SteganoInfoViewModel by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = SteganoInfoFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.initialize(getSteganoInfo())
        viewModel.liveDataTextRes.observe(this) { binding.revealInfoText.setText(it) }
        viewModel.liveDataVersion.observe(this) { binding.revealInfoVersion.text = it }
        viewModel.liveDataTypeTextRes.observe(this) { binding.revealInfoType.setText(it) }
        viewModel.liveDataEncryptionTextRes.observe(this) { binding.revealInfoEncryption.setText(it) }
    }

    private fun getSteganoInfo(): SteganoInfo {
        return requireArguments().getSerializable(EXTRA_STEGANO_INFO) as SteganoInfo
    }

    companion object {
        private const val EXTRA_STEGANO_INFO = "info"

        fun newInstance(steganoInfo: SteganoInfo): SteganoInfoFragment {
            val bundle = Bundle()
            bundle.putSerializable(EXTRA_STEGANO_INFO, steganoInfo)

            val fragment = SteganoInfoFragment()
            fragment.arguments = bundle

            return fragment
        }
    }
}