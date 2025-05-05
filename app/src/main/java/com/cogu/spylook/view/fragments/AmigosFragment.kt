package com.cogu.spylook.view.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cogu.spylook.DAO.ContactoDAO
import com.cogu.spylook.R
import com.cogu.spylook.adapters.PersonaCardAdapter
import com.cogu.spylook.bbdd.AppDatabase
import com.cogu.spylook.mappers.ContactoToCardItem
import com.cogu.spylook.model.cards.ContactoCardItem
import com.cogu.spylook.model.decorators.SpacingItemDecoration
import com.cogu.spylook.model.entity.Contacto
import com.cogu.spylook.model.relationships.AmigosDeContacto
import kotlinx.coroutines.runBlocking
import org.mapstruct.factory.Mappers
import java.util.stream.Collectors

class AmigosFragment(private val contacto: Contacto, private val context: Context?) :
    androidx.fragment.app.Fragment() {
    private val mapper: ContactoToCardItem = Mappers.getMapper<ContactoToCardItem>(ContactoToCardItem::class.java)
    private val db: AppDatabase = AppDatabase.getInstance(context)
    private val contactoDAO: ContactoDAO = db.contactoDAO()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragment = inflater.inflate(R.layout.fragment_amigos, container, false)
        val recyclerView: RecyclerView = fragment.findViewById<RecyclerView>(R.id.recycleramigos)
        runBlocking {
            val amigosDeContacto: AmigosDeContacto? =
                contactoDAO.getAmigosDeContacto(contacto.getId())
            val collect = amigosDeContacto!!.amigos.stream()
                .map<ContactoCardItem?> { contacto: Contacto? -> mapper.toCardItem(contacto) }
                .collect(Collectors.toList())
            if (collect.isEmpty()) {
                collect.add(
                    ContactoCardItem(
                        "Error",
                        "No hay amigos",
                        R.drawable.notfound,
                        false
                    )
                )
            }
            recyclerView.setLayoutManager(LinearLayoutManager(context))
            recyclerView.setAdapter(PersonaCardAdapter(collect, context))
            recyclerView.addItemDecoration(SpacingItemDecoration(context))
        }

        return fragment
    }
}
