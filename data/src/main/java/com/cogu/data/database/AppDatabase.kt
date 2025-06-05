package com.cogu.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.cogu.data.DateConverters
import com.cogu.data.crossrefs.ContactoAmistadCrossRef
import com.cogu.data.crossrefs.ContactoGrupoCrossRef
import com.cogu.data.crossrefs.ContactoSucesoCrossRef
import com.cogu.data.crossrefs.CuentaContactoCrossRef
import com.cogu.data.dao.AnotacionDAO
import com.cogu.data.dao.ContactoDAO
import com.cogu.data.dao.CuentaDao
import com.cogu.data.dao.GrupoDAO
import com.cogu.data.dao.SucesoDAO
import com.cogu.data.entity.AnotableEntity
import com.cogu.data.entity.AnotacionEntity
import com.cogu.data.entity.ContactoEntity
import com.cogu.data.entity.CuentaEntity
import com.cogu.data.entity.GrupoEntity
import com.cogu.data.entity.SucesoEntity

@Database(
    entities = [
        ContactoEntity::class,
        AnotacionEntity::class,
        GrupoEntity::class,
        SucesoEntity::class,
        CuentaEntity::class,
        ContactoSucesoCrossRef::class,
        ContactoGrupoCrossRef::class,
        ContactoAmistadCrossRef::class,
        CuentaContactoCrossRef::class,
        AnotableEntity::class
    ], version = 11, exportSchema = false
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
