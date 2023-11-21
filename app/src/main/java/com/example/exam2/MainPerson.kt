package com.example.exam2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Spinner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.exam2.room.Persona
import com.example.exam2.room.AppDatabase
import com.example.exam2.room.Estado
import com.example.exam2.room.PersonasEnEstado
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainPerson : AppCompatActivity() {
    lateinit var addBtn: FloatingActionButton
    lateinit var backBtn: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person)

        val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "roomDB").build()
        val personaDao = db.personaDao()

        addBtn = findViewById(R.id.addPersonBtn)
        backBtn = findViewById(R.id.backMainPerson)

        addBtn.setOnClickListener {
            var intentAgregar = Intent(this, CreatePerson::class.java)
            startActivity(intentAgregar)
        }
        backBtn.setOnClickListener {
            var intentRegresar = Intent(this, MainActivity::class.java)
            startActivity(intentRegresar)
        }


        CoroutineScope(Dispatchers.IO).launch {
            val recyclerView = findViewById<RecyclerView>(R.id.personRecycleView)
            val listadoPersonas : List<Persona> = personaDao.getAllPersonas()
            runOnUiThread{
                val adapter = PersonAdapter(listadoPersonas)
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(this@MainPerson)
            }
        }

    }
}
