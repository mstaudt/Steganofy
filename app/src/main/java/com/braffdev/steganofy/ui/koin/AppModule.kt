package com.braffdev.steganofy.ui.koin

import com.braffdev.steganofy.lib.service.HideService
import com.braffdev.steganofy.lib.service.RevealService
import com.braffdev.steganofy.service.BitmapService
import com.braffdev.steganofy.service.ExecutionService
import com.braffdev.steganofy.service.FileService
import com.braffdev.steganofy.service.PayloadService
import com.braffdev.steganofy.ui.about.AboutActivity
import com.braffdev.steganofy.ui.about.AboutViewModel
import com.braffdev.steganofy.ui.common.file.picker.FilePickerViewModel
import com.braffdev.steganofy.ui.common.file.saver.FileSaverViewModel
import com.braffdev.steganofy.ui.common.message.MessageViewModel
import com.braffdev.steganofy.ui.hide.HideActivity
import com.braffdev.steganofy.ui.hide.HideDataBridge
import com.braffdev.steganofy.ui.hide.HideViewModel
import com.braffdev.steganofy.ui.hide.success.HideSuccessViewModel
import com.braffdev.steganofy.ui.hide.wizard.HideWizardActivity
import com.braffdev.steganofy.ui.hide.wizard.HideWizardViewModel
import com.braffdev.steganofy.ui.hide.wizard.image.HideWizardImageViewModel
import com.braffdev.steganofy.ui.hide.wizard.input.HideWizardInputViewModel
import com.braffdev.steganofy.ui.hide.wizard.settings.HideWizardSettingsViewModel
import com.braffdev.steganofy.ui.main.MainActivity
import com.braffdev.steganofy.ui.main.MainViewModel
import com.braffdev.steganofy.ui.reveal.RevealActivity
import com.braffdev.steganofy.ui.reveal.RevealViewModel
import com.braffdev.steganofy.ui.reveal.success.RevealSuccessFileViewModel
import com.braffdev.steganofy.ui.reveal.success.RevealSuccessPlainTextViewModel
import com.braffdev.steganofy.ui.reveal.success.RevealSuccessViewModel
import com.braffdev.steganofy.ui.reveal.wizard.RevealWizardActivity
import com.braffdev.steganofy.ui.reveal.wizard.RevealWizardViewModel
import com.braffdev.steganofy.ui.reveal.wizard.info.SteganoInfoViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {

    /**
     * Services
     */
    single { HideService() }
    single { RevealService() }
    single { BitmapService(androidContext()) }
    single { FileService(androidContext()) }
    single { PayloadService(get()) }
    single { ExecutionService() }

    /**
     * ViewModels
     *
     * Most view models are scoped to a specific activity to ensure the instances are shared in the activity, but not beyond it.
     */
    factory { FilePickerViewModel(androidContext(), get(), get()) }
    factory { FileSaverViewModel(androidContext(), get(), get()) }
    factory { MessageViewModel() }
    factory { SteganoInfoViewModel() }

    scope<MainActivity> {
        scoped { MainViewModel(androidContext(), get()) }
    }

    scope<AboutActivity> {
        scoped { AboutViewModel(androidContext()) }
    }

    // Hide
    single { HideDataBridge() }

    scope<HideWizardActivity> {
        scoped { HideWizardViewModel(get()) }
        scoped { HideWizardInputViewModel(get(), get(), get()) }
        scoped { HideWizardImageViewModel(get(), get(), get(), get(), get()) }
        scoped { HideWizardSettingsViewModel(androidContext(), get(), get(), get()) }
    }

    scope<HideActivity> {
        scoped { HideViewModel(get(), get(), get(), get(), get()) }
        scoped { HideSuccessViewModel(androidContext(), get()) }
    }

    // Reveal
    scope<RevealWizardActivity> {
        scoped { RevealWizardViewModel(androidContext(), get(), get(), get()) }
    }

    scope<RevealActivity> {
        scoped { RevealViewModel(get(), get(), get(), get()) }
        scoped { RevealSuccessViewModel(androidContext(), get()) }
        scoped { RevealSuccessPlainTextViewModel(androidContext(), get()) }
        scoped { RevealSuccessFileViewModel(androidContext(), get(), get(), get(), get()) }
    }

}