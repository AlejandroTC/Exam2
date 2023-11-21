package com.example.exam2.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface PCEDAO {
    @Transaction
    @Query("SELECT persona.*, estado.stateId AS estadoId FROM persona INNER JOIN estado ON persona.id_estado = estado.stateId WHERE estado.stateId = :estadoId")
    fun getPersonasEnEstadoByEstadoId(estadoId: Long): List<PersonasEnEstado>

}