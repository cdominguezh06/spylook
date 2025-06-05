package com.cogu.spylook.view.accounts.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cogu.data.dao.ContactoDAO
import com.cogu.data.database.AppDatabase
import com.cogu.data.mappers.toModel
import com.cogu.domain.model.Cuenta
import com.cogu.spylook.R
import com.cogu.spylook.adapters.cards.ContactoCardAdapter
import com.cogu.spylook.mappers.toCardItem
import com.cogu.spylook.model.cards.ContactoCardItem
import kotlinx.coroutines.launch

class CuentaDataFragment(private val cuentaEntity: Cuenta, val contexto: Context) : Fragment() {

    private lateinit var linkTextView: TextView
    private lateinit var redSocialTextView: TextView
    private lateinit var recyclerPropietario : RecyclerView
    private lateinit var contactoDAO : ContactoDAO
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragment = inflater.inflate(R.layout.fragment_account_data, container, false)
        bindStaticFields(fragment)
        lifecycleScope.launch {
            initializeRecyclerView(fragment)
        }
        return fragment
    }

    private suspend fun initializeRecyclerView(fragment: View) {
        contactoDAO = AppDatabase.getInstance(contexto)!!.contactoDAO()!!
        recyclerPropietario = fragment.findViewById<RecyclerView>(R.id.recyclerPropietario)
        val propietario = mutableListOf<ContactoCardItem>(
            contactoDAO.findContactoById(cuentaEntity.idPropietario).toModel().toCardItem()
        )
        recyclerPropietario.layoutManager = LinearLayoutManager(contexto)
        recyclerPropietario.adapter = ContactoCardAdapter(propietario, contexto)
    }

    private fun bindStaticFields(fragment: View) {
        linkTextView = fragment.findViewById<TextView>(R.id.linkText)
        linkTextView.text = cuentaEntity.link
        redSocialTextView = fragment.findViewById<TextView>(R.id.redSocialText)
        redSocialTextView.text = cuentaEntity.redSocial
    }
}