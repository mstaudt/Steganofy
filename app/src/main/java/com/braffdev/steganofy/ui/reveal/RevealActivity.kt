package com.braffdev.steganofy.ui.reveal

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.braffdev.steganofy.R
import com.braffdev.steganofy.databinding.RevealActivityBinding
import com.braffdev.steganofy.ui.common.OperationStatus
import com.braffdev.steganofy.ui.common.progress.ProgressErrorFragment
import com.braffdev.steganofy.ui.common.progress.ProgressRunningFragment
import com.braffdev.steganofy.ui.reveal.success.RevealSuccessFragment
import org.koin.android.scope.ScopeActivity

class RevealActivity : ScopeActivity() {

    private lateinit var binding: RevealActivityBinding
    private val viewModel: RevealViewModel by scope.inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = RevealActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        viewModel.liveDataStatus.observe(this) { setStatus(it) }
        viewModel.liveEventCancelWarning.observe(this) { showCancelWarningDialog() }

        val imageUri = getImageUri(intent)
        val decryptionPassword = getDecryptionPassword(intent)
        viewModel.initialize(imageUri, decryptionPassword)
    }

    override fun finish() {
        viewModel.finish()
        super.finish()
    }

    override fun onBackPressed() {
        viewModel.backPressed()
    }

    private fun showCancelWarningDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.cancel)
            .setMessage(R.string.cancel_confirmation)
            .setPositiveButton(R.string.yes) { _, _ -> finish() }
            .setNegativeButton(R.string.no) { _, _ -> }
            .show()
    }

    private fun setStatus(status: OperationStatus) {
        if (status == OperationStatus.RUNNING) {
            supportFragmentManager.beginTransaction().replace(R.id.container, ProgressRunningFragment.newInstance()).commit()

        } else if (status == OperationStatus.SUCCESS) {
            supportFragmentManager.beginTransaction().replace(R.id.container, RevealSuccessFragment.newInstance()).commit()

        } else if (status == OperationStatus.ERROR) {
            supportFragmentManager.beginTransaction().replace(R.id.container, ProgressErrorFragment.newInstance(R.string.reveal_failed)).commit()
        }
    }

    companion object {

        private const val EXTRA_IMAGE_URI = "imageUri"
        private const val EXTRA_DECRYPTION_PASSWORD = "decryptionPassword"

        fun createIntent(context: Context, imageUri: Uri, decryptionPassword: CharArray?): Intent {
            val intent = Intent(context, RevealActivity::class.java)
            intent.putExtra(EXTRA_IMAGE_URI, imageUri)
            intent.putExtra(EXTRA_DECRYPTION_PASSWORD, decryptionPassword)
            return intent
        }

        fun getImageUri(intent: Intent): Uri {
            return intent.getParcelableExtra(EXTRA_IMAGE_URI)!!
        }

        fun getDecryptionPassword(intent: Intent): CharArray? {
            return intent.getCharArrayExtra(EXTRA_DECRYPTION_PASSWORD)
        }
    }
}