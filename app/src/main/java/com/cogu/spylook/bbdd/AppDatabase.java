package com.cogu.spylook.bbdd;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.cogu.spylook.DAO.ContactoDAO;
import com.cogu.spylook.model.entity.Anotacion;
import com.cogu.spylook.model.entity.Contacto;
import com.cogu.spylook.model.entity.Grupo;
import com.cogu.spylook.model.entity.Suceso;

@Database(entities = {Contacto.class, Anotacion.class, Grupo.class, Suceso.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;

    public abstract ContactoDAO userDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "app_database")
                            .fallbackToDestructiveMigration() // Manejo de migraciones simple.
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
