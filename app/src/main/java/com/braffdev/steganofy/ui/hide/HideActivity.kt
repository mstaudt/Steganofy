package com.braffdev.steganofy.ui.hide

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.braffdev.steganofy.R
import com.braffdev.steganofy.databinding.HideActivityBinding
import com.braffdev.steganofy.ui.common.OperationStatus
import com.braffdev.steganofy.ui.common.progress.ProgressErrorFragment
import com.braffdev.steganofy.ui.common.progress.ProgressRunningFragment
import com.braffdev.steganofy.ui.hide.success.HideSuccessFragment
import org.koin.android.scope.ScopeActivity

class HideActivity : ScopeActivity() {

    private lateinit var binding: HideActivityBinding
    private val viewModel: HideViewModel by scope.inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = HideActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        viewModel.liveDataStatus.observe(this) { setStatus(it) }
        viewModel.liveEventCancelWarning.observe(this) { showCancelWarningDialog() }
        viewModel.initialize()
    }

    private fun showCancelWarningDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.cancel)
            .setMessage(R.string.cancel_confirmation)
            .setPositiveButton(R.string.yes) { _, _ -> finish() }
            .setNegativeButton(R.string.no) { _, _ -> }
            .show()
    }

    override fun finish() {
        viewModel.finish()
        super.finish()
    }

    override fun onBackPressed() {
        viewModel.backPressed()
    }

    private fun setStatus(status: OperationStatus) {
        if (status == OperationStatus.RUNNING) {
            supportFragmentManager.beginTransaction().replace(R.id.container, ProgressRunningFragment.newInstance()).commit()

        } else if (status == OperationStatus.SUCCESS) {
            supportFragmentManager.beginTransaction().replace(R.id.container, HideSuccessFragment.newInstance()).commit()

        } else if (status == OperationStatus.ERROR) {
            supportFragmentManager.beginTransaction().replace(R.id.container, ProgressErrorFragment.newInstance(R.string.hide_failed)).commit()
        }
    }

    companion object {

        fun createIntent(context: Context): Intent {
            return Intent(context, HideActivity::class.java)
        }
    }
}