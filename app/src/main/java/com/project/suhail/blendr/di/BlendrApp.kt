package com.project.suhail.blendr.di

import android.app.Application
import com.project.suhail.blendr.BuildConfig
import com.project.suhail.blendr.utils.PrefUtils
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class BlendrApp : Application() {

    override fun onCreate() {
        super.onCreate()
        PrefUtils.init(this)
        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())
    }

}