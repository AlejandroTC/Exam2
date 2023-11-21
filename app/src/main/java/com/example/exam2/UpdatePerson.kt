package com.example.exam2

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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

class UpdatePerson : AppCompatActivity() {

    lateinit var saveBtn: Button
    lateinit var backBtn: Button
    lateinit var nameInput: TextInputEditText
    lateinit var ageInput: TextInputEditText
    lateinit var ageInputLayout: TextInputLayout
    lateinit var spinnerEstado: Spinner
    lateinit var deleteBtn: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_person)

        saveBtn = findViewById(R.id.saveBtnInfo)
        backBtn = findViewById(R.id.backBtnInfo)
        nameInput = findViewById(R.id.nameInputInfo)
        ageInput = findViewById(R.id.ageInputInfo)
        ageInputLayout = findViewById(R.id.ageInputLayoutInfo)
        spinnerEstado = findViewById(R.id.stateSelectInfo)
        deleteBtn = findViewById(R.id.deleteBtn)

        val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "roomDB").build()

        val id = intent.getIntExtra("id", 0)
        val personName = intent.getStringExtra("nombre")
        val personAge = intent.getIntExtra("edad", 0)

        lifecycleScope.launch {
            try {
                val estados: List<Estado> = withContext(Dispatchers.IO) {
                    db.estadoDao().getAllEstados()
                }
                val adapter = ArrayAdapter(this@UpdatePerson, android.R.layout.simple_spinner_item, estados.map { it.nombre })
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerEstado.adapter = adapter

                val persona: Persona = withContext(Dispatchers.IO) {
                    db.personaDao().getPersonaById(id)
                }
                val estadoActualId: Long = persona.stateId

                val posicionEstadoActual: Int = estados.indexOfFirst { it.stateId == estadoActualId }
                spinnerEstado.setSelection(posicionEstadoActual)
                nameInput.setText(personName)
                ageInput.setText(personAge.toString())
            }catch (e: Exception) {
                Log.e("InfoPersonActivity", "Error en la corutina", e)
            }
        }

        ageInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                // Verificar si la entrada es numérica
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

        deleteBtn.setOnClickListener{
            lifecycleScope.launch {
                // Operaciones de actualización en un hilo secundario
                eliminarPersona(id)
            }
            var intentRegresar = Intent(this, MainPerson::class.java)
            startActivity(intentRegresar)
        }

        saveBtn.setOnClickListener {
            lifecycleScope.launch {
                // Operaciones de actualización en un hilo secundario
                actualizarPersona(id)
            }
            var intentRegresar = Intent(this, MainPerson::class.java)
            startActivity(intentRegresar)
        }

        backBtn.setOnClickListener {
            var intentRegresar = Intent(this, MainPerson::class.java)
            startActivity(intentRegresar)
        }
    }

    private suspend fun eliminarPersona(id: Int) {
        withContext(Dispatchers.IO) {
            // Operaciones de base de datos en un hilo secundario
            val db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "roomDB"
            ).build()

            val personaDao = db.personaDao()

            // Obtener la persona por su ID
            val persona: Persona = personaDao.getPersonaById(id)

            // Eliminar la persona de la base de datos
            personaDao.deletePersona(persona)
            finish()
        }
    }



    private fun actualizarPersona(id: Int) {
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "roomDB").build()
        val personaDao = db.personaDao()
        val estadoDao = db.estadoDao()

        val id = id
        val nombreNuevo = nameInput.text.toString()
        val edadNuevo = ageInput.text.toString().toInt()

        val nombreEstadoSeleccionado = spinnerEstado.selectedItem.toString()
        var estadoNuevo: Estado

        CoroutineScope(Dispatchers.IO).launch {
            // Obtener el estado de la base de datos
            estadoNuevo = estadoDao.getEstadoByName(nombreEstadoSeleccionado)

            // Acceder a stateId dentro del mismo contexto de la corrutina
            val stateId = estadoNuevo.stateId

            withContext(Dispatchers.Main) {
                if (stateId != null) {
                    // Actualizar la persona en la base de datos
                    val personaActualizada = Persona(id, nombreNuevo, edadNuevo, stateId)
                    CoroutineScope(Dispatchers.IO).launch {
                        // Actualizar la persona en la base de datos
                        personaDao.updatePersona(personaActualizada)
                    }
                    finish()
                } else {
                    // Manejar el caso en el que el estado no se pudo obtener
                    Toast.makeText(applicationContext, "Error al obtener el estado seleccionado", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }


}