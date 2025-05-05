package com.cogu.spylook.model.textWatchers

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cogu.spylook.R
import com.cogu.spylook.adapters.PersonaCardAdapter
import com.cogu.spylook.bbdd.AppDatabase
import com.cogu.spylook.mappers.ContactoToCardItem
import com.cogu.spylook.model.cards.ContactoCardItem
import com.cogu.spylook.model.entity.Contacto
import kotlinx.coroutines.runBlocking
import org.mapstruct.factory.Mappers
import java.util.Locale
import java.util.stream.Collectors

class TextWatcherSearchBar(
    private val text: EditText,
    private val recyclerView: RecyclerView?,
    private val adapter: PersonaCardAdapter?,
    private val context: Context?
) : TextWatcher {
    private val mapper: ContactoToCardItem
    private val db: AppDatabase

    init {
        this.mapper = Mappers.getMapper<ContactoToCardItem>(ContactoToCardItem::class.java)
        this.db = AppDatabase.getInstance(context!!)!!
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (text.getText().toString().isEmpty()) {
            recyclerView!!.setLayoutManager(LinearLayoutManager(context))
            recyclerView.setAdapter(adapter)
        } else {
            runBlocking { val contactos: List<Contacto?>? =
                db.contactoDAO()!!.getContactos();
                val collect = contactos!!.stream()
                    .filter { i: Contacto? ->
                        i!!.alias!!.lowercase(Locale.getDefault()).contains(
                            text.getText().toString().lowercase(
                                Locale.getDefault()
                            )
                        )
                    }
                    .map<ContactoCardItem?> { contacto: Contacto? -> mapper.toCardItem(contacto) }
                    .collect(Collectors.toList())
                if (collect.isEmpty()) {
                    collect.add(
                        ContactoCardItem(
                            0,
                            "",
                            "Sin resultados",
                            R.drawable.notfound,
                            false
                        )
                    )
                }

            val newAdapter = PersonaCardAdapter(collect, context!!)
            recyclerView!!.setLayoutManager(LinearLayoutManager(context))
            recyclerView.setAdapter(newAdapter)
            }
        }
    }

    override fun afterTextChanged(s: Editable?) {
    }
}

