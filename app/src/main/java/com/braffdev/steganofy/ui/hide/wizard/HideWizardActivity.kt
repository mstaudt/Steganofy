package com.braffdev.steganofy.ui.hide.wizard

import android.app.AlertDialog
import android.os.Bundle
import android.view.MenuItem
import com.braffdev.steganofy.R
import com.braffdev.steganofy.databinding.HideWizardActivityBinding
import org.koin.android.scope.ScopeActivity

class HideWizardActivity : ScopeActivity() {

    private lateinit var binding: HideWizardActivityBinding
    private val viewModel: HideWizardViewModel by scope.inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = HideWizardActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        setTitle(R.string.main_hide)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_close_24)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        viewModel.liveDataCurrentPage.observe(this) { position -> binding.viewPager.currentItem = position }
        viewModel.liveEventShowCancelDialog.observe(this) { showCancelDialog() }

        binding.viewPager.adapter = HideWizardFragmentStateAdapter(this)
        binding.viewPager.isUserInputEnabled = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            viewModel.previousClicked()
            return true
        }

        return false
    }

    override fun onBackPressed() {
        viewModel.previousClicked()
    }

    private fun showCancelDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.cancel)
            .setMessage(R.string.cancel_confirmation)
            .setPositiveButton(R.string.yes) { _, _ -> finish() }
            .setNegativeButton(R.string.no) { _, _ -> }
            .show()
    }
}