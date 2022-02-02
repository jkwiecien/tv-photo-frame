package net.techbrewery.tvphotoframe.koin

import net.techbrewery.tvphotoframe.welcome.WelcomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

object ViewModelModule {
    val get: Module
        get() = module {
            viewModel { WelcomeViewModel() }
        }
}