package com.example.exam2.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Persona :: class, Estado :: class, PersonaEstadoCruzados :: class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun personaDao(): PersonaDAO
    abstract fun estadoDao(): EstadoDAO
    abstract fun PCEDao(): PCEDAO
}