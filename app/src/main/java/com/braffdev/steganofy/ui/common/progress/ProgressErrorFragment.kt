package com.braffdev.steganofy.ui.common.progress

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.braffdev.steganofy.databinding.ProgressErrorFragmentBinding

class ProgressErrorFragment : Fragment() {

    private lateinit var binding: ProgressErrorFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = ProgressErrorFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textViewErrorMessage.setText(getTextRes())
        binding.buttonClose.setOnClickListener { requireActivity().finish() }
    }

    private fun getTextRes(): Int {
        return requireArguments().getInt(EXTRA_TEXT_RES)
    }

    companion object {

        private const val EXTRA_TEXT_RES = "textRes"

        fun newInstance(@StringRes textRes: Int): ProgressErrorFragment {
            val bundle = Bundle()
            bundle.putInt(EXTRA_TEXT_RES, textRes)

            val fragment = ProgressErrorFragment()
            fragment.arguments = bundle

            return fragment
        }
    }
}