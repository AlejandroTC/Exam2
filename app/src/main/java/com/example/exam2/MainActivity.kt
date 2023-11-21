package com.example.exam2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    lateinit var personBtn : Button
    lateinit var stateBtn : Button
    lateinit var exitBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        personBtn = findViewById(R.id.Person)
        stateBtn = findViewById(R.id.States)
        exitBtn = findViewById(R.id.Exit)

        personBtn.setOnClickListener {
            var intentPerson = Intent(this, MainPerson::class.java)
            startActivity(intentPerson)
        }

        stateBtn.setOnClickListener {
            var intentState = Intent(this, MainState::class.java)
            startActivity(intentState)
        }

        exitBtn.setOnClickListener {
            finishAffinity()
        }

    }
}