package com.example.lionnote

// criado no quando o app inicia
import android.app.Application
import com.example.lionnote.model.AppDatabase

class App : Application(){
    lateinit var db: AppDatabase

    fun OnCreate(){
        super.onCreate()
        db = AppDatabase.getDatabase(this)
    }

}