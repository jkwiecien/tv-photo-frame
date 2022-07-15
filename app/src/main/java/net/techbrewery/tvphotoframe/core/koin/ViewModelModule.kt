package net.techbrewery.tvphotoframe.core.koin

import net.techbrewery.tvphotoframe.mobile.welcome.WelcomeMobileViewModel
import net.techbrewery.tvphotoframe.tv.welcome.WelcomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

object ViewModelModule {
    val get: Module
        get() = module {
            viewModel { WelcomeViewModel() }
            viewModel { WelcomeMobileViewModel(get()) }
        }
}