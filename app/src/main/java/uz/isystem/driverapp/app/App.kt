package uz.isystem.driverapp.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import uz.isystem.driverapp.data.cache.AppCache

@HiltAndroidApp
class App : Application(){
    override fun onCreate() {
        super.onCreate()
        AppCache.init(this)
    }
}