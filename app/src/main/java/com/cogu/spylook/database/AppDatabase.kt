package com.cogu.spylook.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.cogu.spylook.dao.AnotacionDAO
import com.cogu.spylook.dao.ContactoDAO
import com.cogu.spylook.dao.CuentaDao
import com.cogu.spylook.dao.GrupoDAO
import com.cogu.spylook.dao.SucesoDAO
import com.cogu.spylook.model.utils.converters.DateConverters
import com.cogu.spylook.model.entity.Anotacion
import com.cogu.spylook.model.entity.Contacto
import com.cogu.spylook.model.entity.ContactoAmistadCrossRef
import com.cogu.spylook.model.entity.ContactoGrupoCrossRef
import com.cogu.spylook.model.entity.ContactoSucesoCrossRef
import com.cogu.spylook.model.entity.Cuenta
import com.cogu.spylook.model.entity.CuentaContactoCrossRef
import com.cogu.spylook.model.entity.Grupo
import com.cogu.spylook.model.entity.Suceso

@Database(
    entities = [
        Contacto::class,
        Anotacion::class,
        Grupo::class,
        Suceso::class,
        Cuenta::class,
        ContactoSucesoCrossRef::class,
        ContactoGrupoCrossRef::class,
        ContactoAmistadCrossRef::class,
        CuentaContactoCrossRef::class
    ], version = 2, exportSchema = false
)
@TypeConverters(DateConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun contactoDAO(): ContactoDAO?
    abstract fun sucesoDAO(): SucesoDAO?
    abstract fun grupoDAO(): GrupoDAO?
    abstract fun anotacionDAO(): AnotacionDAO?
    abstract fun cuentaDAO(): CuentaDao?

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
                            .fallbackToDestructiveMigration(true)
                            .build()
                    }
                }
            }
            return INSTANCE
        }
    }
}
