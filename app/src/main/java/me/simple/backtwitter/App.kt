package me.simple.backtwitter

import android.app.Application
import android.content.Context

class App : Application() {

    companion object {
        var app: App? = null

        val context: Context
            get() = app!!.applicationContext
    }

    override fun onCreate() {
        super.onCreate()
        app = this
    }
}