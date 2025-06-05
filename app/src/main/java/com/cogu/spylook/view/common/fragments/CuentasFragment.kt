package com.cogu.spylook.view.common.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cogu.data.database.AppDatabase
import com.cogu.data.mappers.toModel
import com.cogu.domain.model.Anotable
import com.cogu.spylook.R
import com.cogu.spylook.adapters.cards.CuentaCardAdapter
import com.cogu.spylook.mappers.toCardItem
import com.cogu.spylook.model.cards.CuentaCardItem
import com.cogu.spylook.model.utils.decorators.SpacingItemDecoration
import kotlinx.coroutines.runBlocking

class CuentasFragment(private val anotable: Anotable, private val context: Context) : Fragment() {

    var cuentas = mutableListOf<CuentaCardItem>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_empty_recycler, container, false)

        setupRecyclerView(rootView.findViewById(R.id.recyclerGeneric))

        return rootView
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) = runBlocking {
        fetchSucesos()
        initializeRecyclerView(recyclerView)
    }


    private suspend fun fetchSucesos() {
        val db = AppDatabase.Companion.getInstance(requireContext())
        val cuentaDao = db?.cuentaDAO()!!
        cuentas = mutableListOf()
        cuentas.add(CuentaCardItem.DEFAULT_FOR_ADD)
        val temp = cuentaDao.findCuentasByPropietario(anotable.idAnotable)
            .map { it.toModel().toCardItem() }
            .toMutableList()
            .apply {
                addAll(cuentaDao.findCuentasByContacto(anotable.idAnotable)
                    .map { cuentaDao.findCuentaById(it.idCuenta)!! }
                    .map { it.toModel().toCardItem() }) }
            .sortedByDescending { it.idAnotable }
        cuentas.apply { addAll(temp) }
            .toMutableList()
    }

    private fun initializeRecyclerView(recyclerView: RecyclerView) {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = CuentaCardAdapter(cuentas, context, anotable)
        recyclerView.addItemDecoration(SpacingItemDecoration(context))
    }
}