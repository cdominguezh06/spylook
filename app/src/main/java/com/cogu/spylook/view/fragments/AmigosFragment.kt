package com.cogu.spylook.view.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cogu.spylook.R
import com.cogu.spylook.adapters.PersonaCardAdapter
import com.cogu.spylook.database.AppDatabase
import com.cogu.spylook.dao.ContactoDAO
import com.cogu.spylook.mappers.ContactoToCardItem
import com.cogu.spylook.model.cards.ContactoCardItem
import com.cogu.spylook.model.entity.Contacto
import com.cogu.spylook.model.relations.AmigosDeContacto
import com.cogu.spylook.model.utils.decorators.SpacingItemDecoration
import kotlinx.coroutines.runBlocking
import org.mapstruct.factory.Mappers

class AmigosFragment(private val contacto: Contacto, private val context: Context?) : Fragment() {

    private val mapper: ContactoToCardItem = Mappers.getMapper(ContactoToCardItem::class.java)

    private companion object {
        val ERROR_CARD_ITEM = ContactoCardItem(
            idContacto = -1,
            nombre = "Error",
            alias = "No hay amigos",
            colorFoto = R.drawable.notfound,
            clickable = false
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_amigos, container, false)

        setupRecyclerView(rootView.findViewById(R.id.recycleramigos))

        return rootView
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) = runBlocking {
        val contactoDAO = getContactoDAO()
        val amigos = fetchAmigos(contactoDAO)
        initializeRecyclerView(recyclerView, amigos)
    }

    private fun getContactoDAO(): ContactoDAO {
        val db = AppDatabase.getInstance(requireContext())
        return db!!.contactoDAO()!!
    }

    private suspend fun fetchAmigos(contactoDAO: ContactoDAO): List<ContactoCardItem> {
        val amigosDeContacto: AmigosDeContacto = contactoDAO.getAmigosDeContacto(contacto.idContacto)
        val amigos = amigosDeContacto.amigos?.mapNotNull { amigo ->
            mapper.toCardItem(amigo!!)
        } ?: emptyList()

        return amigos.ifEmpty { listOf(ERROR_CARD_ITEM) }
    }

    private fun initializeRecyclerView(recyclerView: RecyclerView, amigos: List<ContactoCardItem>) {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = PersonaCardAdapter(amigos, requireContext())
        recyclerView.addItemDecoration(SpacingItemDecoration(requireContext()))
    }
}