package com.cogu.spylook.view.contacts.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cogu.spylook.R
import com.cogu.spylook.adapters.cards.GrupoCardAdapter
import com.cogu.spylook.adapters.cards.GruposDeContactoCardAdapter
import com.cogu.spylook.database.AppDatabase
import com.cogu.spylook.mappers.GrupoToCardItem
import com.cogu.spylook.model.cards.GrupoCardItem
import com.cogu.spylook.model.entity.Contacto
import com.cogu.spylook.model.utils.decorators.SpacingItemDecoration
import kotlinx.coroutines.launch
import org.mapstruct.factory.Mappers

class ContactGroupsFragment(private val contacto: Contacto) : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private val mapper = Mappers.getMapper(GrupoToCardItem::class.java)

    companion object{
        var grupos = mutableListOf<GrupoCardItem>()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        grupos = mutableListOf<GrupoCardItem>()
        val fragment = inflater.inflate(R.layout.fragment_contact_groups, container, false)
        initializeRecyclerView(fragment)

        return fragment
    }

    private fun initializeRecyclerView(fragment: View) {
        recyclerView = fragment.findViewById(R.id.recyclerGrupos)
        val db = AppDatabase.getInstance(requireContext())!!.grupoDAO()
        lifecycleScope.launch {
            grupos = db!!.findGruposByMiembro(contacto.idAnotable).map {
                mapper.toCardItem(db.findGrupoById(it.idGrupo)!!)
            }.toMutableList()
            grupos.addAll(
                db.findGruposByCreador(contacto.idAnotable).map {
                    mapper.toCardItem(it)
                }
            )
            grupos.add(GrupoCardItem.ADD_TO_GROUP)
            setupRecyclerView(grupos)
        }
    }

    private fun setupRecyclerView(cardItems: MutableList<GrupoCardItem>) {
        val adapter = GruposDeContactoCardAdapter(cardItems, requireContext(), contacto)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        if (recyclerView.itemDecorationCount == 0) {
            recyclerView.addItemDecoration(SpacingItemDecoration(requireContext()))
        }
    }
}