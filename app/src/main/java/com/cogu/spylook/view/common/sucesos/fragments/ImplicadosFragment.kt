package com.cogu.spylook.view.common.sucesos.fragments

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
import com.cogu.spylook.model.entity.Suceso
import com.cogu.spylook.model.utils.decorators.SpacingItemDecoration
import kotlinx.coroutines.runBlocking
import org.mapstruct.factory.Mappers

class ImplicadosFragment(private val suceso: Suceso) : Fragment() {

    private lateinit var recycler: RecyclerView
    private val mapper = Mappers.getMapper(ContactoToCardItem::class.java)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragment = inflater.inflate(R.layout.fragment_empty_recycler, container, false)
        initializeRecyclerView(fragment)

        return fragment
    }

    private fun initializeRecyclerView(fragment: View) {
        recycler = fragment.findViewById(R.id.recyclerGeneric)
        val sucesoDao = AppDatabase.getInstance(requireContext())!!.sucesoDAO()
        val contactoDao = AppDatabase.getInstance(requireContext())!!.contactoDAO()
        runBlocking {
            val implicados = sucesoDao!!.getRelacionesBySuceso(suceso.idAnotable).map {
                contactoDao!!.findContactoById(it.idContacto)
            }
            val cardItems = buildCardItemList(implicados)
            setupMemberRecyclerView(cardItems)
        }
    }

    private fun buildCardItemList(miembros: List<Contacto>): MutableList<ContactoCardItem> {
        val cardItems = miembros.map { mapper.toCardItem(it) }.toMutableList()
        return cardItems
    }

    private fun setupMemberRecyclerView(cardItems: MutableList<ContactoCardItem>) {
        val adapterMiembros = ContactoCardAdapter(cardItems, requireContext())
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapterMiembros
        if (recycler.itemDecorationCount == 0) {
            recycler.addItemDecoration(SpacingItemDecoration(requireContext()))
        }
    }
}