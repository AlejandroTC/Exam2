package com.example.exam2

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.exam2.room.AppDatabase
import com.example.exam2.room.Estado
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainState : AppCompatActivity() {
    lateinit var addBtn: FloatingActionButton
    lateinit var backBtn: FloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_state)

        addBtn = findViewById(R.id.addStateBtn)
        backBtn = findViewById(R.id.backMainState)

        addBtn.setOnClickListener{
            var intentAgregarEstado = Intent(this, CreateState::class.java)
            startActivity(intentAgregarEstado)
        }
        backBtn.setOnClickListener {
            var intentRegresar = Intent(this, MainActivity::class.java)
            startActivity(intentRegresar)
        }

        val dbPersona = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "roomDB").build()

        val estadoDao = dbPersona.estadoDao()
        CoroutineScope(Dispatchers.IO).launch {
            val recyclerView = findViewById<RecyclerView>(R.id.stateRecycleView)
            val listadoEstados : List<Estado> = estadoDao.getAllEstados()
            runOnUiThread{
                val adapter = StateAdapter(listadoEstados)
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(this@MainState)
            }

        }
    }
}