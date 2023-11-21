package com.example.exam2

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.exam2.room.Persona
import com.example.exam2.room.AppDatabase
import com.example.exam2.room.Estado
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreatePerson : AppCompatActivity() {

    lateinit var createBtn: Button
    lateinit var backBtn: Button
    lateinit var nameInput: TextInputEditText
    lateinit var ageInput: TextInputEditText
    lateinit var ageInputLayout: TextInputLayout
    lateinit var spinnerEstado: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_person)

        createBtn = findViewById(R.id.saveBtn)
        backBtn = findViewById(R.id.backBtn)
        nameInput = findViewById(R.id.nameInput)
        ageInput = findViewById(R.id.ageInput)
        ageInputLayout = findViewById(R.id.ageInputLayout)
        spinnerEstado = findViewById(R.id.stateSelect)

        val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "roomDB").build()

        lifecycleScope.launch {
            try {
                val estados: List<Estado> = withContext(Dispatchers.IO) {
                    db.estadoDao().getAllEstados()
                }
                val adapter = ArrayAdapter(this@CreatePerson, android.R.layout.simple_spinner_item, estados.map { it.nombre })
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerEstado.adapter = adapter

            }catch (e: Exception) {
                Log.e("InfoPersonActivity", "Error obteniendo estados", e)
            }
        }


        ageInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (!s.isNullOrBlank()) {
                    val input = s.toString()
                    val number = input.toIntOrNull()
                    if (number == null || number < 0) {
                        ageInput.error = "Ingrese un número válido mayor que 0"
                        ageInputLayout.endIconMode = TextInputLayout.END_ICON_NONE
                    } else {
                        ageInput.error = null
                        ageInputLayout.endIconMode = TextInputLayout.END_ICON_CLEAR_TEXT
                    }
                }
            }
        })
        createBtn.setOnClickListener {
            agregarPersona()
            var intentRegresar = Intent(this, MainPerson::class.java)
            startActivity(intentRegresar)
        }

        backBtn.setOnClickListener {
            var intentRegresar = Intent(this, MainPerson::class.java)
            startActivity(intentRegresar)
        }
    }

    private fun agregarPersona() {
        val nombrePersona = nameInput.text.toString()
        val edadPersona = ageInput.text.toString()
        val estadoPersona = spinnerEstado.selectedItem.toString()

        if (nombrePersona.isNotEmpty() && edadPersona.isNotEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val db = Room.databaseBuilder(
                        applicationContext,
                        AppDatabase::class.java, "roomDB"
                    ).build()

                    val estadoDao = db.estadoDao()

                    val estadoPersonaBD: Estado = estadoDao.getEstadoByName(estadoPersona)
                    val stateId = estadoPersonaBD.stateId

                    val personaDao = db.personaDao()

                    val nuevaPersona = Persona(nombre = nombrePersona, edad = edadPersona.toInt(), stateId = stateId)
                    personaDao.newPersona(nuevaPersona)

                    val listadoPersona: List<Persona> = personaDao.getAllPersonas()

                    withContext(Dispatchers.Main) {
                        nameInput.setText("")
                        ageInput.setText("")
                        finish()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main) {
                        Toast.makeText(applicationContext, "Error al agregar persona", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {
            // Manejar el caso cuando los campos están vacíos
            Toast.makeText(applicationContext, "Nombre y edad son obligatorios", Toast.LENGTH_SHORT).show()
        }
    }

}