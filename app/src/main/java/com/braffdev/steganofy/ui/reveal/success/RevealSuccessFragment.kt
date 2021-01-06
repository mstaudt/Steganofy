package com.braffdev.steganofy.ui.reveal.success

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.braffdev.steganofy.R
import com.braffdev.steganofy.databinding.RevealSuccessFragmentBinding
import com.braffdev.steganofy.lib.domain.Type
import org.koin.android.scope.ScopeActivity


class RevealSuccessFragment : Fragment() {

    companion object {
        fun newInstance() = RevealSuccessFragment()
    }

    private lateinit var binding: RevealSuccessFragmentBinding
    private val viewModel: RevealSuccessViewModel by lazy { (requireActivity() as ScopeActivity).scope.get() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = RevealSuccessFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.initialize()
        viewModel.liveDataStatisticsBytes.observe(this) { binding.textViewStatisticsBytes.text = it }
        viewModel.liveDataStatisticsRevealTime.observe(this) { binding.textViewStatisticsRevealTime.text = it }
        viewModel.liveDataStatisticsImageProcessing.observe(this) { binding.textViewStatisticsImageProcessing.text = it }
        viewModel.liveDataStatisticsTotalTime.observe(this) { binding.textViewStatisticsTotalTime.text = it }

        val fragment = if (viewModel.getPayloadType() == Type.FILE) {
            RevealSuccessFileFragment.newInstance()
        } else {
            RevealSuccessPlainTextFragment.newInstance()
        }

        requireFragmentManager().beginTransaction().replace(R.id.containerResult, fragment).commit()
    }
}