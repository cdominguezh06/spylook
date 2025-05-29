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
import com.cogu.spylook.adapters.cards.SucesoCardAdapter
import com.cogu.spylook.database.AppDatabase
import com.cogu.spylook.mappers.SucesoToCardItem
import com.cogu.spylook.model.cards.SucesoCardItem
import com.cogu.spylook.model.entity.Anotable
import com.cogu.spylook.model.utils.decorators.SpacingItemDecoration
import kotlinx.coroutines.runBlocking
import org.mapstruct.factory.Mappers

class SucesosFragment(private val anotable: Anotable, private val context: Context) : Fragment() {

    private val mapper: SucesoToCardItem = Mappers.getMapper(SucesoToCardItem::class.java)

    companion object {
        var sucesos = mutableListOf<SucesoCardItem>()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_sucesos, container, false)

        setupRecyclerView(rootView.findViewById(R.id.recyclerSucesos))

        return rootView
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) = runBlocking {
        fetchSucesos()
        initializeRecyclerView(recyclerView)
    }


    private suspend fun fetchSucesos() {
        val db = AppDatabase.getInstance(requireContext())
        val sucesoDao = db?.sucesoDAO()!!
        sucesos = sucesoDao.findSucesosByCausante(anotable.idAnotable).toMutableList()
            .apply {
                addAll(
                    sucesoDao.findSucesosByImplicado(anotable.idAnotable).map {
                        sucesoDao.findSucesoById(it.idSuceso)!!
                    }
                )
            }.map {
                mapper.toCardItem(it)
            }
            .toMutableList()
        sucesos.add(SucesoCardItem.DEFAULT_FOR_ADD)
    }

    private fun initializeRecyclerView(recyclerView: RecyclerView) {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = SucesoCardAdapter(sucesos, context)
        recyclerView.addItemDecoration(SpacingItemDecoration(context))
    }
}