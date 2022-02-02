package net.techbrewery.tvphotoframe.core.koin

import net.techbrewery.tvphotoframe.features.welcome.WelcomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

object ViewModelModule {
    val get: Module
        get() = module {
            viewModel { WelcomeViewModel() }
        }
}