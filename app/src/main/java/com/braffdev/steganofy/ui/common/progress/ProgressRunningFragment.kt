package com.braffdev.steganofy.ui.common.progress

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.braffdev.steganofy.databinding.ProgressRunningFragmentBinding

class ProgressRunningFragment : Fragment() {

    companion object {
        fun newInstance() = ProgressRunningFragment()
    }

    private lateinit var binding: ProgressRunningFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = ProgressRunningFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
}