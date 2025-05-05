package com.cogu.spylook.bbdd;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.cogu.spylook.DAO.AnotacionDAO;
import com.cogu.spylook.DAO.ContactoDAO;
import com.cogu.spylook.DAO.GrupoDAO;
import com.cogu.spylook.DAO.SucesoDAO;
import com.cogu.spylook.model.converters.DateConverters;
import com.cogu.spylook.model.entity.Anotacion;
import com.cogu.spylook.model.entity.Contacto;
import com.cogu.spylook.model.entity.ContactoAmistadCrossRef;
import com.cogu.spylook.model.entity.ContactoGrupoCrossRef;
import com.cogu.spylook.model.entity.ContactoSucesoCrossRef;
import com.cogu.spylook.model.entity.Grupo;
import com.cogu.spylook.model.entity.Suceso;

@Database(
        entities = {
                Contacto.class,
                ContactoSucesoCrossRef.class,
                ContactoGrupoCrossRef.class,
                ContactoAmistadCrossRef.class,
                Anotacion.class,
                Grupo.class,
                Suceso.class
        }, version = 1, exportSchema = false)
@TypeConverters({DateConverters.class})
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;

    public abstract ContactoDAO contactoDAO();
    public abstract SucesoDAO sucesoDAO();
    public abstract GrupoDAO grupoDAO();
    public abstract AnotacionDAO anotacionDAO();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "app_database.db")
                            .fallbackToDestructiveMigration() // Manejo de migraciones simple.
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
