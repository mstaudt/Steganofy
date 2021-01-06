package com.braffdev.steganofy.ui.common.message

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.braffdev.steganofy.databinding.MessageFragmentBinding
import org.koin.android.ext.android.inject

class MessageFragment : Fragment() {

    private lateinit var binding: MessageFragmentBinding
    private val viewModel: MessageViewModel by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = MessageFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.initialize(getTextRes(), getType())
        viewModel.liveDataTextRes.observe(this) { binding.textViewMessage.setText(it) }
        viewModel.liveDataIconRes.observe(this) { binding.imageViewIcon.setImageResource(it) }
        viewModel.liveDataBackgroundRes.observe(this) { binding.messageContainer.setBackgroundResource(it) }
    }

    private fun getTextRes(): Int {
        return requireArguments().getInt(EXTRA_TEXT_RES)
    }

    private fun getType(): Type {
        return Type.valueOf(requireArguments().getString(EXTRA_TYPE)!!)
    }

    companion object {
        private const val EXTRA_TEXT_RES = "textRes"
        private const val EXTRA_TYPE = "type"

        fun createErrorMessage(@StringRes text: Int): MessageFragment {
            return newInstance(text, Type.ERROR)
        }

        fun createInfoMessage(@StringRes text: Int): MessageFragment {
            return newInstance(text, Type.INFO)
        }

        private fun newInstance(@StringRes text: Int, type: Type): MessageFragment {
            val bundle = Bundle()
            bundle.putInt(EXTRA_TEXT_RES, text)
            bundle.putString(EXTRA_TYPE, type.name)

            val fragment = MessageFragment()
            fragment.arguments = bundle

            return fragment
        }
    }

    enum class Type {
        INFO, ERROR
    }
}