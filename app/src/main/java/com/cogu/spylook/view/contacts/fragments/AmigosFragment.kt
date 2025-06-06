package com.cogu.spylook.view.contacts.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cogu.spylook.R
import com.cogu.spylook.adapters.cards.AmigoCardAdapter
import com.cogu.spylook.database.AppDatabase
import com.cogu.spylook.dao.ContactoDAO
import com.cogu.spylook.mappers.ContactoToCardItem
import com.cogu.spylook.model.cards.ContactoCardItem
import com.cogu.spylook.model.entity.Contacto
import com.cogu.spylook.model.utils.decorators.SpacingItemDecoration
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.mapstruct.factory.Mappers

class AmigosFragment(private val contacto: Contacto, private val context: Context?) : Fragment() {

    private val mapper: ContactoToCardItem = Mappers.getMapper(ContactoToCardItem::class.java)
    var amigos = mutableListOf<ContactoCardItem>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_amigos, container, false)

        setupRecyclerView(rootView.findViewById(R.id.recycleramigos))

        return rootView
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) = lifecycleScope.launch {
        val contactoDAO = getContactoDAO()
        amigos = fetchAmigos(contactoDAO)
        initializeRecyclerView(recyclerView, amigos)
    }

    private fun getContactoDAO(): ContactoDAO {
        val db = AppDatabase.getInstance(requireContext())
        return db!!.contactoDAO()!!
    }

    private suspend fun fetchAmigos(contactoDAO: ContactoDAO): MutableList<ContactoCardItem> {
        val busquedaComoContacto = contactoDAO.getAmistadesPorContacto(contacto.idAnotable).map {
            contactoDAO.findContactoById(it.idAmigo)
        }.toMutableList()
        val busquedaComoAmigo = contactoDAO.getContactosPorAmigo(contacto.idAnotable).map {
            contactoDAO.findContactoById(it.idContacto)
        }.toMutableList()
        busquedaComoAmigo.addAll(busquedaComoContacto)
        val amigos = busquedaComoAmigo.distinct().map {
            mapper.toCardItem(it)
        }.toMutableList()
        amigos.add(ContactoCardItem(
            idAnotable = -1,
            nombre = "Añade más amigos",
            alias = "Agregar",
            colorFoto = R.drawable.notfound,
            clickable = false
        ))
        return amigos
    }

    private fun initializeRecyclerView(recyclerView: RecyclerView, amigos: List<ContactoCardItem>) {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = AmigoCardAdapter(amigos.toMutableList(), requireContext(), contacto)
        recyclerView.addItemDecoration(SpacingItemDecoration(requireContext()))
    }

}