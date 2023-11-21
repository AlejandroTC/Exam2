package com.example.exam2.room

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class PersonasEnEstado(
    @Embedded val persona: Persona,
    @Relation(
        parentColumn = "personId",
        entityColumn = "stateId",
        associateBy = Junction(PersonaEstadoCruzados::class)
    )
    val estado: Estado
)
