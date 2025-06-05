package com.cogu.spylook.view.common.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cogu.spylook.R
import com.cogu.spylook.adapters.cards.AnotacionCardAdapter
import com.cogu.spylook.database.AppDatabase
import com.cogu.spylook.mappers.AnotacionToCardItem
import com.cogu.spylook.model.cards.AnotacionCardItem
import com.cogu.spylook.model.entity.AnotableEntity
import com.cogu.spylook.model.entity.AnotacionEntity
import com.cogu.spylook.model.utils.converters.DateConverters
import com.cogu.spylook.model.utils.decorators.SpacingItemDecoration
import kotlinx.coroutines.runBlocking
import org.mapstruct.factory.Mappers
import java.time.LocalDateTime

class AnotacionesFragment(private val anotableEntity: AnotableEntity, val contexto : Context) : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private val mapper = Mappers.getMapper(AnotacionToCardItem::class.java)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragment = inflater.inflate(R.layout.fragment_empty_recycler, container, false)
        initializeRecyclerView(fragment)

        return fragment
    }

    private fun initializeRecyclerView(fragment: View) {
        recyclerView = fragment.findViewById(R.id.recyclerGeneric)
        val db = AppDatabase.Companion.getInstance(requireContext())!!.anotacionDAO()
        runBlocking {
            val anotaciones = db!!.getAnotacionesDeAnotable(anotableEntity.idAnotable)
            val cardItems = buildCardItemList(anotaciones)
            setupRecyclerView(cardItems)
        }
    }

    private fun buildCardItemList(anotaciones: List<AnotacionEntity>): MutableList<AnotacionCardItem> {
        val cardItems = anotaciones.map { mapper.toCardItem(it) }.toMutableList()
        cardItems.add(
            AnotacionCardItem(
                id = -1,
                titulo = "Nueva Anotacion",
                descripcion = "",
                fecha = DateConverters.toDateTimeString(LocalDateTime.now())!!,
                idAnotable = anotableEntity.idAnotable
            )
        )
        cardItems.sortBy { it.id }
        return cardItems
    }

    private fun setupRecyclerView(cardItems: MutableList<AnotacionCardItem>) {
        val adapter = AnotacionCardAdapter(cardItems, contexto, anotableEntity.idAnotable)
        recyclerView.layoutManager = LinearLayoutManager(contexto)
        recyclerView.adapter = adapter
        if (recyclerView.itemDecorationCount == 0) {
            recyclerView.addItemDecoration(SpacingItemDecoration(contexto))
        }
    }

}