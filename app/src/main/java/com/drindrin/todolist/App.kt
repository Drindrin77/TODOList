package com.drindrin.todolist

import android.app.Application
import com.drindrin.todolist.network.Api


class App: Application() {
    override fun onCreate() {
        super.onCreate()
        Api.setUpContext(this)
    }
}