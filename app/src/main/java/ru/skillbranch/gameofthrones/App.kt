package ru.skillbranch.gameofthrones

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import ru.skillbranch.gameofthrones.di.apiModule

class App : Application() {

    companion object {
        private var instance: App? = null

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }
    }

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        /*PreferencesRepository.getAppTheme().also{
            AppCompatDelegate.setDefaultNightMode(it)
        }*/
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(
                listOf(
                    apiModule
//                    ,
//                    networkModule,
//                    viewModelModule
                )
            )
        }

        //TODO call once when application created
    }

}