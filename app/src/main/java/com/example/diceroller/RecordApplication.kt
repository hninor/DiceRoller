package com.example.diceroller

import android.app.Application
import com.facebook.stetho.Stetho

class RecordApplication : Application() {



    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this);
    }

}