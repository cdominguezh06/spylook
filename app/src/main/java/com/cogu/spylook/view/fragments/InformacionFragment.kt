package com.cogu.spylook.view.fragments

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
import com.cogu.spylook.bbdd.AppDatabase
import com.cogu.spylook.mappers.AnotacionToCardItem
import com.cogu.spylook.model.cards.AnotacionCardItem
import com.cogu.spylook.model.utils.decorators.SpacingItemDecoration
import com.cogu.spylook.model.entity.Anotacion
import com.cogu.spylook.model.entity.Contacto
import com.cogu.spylook.model.utils.converters.DateConverters
import kotlinx.coroutines.runBlocking
import org.mapstruct.factory.Mappers
import java.time.LocalDate
import java.time.LocalDateTime

class InformacionFragment(private val contacto: Contacto) : Fragment() {
    private var edadContent: TextView? = null
    private var nickContent: TextView? = null
    private var fechaContent: TextView? = null
    private var ciudadContent: TextView? = null
    private var estadoContent: TextView? = null
    private var paisContent: TextView? = null
    private var recyclerView : RecyclerView? = null
    private val mapper = Mappers.getMapper(AnotacionToCardItem::class.java)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragment = inflater.inflate(R.layout.fragment_informacion, container, false)
        edadContent = fragment.findViewById<TextView>(R.id.edadContent)
        nickContent = fragment.findViewById<TextView>(R.id.nickContent)
        fechaContent = fragment.findViewById<TextView>(R.id.fechaContent)
        ciudadContent = fragment.findViewById<TextView>(R.id.ciudadContent)
        estadoContent = fragment.findViewById<TextView>(R.id.estadoContent)
        paisContent = fragment.findViewById<TextView>(R.id.paisContent)
        val db = AppDatabase.getInstance(requireContext())!!
        val dao = db.anotacionDAO()
        edadContent!!.text = contacto.edad.toString()
        nickContent!!.text = contacto.alias
        fechaContent!!.text = contacto.fechaNacimiento.toString()
        ciudadContent!!.text = contacto.ciudad
        estadoContent!!.text = contacto.estado
        paisContent!!.text = contacto.pais

        recyclerView = fragment.findViewById<RecyclerView>(R.id.recyclerAnotaciones)

        runBlocking {
            val anotaciones = dao!!.getAnotacionesContacto(contacto.id)
            val cardItemList = anotaciones.mapNotNull { a -> mapper.toCardItem(a) }.toMutableList()
            cardItemList.add(AnotacionCardItem(-1, "Nueva Anotacion","", DateConverters.toDateTimeString(
                LocalDateTime.now())!!, contacto.id))
            cardItemList.sortBy { a -> a.id }
            val adapter = AnotacionCardAdapter(cardItemList, requireContext(), contacto.id)
            recyclerView!!.setLayoutManager(LinearLayoutManager(requireContext()))
            recyclerView!!.adapter = adapter
            if (recyclerView!!.itemDecorationCount <= 0) {
                recyclerView!!.addItemDecoration(SpacingItemDecoration(requireContext()))
            }

        }

        return fragment
    }
}
