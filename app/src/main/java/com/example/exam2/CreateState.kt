package com.example.exam2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.exam2.room.AppDatabase
import com.example.exam2.room.Estado
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreateState : AppCompatActivity() {
    lateinit var createBtn: Button
    lateinit var backBtn: Button
    lateinit var nameInput: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_state)

        createBtn = findViewById(R.id.stateSaveBtn)
        backBtn = findViewById(R.id.stateBackBtn)
        nameInput = findViewById(R.id.stateNameInput)

        createBtn.setOnClickListener {
            agregarEstado()
            var intentRegresar = Intent(this, MainState::class.java)
            startActivity(intentRegresar)
        }

        backBtn.setOnClickListener {
            var intentRegresar = Intent(this, MainState::class.java)
            startActivity(intentRegresar)
        }
    }

    private fun agregarEstado() {
        val nombreEstado = nameInput.text.toString()
        if(nombreEstado.isNotEmpty()){
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val db = Room.databaseBuilder(
                        applicationContext,
                        AppDatabase::class.java, "roomDB"
                    ).build()

                    val estadoDAO = db.estadoDao()
                    val nuevoEstado = Estado(nombre = nombreEstado)
                    estadoDAO.insertEstado(nuevoEstado)

                    withContext(Dispatchers.Main) {
                        nameInput.setText("")
                        finish()
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(applicationContext, "Error al agregar estado", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {
            // Manejar el caso cuando los campos están vacíos
            Toast.makeText(applicationContext, "Nombre es obligatorios", Toast.LENGTH_SHORT).show()
        }
    }
}