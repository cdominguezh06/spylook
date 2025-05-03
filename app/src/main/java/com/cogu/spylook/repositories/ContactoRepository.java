package com.cogu.spylook.repositories;

import android.content.Context;

import com.cogu.spylook.R;
import com.cogu.spylook.bbdd.SQLAdapter;
import com.cogu.spylook.model.Contacto;

import java.util.List;

import lombok.Data;

@Data
public class ContactoRepository {

    private List<Contacto> contactos;
    private SQLAdapter adapter;
    private static ContactoRepository instance;
    public ContactoRepository(Context context) {
        adapter = new SQLAdapter(context);
        contactos = adapter.getContactos();
    }
    public static ContactoRepository getInstance(Context context) {
        if (instance == null) {
            instance = new ContactoRepository(context);
        }
        return instance;
    }

    public boolean addContacto(Contacto contacto) {
        contacto.setFoto(R.drawable.ratona);
        if (adapter.addContacto(contacto)){
            contactos.add(contacto);
            return true;
        }
        return false;
    }

    public Contacto findContactoById(int id) {
        return adapter.findContactoById(id);
    }

    public List<Contacto> getAmigos(Contacto contacto) {
        return adapter.getAmigos(contacto);
    }
}
