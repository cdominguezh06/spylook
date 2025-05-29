package com.cogu.spylook.view.sucesos.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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

    private lateinit var recyclerViewCausante: RecyclerView
    private lateinit var recyclerViewImplicados: RecyclerView
    private lateinit var descripcion: TextView
    private lateinit var fecha: TextView
    private lateinit var lugar: TextView
    private val mapper = Mappers.getMapper(ContactoToCardItem::class.java)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragment = inflater.inflate(R.layout.fragment_suceso_data, container, false)
        initializeTexts(fragment)
        initializeRecyclerViews(fragment)

        return fragment
    }

    private fun initializeTexts(fragment: View) {
        descripcion = fragment.findViewById<TextView>(R.id.descripcionText)
        descripcion.text = suceso.descripcion
        fecha = fragment.findViewById<TextView>(R.id.sucesoFechaText)
        fecha.text = suceso.fecha
        lugar = fragment.findViewById<TextView>(R.id.sucesoLugarText)
        lugar.text = suceso.lugar

    }

    private fun initializeRecyclerViews(fragment: View) {
        recyclerViewCausante = fragment.findViewById(R.id.recyclerCreador)
        recyclerViewImplicados = fragment.findViewById(R.id.recyclerMiembros)
        val sucesoDao = AppDatabase.getInstance(requireContext())!!.sucesoDAO()
        val contactoDao = AppDatabase.getInstance(requireContext())!!.contactoDAO()
        runBlocking {
            val implicados = sucesoDao!!.getRelacionesBySuceso(suceso.idAnotable).map {
                contactoDao!!.findContactoById(it.idContacto)
            }
            val causante = contactoDao!!.findContactoById(suceso.idCausante)
            recyclerViewCausante.layoutManager = LinearLayoutManager(requireContext())
            recyclerViewCausante.adapter = ContactoCardAdapter(mutableListOf(mapper.toCardItem(causante)), requireContext())
            if (recyclerViewCausante.itemDecorationCount == 0) {
                recyclerViewCausante.addItemDecoration(SpacingItemDecoration(requireContext()))
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
        recyclerViewImplicados.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewImplicados.adapter = adapterMiembros
        if (recyclerViewImplicados.itemDecorationCount == 0) {
            recyclerViewImplicados.addItemDecoration(SpacingItemDecoration(requireContext()))
        }
    }
}