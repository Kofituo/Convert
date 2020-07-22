package com.otuolabs.unitconverter

import android.app.Application
import android.content.Context
import com.otuolabs.unitconverter.ads.AdsManager

class ApplicationLoader : Application() {

    companion object {
        var applicationContext: Context? = null
    }

    override fun onCreate() {
        super.onCreate()
        ApplicationLoader.applicationContext = applicationContext
        AdsManager
                .initializeAds()
                .initializeNetworkCallBack()
    }
}