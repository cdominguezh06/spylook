package com.cogu.spylook.view.sucesos.fragments

import android.content.Context
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cogu.spylook.R
import com.cogu.spylook.adapters.cards.ContactoCardAdapter
import com.cogu.spylook.dao.ContactoDAO
import com.cogu.spylook.database.AppDatabase
import com.cogu.spylook.mappers.ContactoToCardItem
import com.cogu.spylook.model.cards.ContactoCardItem
import com.cogu.spylook.model.entity.SucesoEntity
import kotlinx.coroutines.launch
import org.mapstruct.factory.Mappers

class SucesoDataFragment(private val sucesoEntity: SucesoEntity, val contexto: Context) : Fragment() {

    private lateinit var descripcionTextView: TextView
    private lateinit var fechaTextView: TextView
    private lateinit var lugarTextView: TextView
    private lateinit var recyclerCausante : RecyclerView
    private var mapper = Mappers.getMapper<ContactoToCardItem>(ContactoToCardItem::class.java)
    private lateinit var contactoDAO : ContactoDAO
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragment = inflater.inflate(R.layout.fragment_suceso_data, container, false)
        bindStaticFields(fragment)
        lifecycleScope.launch {
            initializeRecyclerView(fragment)
        }
        return fragment
    }

    private suspend fun initializeRecyclerView(fragment: View) {
        contactoDAO = AppDatabase.getInstance(contexto)!!.contactoDAO()!!
        recyclerCausante = fragment.findViewById<RecyclerView>(R.id.recyclerCausante)
        val causante = mutableListOf<ContactoCardItem>(
            mapper.toCardItem(contactoDAO.findContactoById(sucesoEntity.idCausante))
        )
        recyclerCausante.layoutManager = LinearLayoutManager(contexto)
        recyclerCausante.adapter = ContactoCardAdapter(causante, contexto)
    }

    private fun bindStaticFields(fragment: View) {
        descripcionTextView = fragment.findViewById<TextView>(R.id.descripcionText)
        descripcionTextView.text = sucesoEntity.descripcion
        descripcionTextView.movementMethod = ScrollingMovementMethod()
        fechaTextView = fragment.findViewById<TextView>(R.id.sucesoFechaText)
        fechaTextView.text = sucesoEntity.fecha
        lugarTextView = fragment.findViewById<TextView>(R.id.sucesoLugarText)
        lugarTextView.text = sucesoEntity.lugar
    }
}