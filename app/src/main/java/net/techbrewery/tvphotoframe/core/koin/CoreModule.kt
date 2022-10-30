package net.techbrewery.tvphotoframe.core.koin

import androidx.preference.PreferenceManager
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

object CoreModule {
    val get: Module
        get() = module {
//            single {
//                // This is bugged in android
//                EncryptedSharedPreferences.create(
//                    "secret_shared_prefs",
//                    BuildConfig.SHARED_PREF_KEY,
//                    androidContext(),
//                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
//                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
//                )
//            }
            single {
                PreferenceManager.getDefaultSharedPreferences(androidContext())
            }
            single {
                androidContext().applicationContext.contentResolver
            }
        }
}