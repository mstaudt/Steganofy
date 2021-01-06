package com.braffdev.steganofy.ui.hide.wizard

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.braffdev.steganofy.ui.hide.wizard.image.HideWizardImageFragment
import com.braffdev.steganofy.ui.hide.wizard.input.HideWizardInputFragment
import com.braffdev.steganofy.ui.hide.wizard.settings.HideWizardSettingsFragment

class HideWizardFragmentStateAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return HideWizardInputFragment.newInstance()
            1 -> return HideWizardImageFragment.newInstance()
            2 -> return HideWizardSettingsFragment.newInstance()
        }

        throw IllegalStateException("Unexpected position $position")
    }
}