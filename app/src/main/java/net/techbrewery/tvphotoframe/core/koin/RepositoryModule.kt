package net.techbrewery.tvphotoframe.core.koin

import net.techbrewery.tvphotoframe.mobile.welcome.PhotosRepository
import org.koin.core.module.Module
import org.koin.dsl.module

object RepositoryModule {
    val get: Module
        get() = module {
            factory { PhotosRepository() }
        }
}