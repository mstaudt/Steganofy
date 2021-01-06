package com.braffdev.steganofy.ui.about

import android.os.Bundle
import com.braffdev.steganofy.R
import com.braffdev.steganofy.databinding.AboutActivityBinding
import org.koin.android.scope.ScopeActivity


class AboutActivity : ScopeActivity() {

    private lateinit var binding: AboutActivityBinding
    private val viewModel: AboutViewModel by scope.inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = AboutActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_close_24)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setTitle(R.string.about_title)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        viewModel.initialize()
        viewModel.liveEventStartActivity.observe(this) { startActivity(it) }
        viewModel.liveDataAppInfo.observe(this) { binding.textViewAboutVersion.text = it }

        binding.buttonAboutEmail.setOnClickListener { viewModel.emailClicked() }
        binding.buttonAboutWeb.setOnClickListener { viewModel.webClicked() }
        binding.buttonAboutGitHub.setOnClickListener { viewModel.gitHubClicked() }
        binding.textViewAboutFAQ.setOnClickListener { viewModel.faqClicked() }
        binding.textViewAboutLicenses.setOnClickListener { viewModel.licensesClicked() }
    }
}