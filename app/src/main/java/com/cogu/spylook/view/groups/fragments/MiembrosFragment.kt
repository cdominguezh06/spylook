package com.cogu.spylook.view.groups.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cogu.spylook.R
import com.cogu.spylook.adapters.cards.ContactoCardAdapter
import com.cogu.spylook.database.AppDatabase
import com.cogu.spylook.mappers.ContactoToCardItem
import com.cogu.spylook.model.cards.ContactoCardItem
import com.cogu.spylook.model.entity.Contacto
import com.cogu.spylook.model.entity.Grupo
import com.cogu.spylook.model.utils.decorators.SpacingItemDecoration
import kotlinx.coroutines.runBlocking
import org.mapstruct.factory.Mappers

class MiembrosFragment(private val grupo: Grupo) : Fragment() {

    private lateinit var recyclerViewCreador: RecyclerView
    private lateinit var recyclerViewMiembros: RecyclerView
    private val mapper = Mappers.getMapper(ContactoToCardItem::class.java)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragment = inflater.inflate(R.layout.fragment_group_members, container, false)
        initializeRecyclerViews(fragment)

        return fragment
    }

    private fun initializeRecyclerViews(fragment: View) {
        recyclerViewCreador = fragment.findViewById(R.id.recyclerCreador)
        recyclerViewMiembros = fragment.findViewById(R.id.recyclerMiembros)
        val grupoDao = AppDatabase.getInstance(requireContext())!!.grupoDAO()
        val contactoDao = AppDatabase.getInstance(requireContext())!!.contactoDAO()
        runBlocking {
            val miembros = grupoDao!!.obtenerRelacionesPorGrupo(grupo.idAnotable).map {
                contactoDao!!.findContactoById(it.idContacto)
            }
            val creador = contactoDao!!.findContactoById(grupo.idCreador)
            recyclerViewCreador.layoutManager = LinearLayoutManager(requireContext())
            recyclerViewCreador.adapter = ContactoCardAdapter(mutableListOf(mapper.toCardItem(creador)), requireContext())
            if (recyclerViewCreador.itemDecorationCount == 0) {
                recyclerViewCreador.addItemDecoration(SpacingItemDecoration(requireContext()))
            }
            val cardItems = buildCardItemList(miembros)
            setupMemberRecyclerView(cardItems)
        }
    }

    private fun buildCardItemList(miembros: List<Contacto>): MutableList<ContactoCardItem> {
        val cardItems = miembros.map { mapper.toCardItem(it) }.toMutableList()
        return cardItems
    }

    private fun setupMemberRecyclerView(cardItems: MutableList<ContactoCardItem>) {
        val adapterMiembros = ContactoCardAdapter(cardItems, requireContext())
        recyclerViewMiembros.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewMiembros.adapter = adapterMiembros
        if (recyclerViewMiembros.itemDecorationCount == 0) {
            recyclerViewMiembros.addItemDecoration(SpacingItemDecoration(requireContext()))
        }
    }
}