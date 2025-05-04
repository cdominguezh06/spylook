package com.cogu.spylook.bbdd;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cogu.spylook.R;
import com.cogu.spylook.model.entity.Contacto;
import com.cogu.spylook.model.entity.Grupo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SQLAdapter {
    private SQLOpenHelper sqlOpenHelper;
    private SQLiteDatabase db;

    public SQLAdapter(Context context) {
        this.sqlOpenHelper = SQLOpenHelper.getInstance(context);
    }

    public List<Contacto> getContactos() {
        this.db = sqlOpenHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from contacto", null);
        List<Contacto> contactos = new ArrayList<>();
        while (cursor.moveToNext()) {
            Contacto contacto = new Contacto(cursor.getInt(0), cursor.getString(1), cursor.getString(2), LocalDate.parse(Optional.ofNullable(cursor.getString(3)).orElse(LocalDate.now().toString())), cursor.getString(4), cursor.getString(5), cursor.getString(6));
            if (contacto.getFoto() == 0){
                contacto.setFoto(R.drawable.ratona);
            }
            contactos.add(contacto);
        }
        db.close();
        return contactos;
    }

    public boolean addContacto(Contacto contacto) {
        this.db = sqlOpenHelper.getWritableDatabase();
        try {
            db.execSQL("INSERT INTO contacto (nombre, nickMasConocido, fechaNacimiento, ciudad, estado, pais) VALUES ('" + contacto.getNombre() + "','" + contacto.getAlias() + "','" + contacto.getFechaNacimiento() + "','" + contacto.getCiudad()+ "','" + contacto.getEstado() + "','" + contacto.getPais() +"');");
            return true;
        } catch (Exception e) {
            db.close();
            return false;
        }
    }

    public Contacto findContactoById(int id) {
        this.db = sqlOpenHelper.getWritableDatabase();
        Cursor contacto = db.rawQuery("Select * from contacto where id = " + id, null);
        if (contacto.moveToNext()) {
            return new Contacto(contacto.getInt(0), contacto.getString(1), contacto.getString(2), LocalDate.parse(Optional.ofNullable(contacto.getString(3)).orElse(LocalDate.now().toString())), contacto.getString(4), contacto.getString(5), contacto.getString(6));
        }
        return null;
    }

    public Grupo findGrupoById(int id) {
        this.db = sqlOpenHelper.getWritableDatabase();
        Cursor grupo = db.rawQuery("Select * from grupo where id = " + id, null);
        if (grupo.moveToNext()) {
            return new Grupo(grupo.getInt(0), grupo.getString(1), grupo.getInt(2), findContactoById(grupo.getInt(3)));
        }
        return null;

    }

    public List<Contacto> getAmigos(Contacto contacto) {
        this.db = sqlOpenHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select id from contacto_amigo where persona_id = " + contacto.getId(), null);
        List<Contacto> contactos = new ArrayList<>();
        while (cursor.moveToNext()) {
            contactos.add(findContactoById(cursor.getInt(2)));
        }
        db.close();
        return contactos;
    }

//    public boolean addAvance(AvanceCientifico avanceCientifico) {
//        this.db = sqlOpenHelper.getWritableDatabase();
//        try {
//            db.execSQL("INSERT INTO avancescientificos (nombre, descripcion, idClient) VALUES ('" + avanceCientifico.getNombre() + "','" + avanceCientifico.getDescripcion() + "'," + avanceCientifico.getIdClient() + ")");
//        } catch (Exception e) {
//            db.close();
//            return false;
//        }
//        db.close();
//        return true;
//    }
//
//    public Optional<AvanceCientifico> findAvanceById(int id) {
//        this.db = sqlOpenHelper.getWritableDatabase();
//        Cursor avance = db.rawQuery("Select * from avancescientificos where id = " + id, null);
//        if (avance.moveToNext()) {
//            return Optional.of(new AvanceCientifico(avance.getInt(0), avance.getString(1), avance.getString(2), avance.getInt(3)));
//        }
//        return Optional.empty();
//    }
//
//    public boolean updateAvance(int idAvance, AvanceCientifico avanceCientifico) {
//        this.db = sqlOpenHelper.getWritableDatabase();
//        try {
//            ContentValues values = new ContentValues();
//            values.put("nombre", avanceCientifico.getNombre());
//            values.put("descripcion", avanceCientifico.getDescripcion());
//            values.put("idClient", avanceCientifico.getIdClient());
//            db.update("avancescientificos", values, "id = ?", new String[]{idAvance + ""});
//        } catch (Exception e) {
//            db.close();
//            e.printStackTrace();
//            return false;
//        }
//        db.close();
//        return true;
//
//    }
//
//    public List<AvanceCientifico> getAvances() {
//        this.db = sqlOpenHelper.getWritableDatabase();
//        Cursor cursor = db.rawQuery("Select * from avancescientificos", null);
//        List<AvanceCientifico> avances = new ArrayList<>();
//        while (cursor.moveToNext()) {
//            AvanceCientifico avance = new AvanceCientifico(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3));
//            avances.add(avance);
//        }
//        db.close();
//        return avances;
//    }
}
