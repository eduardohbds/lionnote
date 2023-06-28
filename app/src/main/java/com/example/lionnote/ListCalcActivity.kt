package com.example.lionnote

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class ListCalcActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_calc)
        //se não chegar nada joga um erro
        val type = intent?.extras?.getString("type") ?: throw IllegalStateException("Type not found")
    }
}