package com.cogu.spylook.view.contacts.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cogu.data.database.AppDatabase
import com.cogu.data.mappers.toModel
import com.cogu.domain.model.Anotacion
import com.cogu.domain.model.Contacto
import com.cogu.spylook.R
import com.cogu.spylook.adapters.cards.AnotacionCardAdapter
import com.cogu.spylook.mappers.toCardItem
import com.cogu.spylook.model.cards.AnotacionCardItem
import com.cogu.spylook.model.utils.decorators.SpacingItemDecoration
import kotlinx.coroutines.launch
import org.mapstruct.factory.Mappers

class InformacionFragment(private val contacto: Contacto) : Fragment() {

    private val ID_RECYCLER_VIEW = R.id.recyclerAnotaciones
    private val camposTextViewIds = mapOf(
        "edad" to R.id.edadContent,
        "nick" to R.id.nickContent,
        "fecha" to R.id.fechaContent,
        "ciudad" to R.id.ciudadContent,
        "estado" to R.id.estadoContent,
        "pais" to R.id.paisContent
    )

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragment = inflater.inflate(R.layout.fragment_contact_data, container, false)

        bindStaticFields(fragment)
        initializeRecyclerView(fragment)

        return fragment
    }

    private fun bindStaticFields(fragment: View) {
        camposTextViewIds.entries.forEach { (field, id) ->
            val textView = fragment.findViewById<TextView>(id)
            textView.text = when (field) {
                "edad" -> contacto.edad.toString()
                "nick" -> contacto.alias
                "fecha" -> contacto.fechaNacimiento.toString()
                "ciudad" -> contacto.ciudad
                "estado" -> contacto.estado
                "pais" -> contacto.pais
                else -> ""
            }
        }
    }

    private fun initializeRecyclerView(fragment: View) {
        recyclerView = fragment.findViewById(ID_RECYCLER_VIEW)
        val db = AppDatabase.getInstance(requireContext())!!.anotacionDAO()
        lifecycleScope.launch {
            val anotaciones = db!!.getAnotacionesDeAnotable(contacto.idAnotable)
                .map { it.toModel() }
            val cardItems = buildCardItemList(anotaciones)
            setupRecyclerView(cardItems)
        }
    }

    private fun buildCardItemList(anotaciones: List<Anotacion>): MutableList<AnotacionCardItem> {
        val cardItems = anotaciones.map { it.toCardItem() }.toMutableList()
        cardItems.add(
            AnotacionCardItem.getDefaultForNew(contacto.idAnotable)
        )
        cardItems.sortBy { it.id }
        return cardItems
    }

    private fun setupRecyclerView(cardItems: MutableList<AnotacionCardItem>) {
        val adapter = AnotacionCardAdapter(cardItems, requireContext(), contacto.idAnotable)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        if (recyclerView.itemDecorationCount == 0) {
            recyclerView.addItemDecoration(SpacingItemDecoration(requireContext()))
        }
    }
}