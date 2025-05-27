package com.cogu.spylook.view.groups.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cogu.spylook.R
import com.cogu.spylook.adapters.AnotacionCardAdapter
import com.cogu.spylook.database.AppDatabase
import com.cogu.spylook.mappers.AnotacionToCardItem
import com.cogu.spylook.model.cards.AnotacionCardItem
import com.cogu.spylook.model.entity.Anotacion
import com.cogu.spylook.model.entity.Grupo
import com.cogu.spylook.model.utils.decorators.SpacingItemDecoration
import com.cogu.spylook.model.utils.converters.DateConverters
import kotlinx.coroutines.runBlocking
import org.mapstruct.factory.Mappers
import java.time.LocalDateTime

class GroupNotesFragment(private val grupo: Grupo) : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private val mapper = Mappers.getMapper(AnotacionToCardItem::class.java)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragment = inflater.inflate(R.layout.fragment_group_notes, container, false)
        initializeRecyclerView(fragment)

        return fragment
    }

    private fun initializeRecyclerView(fragment: View) {
        recyclerView = fragment.findViewById(R.id.recyclerNotas)
        val db = AppDatabase.getInstance(requireContext())!!.anotacionDAO()
        runBlocking {
            val anotaciones = db!!.getAnotacionesDeAnotable(grupo.idAnotable)
            val cardItems = buildCardItemList(anotaciones)
            setupRecyclerView(cardItems)
        }
    }

    private fun buildCardItemList(anotaciones: List<Anotacion>): MutableList<AnotacionCardItem> {
        val cardItems = anotaciones.map { mapper.toCardItem(it) }.toMutableList()
        cardItems.add(
            AnotacionCardItem(
                id = -1,
                titulo = "Nueva Anotacion",
                descripcion = "",
                fecha = DateConverters.toDateTimeString(LocalDateTime.now())!!,
                idAnotable = grupo.idAnotable
            )
        )
        cardItems.sortBy { it.id }
        return cardItems
    }

    private fun setupRecyclerView(cardItems: MutableList<AnotacionCardItem>) {
        val adapter = AnotacionCardAdapter(cardItems, requireContext(), grupo.idAnotable)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        if (recyclerView.itemDecorationCount == 0) {
            recyclerView.addItemDecoration(SpacingItemDecoration(requireContext()))
        }
    }
}