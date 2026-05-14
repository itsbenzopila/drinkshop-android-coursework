package com.itsbenzopila.drinkshop

import android.app.Application
import com.itsbenzopila.drinkshop.di.AppContainer

class DrinkshopApplication : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(applicationContext)
    }
}
