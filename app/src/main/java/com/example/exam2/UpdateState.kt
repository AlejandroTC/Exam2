package com.example.exam2

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.exam2.room.AppDatabase
import com.example.exam2.room.Estado
import com.example.exam2.room.Persona
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UpdateState : AppCompatActivity() {

    lateinit var saveBtn: Button
    lateinit var backBtn: Button
    lateinit var nameInput: TextInputEditText
    lateinit var deleteBtn: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_state)

        saveBtn = findViewById(R.id.stateSaveBtnInfo)
        deleteBtn = findViewById(R.id.stateDeleteBtnInfo)
        nameInput = findViewById(R.id.stateNameInputInfo)
        backBtn = findViewById(R.id.stateBackBtnInfo)

        val id = intent.getLongExtra("id", 0)
        val estadoName = intent.getStringExtra("nombre")
        nameInput.setText(estadoName)

        deleteBtn.setOnClickListener{
            lifecycleScope.launch {
                eliminarEstado(id)
            }
            var intentRegresar = Intent(this, MainState::class.java)
            startActivity(intentRegresar)
        }

        saveBtn.setOnClickListener {
            lifecycleScope.launch {
                actualizarEstado(id)
            }
            finish()
            var intentRegresar = Intent(this, MainState::class.java)
            startActivity(intentRegresar)
        }

        backBtn.setOnClickListener {
            finish()
            var intentRegresar = Intent(this, MainState::class.java)
            startActivity(intentRegresar)
        }

    }

    private suspend fun eliminarEstado(id: Long) {
        withContext(Dispatchers.IO) {
            val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "roomDB").build()
            val estadoDao = db.estadoDao()
            val estado: Estado = estadoDao.getEstadoById(id)
            estadoDao.deleteEstado(estado)
        }
    }

    private fun actualizarEstado(id: Long) {
        val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "roomDB").build()
        val estadoDao = db.estadoDao()
        val id = id
        val nombreNuevo = nameInput.text.toString()
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main) {
                if (nombreNuevo != null) {
                    val estadoActualizado = Estado(id, nombreNuevo)
                    CoroutineScope(Dispatchers.IO).launch {
                        estadoDao.updateEstado(estadoActualizado)
                    }
                    finish()
                } else {
                    Toast.makeText(applicationContext, "Error al actualizar el estado", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}