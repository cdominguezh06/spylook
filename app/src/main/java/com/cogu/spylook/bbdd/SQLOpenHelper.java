package com.cogu.spylook.bbdd;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class SQLOpenHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "gente.db";
    private static final int DATABASE_VERSION = 1;
    private static SQLOpenHelper instance;
    private Context context;

    private SQLOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }

    public static SQLOpenHelper getInstance(Context context) {
        if (instance == null){
            instance = new SQLOpenHelper(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createContacto(db);
        createSuceso(db);
        createGrupo(db);
        createAnotacion(db);
        createCuenta(db);
    }

    private void createCuenta(SQLiteDatabase db) {
        String sql = "CREATE TABLE cuenta(" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    nickname VARCHAR(255)," +
                "    enlace varchar(255)," +
                "    plataforma INTEGER" +
                ");";
        String sql2="CREATE TABLE contacto_cuenta(" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    propietario_id INTEGER," +
                "    cuenta_id INTEGER," +
                "    FOREIGN KEY (propietario_id) REFERENCES contacto(id)," +
                "    FOREIGN KEY (cuenta_id) REFERENCES cuenta(id)" +
                ");";
        try {
            db.execSQL(sql);
            db.execSQL(sql2);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this.context, "Error al crear la tabla;", Toast.LENGTH_SHORT).show();
        }
    }

    private void createAnotacion(SQLiteDatabase db) {
        String sql = "CREATE TABLE anotacion(" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    descripcion TEXT," +
                "    contacto_id INTEGER,"+
                "    FOREIGN KEY (contacto_id) REFERENCES contacto(id)" +
                ");";
        try {
            db.execSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this.context, "Error al crear la tabla;", Toast.LENGTH_SHORT).show();
        }
    }

    private void createGrupo(SQLiteDatabase db) {
        String sql = "CREATE TABLE grupo(" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    nombre VARCHAR(255)," +
                "    grupoImg INTEGER," +
                "    creador_id INTEGER," +
                "    FOREIGN KEY (creador_id) REFERENCES contacto(id)" +
                ");";
        String sql2="CREATE TABLE contacto_grupo(" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    miembro_id INTEGER," +
                "    grupo_id INTEGER," +
                "    FOREIGN KEY (miembro_id) REFERENCES contacto(id)," +
                "    FOREIGN KEY (grupo_id) REFERENCES grupo(id)" +
                ");";
        try {
            db.execSQL(sql);
            db.execSQL(sql2);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this.context, "Error al crear la tabla;", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void createContacto(SQLiteDatabase db) {
        String sql = "CREATE TABLE contacto(" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    nombre varchar(255)," +
                "    nickMasConocido varchar(255),"+
                "    foto INTEGER,"+
                "    edad INTEGER," +
                "    fechaNacimiento DATE," +
                "    ciudad VARCHAR(255)," +
                "    estado VARCHAR(255)," +
                "    pais VARCHAR(255)" +
                ");";
        String sql2 = "CREATE TABLE contacto_amigo(" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    persona_id INTEGER," +
                "    amigo_id INTEGER," +
                "    FOREIGN KEY (persona_id) REFERENCES contacto(id)," +
                "    FOREIGN KEY (amigo_id) REFERENCES contacto(id)" +
                ");";
        try {
           db.execSQL(sql);
           db.execSQL(sql2);
        } catch (Exception e) {
            Toast.makeText(this.context, "Error al crear la tabla;", Toast.LENGTH_SHORT).show();
        }
    }
    private void createSuceso(SQLiteDatabase db) {
        String sql = "CREATE TABLE suceso(" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    fecha DATE," +
                "    descripcion TEXT," +
                "    causante_id INTEGER," +
                "    FOREIGN KEY (causante_id) REFERENCES contacto(id)" +
                ");";
        String sql2= "CREATE TABLE suceso_contacto(" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    participante_id INTEGER," +
                "    suceso_id INTEGER," +
                "    FOREIGN KEY (participante_id) REFERENCES contacto(id)," +
                "    FOREIGN KEY (suceso_id) REFERENCES Suceso(id)" +
                ");";
        try {
            db.execSQL(sql);
            db.execSQL(sql2);
        } catch (Exception e) {
            Toast.makeText(this.context, "Error al crear la tabla;", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String[] sqls = {"DROP TABLE IF EXISTS contacto",
                "DROP TABLE IF EXISTS contacto_amigo",
                "DROP TABLE IF EXISTS contacto_grupo",
                "DROP TABLE IF EXISTS contacto_anotacion",
                "DROP TABLE IF EXISTS contacto_cuenta",
                "DROP TABLE IF EXISTS cuenta",
                "DROP TABLE IF EXISTS grupo",
                "DROP TABLE IF EXISTS suceso",
                "DROP TABLE IF EXISTS suceso_contacto"};
        try {
            for (String sql : sqls) {
                db.execSQL(sql);
            }
            this.onCreate(db);
        } catch (SQLException e) {
            Toast.makeText(this.context, "Error al reestablecer la tabla;", Toast.LENGTH_SHORT).show();
        }
    }
}