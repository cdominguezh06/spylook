package com.cogu.spylook.view.contacts.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cogu.data.database.AppDatabase
import com.cogu.data.mappers.toModel
import com.cogu.domain.model.Contacto
import com.cogu.spylook.R
import com.cogu.spylook.adapters.cards.GruposDeContactoCardAdapter
import com.cogu.spylook.mappers.toCardItem
import com.cogu.spylook.model.cards.GrupoCardItem
import com.cogu.spylook.model.utils.decorators.SpacingItemDecoration
import kotlinx.coroutines.launch

class ContactGroupsFragment(private val contacto: Contacto) : Fragment() {

    private lateinit var recyclerView: RecyclerView

    companion object{
        var grupos = mutableListOf<GrupoCardItem>()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        grupos = mutableListOf<GrupoCardItem>()
        val fragment = inflater.inflate(R.layout.fragment_empty_recycler, container, false)
        initializeRecyclerView(fragment)

        return fragment
    }

    private fun initializeRecyclerView(fragment: View) {
        recyclerView = fragment.findViewById(R.id.recyclerGeneric)
        val db = AppDatabase.getInstance(requireContext())!!.grupoDAO()
        lifecycleScope.launch {
            grupos = db!!.findGruposByMiembro(contacto.idAnotable).map {
                db.findGrupoById(it.idGrupo)!!.toModel().toCardItem()
            }.toMutableList()
            grupos.addAll(
                db.findGruposByCreador(contacto.idAnotable).map {
                    it.toModel().toCardItem()
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