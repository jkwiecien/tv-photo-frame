package net.techbrewery.tvphotoframe

import android.app.Application
import net.techbrewery.tvphotoframe.core.koin.OAuth2Module
import net.techbrewery.tvphotoframe.core.koin.ViewModelModule
import net.techbrewery.tvphotoframe.core.logs.DebugLogsTree
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        setupKoin()
        setupLogs()
    }

    private fun setupLogs() {
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugLogsTree())
        }
    }

    private fun setupKoin() {
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@App)
            modules(
                listOf(
                    OAuth2Module.get,
                    ViewModelModule.get
                )
            )
        }
    }
}

