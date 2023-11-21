package com.example.exam2.room

import androidx.room.Entity

@Entity(primaryKeys = ["personId", "stateId"])
data class PersonaEstadoCruzados (
    val personId: Long,
    val stateId: Long
)