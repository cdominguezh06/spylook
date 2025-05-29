package com.cogu.spylook.adapters.search

import android.app.Dialog
import android.content.Context
import android.graphics.PorterDuff
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cogu.spylook.R
import com.cogu.spylook.database.AppDatabase
import com.cogu.spylook.mappers.ContactoToCardItem
import com.cogu.spylook.model.cards.ContactoCardItem
import com.cogu.spylook.model.utils.textWatchers.TextWatcherSearchBarMiembros
import kotlinx.coroutines.runBlocking
import org.mapstruct.factory.Mappers

class SingleContactCardSearchAdapter(
    internal val cardItemList: List<ContactoCardItem>,
    private val context: Context,
    private val onClick : (ContactoCardItem, Dialog, SingleContactCardSearchAdapter) -> Unit,
    private val onLongClick : (ContactoCardItem, Context, SingleContactCardSearchAdapter, CardViewHolder) -> Boolean,
) : RecyclerView.Adapter<SingleContactCardSearchAdapter.CardViewHolder>() {
    private lateinit var mapper: ContactoToCardItem
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.contact_card, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        mapper = Mappers.getMapper(ContactoToCardItem::class.java)
        val cardItem = cardItemList[position]
        holder.name.text = cardItem.nombre
        holder.mostknownalias.text = cardItem.alias
        if (cardItem.idAnotable != -1) {
            holder.careto.setImageResource(R.drawable.contact_icon)
            holder.careto.setColorFilter(cardItem.colorFoto, PorterDuff.Mode.MULTIPLY)
        } else {
            holder.careto.setImageResource(R.drawable.notfound)
        }
        if (cardItem.clickable) {
            holder.itemView.setOnClickListener(View.OnClickListener { view: View? ->
                view?.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                val inflated =
                    LayoutInflater.from(context).inflate(R.layout.buscar_contacto_inflate, null)
                val searchBar = inflated.findViewById<EditText>(R.id.searchInflateEditText)
                val recycler = inflated.findViewById<RecyclerView>(R.id.recyclerBusquedaContactos)
                val dialog = AlertDialog.Builder(context, R.style.CustomDialog)
                    .setView(inflated)
                    .create()
                var lista = listOf<ContactoCardItem>()
                runBlocking {
                    lista = AppDatabase.getInstance(context)!!
                        .contactoDAO()!!.getContactos().map { c -> mapper.toCardItem(c) }
                }
                recycler.layoutManager = LinearLayoutManager(context)
                val busquedaContactoCardAdapter =
                    object : BusquedaContactoCardAdapter(lista, context) {
                        override fun onClick(cardItem: ContactoCardItem) {
                            onClick(cardItem,dialog,this@SingleContactCardSearchAdapter)
                        }
                    }
                recycler.adapter = busquedaContactoCardAdapter
                dialog.show()
                searchBar.addTextChangedListener(
                    TextWatcherSearchBarMiembros(
                        searchBar,
                        recycler,
                        onClickFunction = {item ->
                            onClick(item, dialog, this)
                        },
                        context,
                        cardItem.idAnotable,
                        onExclude = {
                            listOf()
                        }
                    )
                )
                dialog.window?.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            })

            holder.itemView.setOnLongClickListener {
                onLongClick(cardItem, context, this, holder)
            }
        }
    }

    override fun getItemCount(): Int {
        return cardItemList.size
    }

    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById(R.id.numberOfMembers)
        var mostknownalias: TextView = itemView.findViewById(R.id.name)
        var careto: ImageView = itemView.findViewById(R.id.imagen)
    }
}