package com.braffdev.steganofy.ui.reveal.success

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.braffdev.steganofy.R
import com.braffdev.steganofy.databinding.RevealSuccessPlaintextFragmentBinding
import com.google.android.material.snackbar.Snackbar
import org.koin.android.scope.ScopeActivity


class RevealSuccessPlainTextFragment : Fragment() {

    private lateinit var binding: RevealSuccessPlaintextFragmentBinding
    private val viewModel: RevealSuccessPlainTextViewModel by lazy { (requireActivity() as ScopeActivity).scope.get() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = RevealSuccessPlaintextFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.initialize()
        viewModel.liveDataPlainText.observe(this) { binding.textViewResultPlainText.text = it }

        binding.buttonClose.setOnClickListener { requireActivity().finish() }
        binding.buttonCopyToClipBoard.setOnClickListener {
            viewModel.copyToClipBoardClicked()
            Snackbar.make(requireView(), R.string.reveal_plaintext_copied, Snackbar.LENGTH_SHORT).show()
        }
    }

    companion object {
        fun newInstance() = RevealSuccessPlainTextFragment()
    }
}
