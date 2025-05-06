package com.cogu.spylook.bbdd

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.cogu.spylook.DAO.AnotacionDAO
import com.cogu.spylook.DAO.ContactoDAO
import com.cogu.spylook.DAO.GrupoDAO
import com.cogu.spylook.DAO.SucesoDAO
import com.cogu.spylook.model.converters.DateConverters
import com.cogu.spylook.model.entity.Anotacion
import com.cogu.spylook.model.entity.Contacto
import com.cogu.spylook.model.entity.ContactoAmistadCrossRef
import com.cogu.spylook.model.entity.ContactoGrupoCrossRef
import com.cogu.spylook.model.entity.ContactoSucesoCrossRef
import com.cogu.spylook.model.entity.Grupo
import com.cogu.spylook.model.entity.Suceso

@Database(
    entities = [Contacto::class, ContactoSucesoCrossRef::class, ContactoGrupoCrossRef::class, ContactoAmistadCrossRef::class, Anotacion::class, Grupo::class, Suceso::class
    ], version = 3, exportSchema = false
)
@TypeConverters(DateConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun contactoDAO(): ContactoDAO?
    abstract fun sucesoDAO(): SucesoDAO?
    abstract fun grupoDAO(): GrupoDAO?
    abstract fun anotacionDAO(): AnotacionDAO?

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java, "app_database.db"
                        )
                            .fallbackToDestructiveMigration(false)
                            .build()
                    }
                }
            }
            return INSTANCE
        }
    }
}
