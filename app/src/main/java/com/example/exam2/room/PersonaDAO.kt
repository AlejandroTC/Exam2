package com.example.exam2.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface PersonaDAO {
    @Query("SELECT * FROM persona")
    fun getAllPersonas(): List<Persona>

    @Query("SELECT * FROM persona WHERE personId = :idPersona")
    fun getPersonaById(idPersona: Int): Persona

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun newPersona(persona: Persona)

    @Update
    fun updatePersona(persona: Persona)

    @Delete
    fun deletePersona(persona: Persona)

}
