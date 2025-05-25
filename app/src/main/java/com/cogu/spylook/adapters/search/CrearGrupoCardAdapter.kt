package com.cogu.spylook.adapters.search

import android.content.Context
import android.graphics.PorterDuff
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cogu.spylook.R
import com.cogu.spylook.model.cards.ContactoCardItem

class CrearGrupoCardAdapter (
    internal val cardItemList: List<ContactoCardItem>,
    private val context: Context,
    private val addLimit : Int
) : RecyclerView.Adapter<CrearGrupoCardAdapter.CardViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.personacard, parent, false)
        return CardViewHolder(view)
    }


    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val cardItem = cardItemList[position]
        holder.name.text = cardItem.nombre
        holder.mostknownalias.text = cardItem.alias
        if(cardItem.idAnotable !=-1){
            holder.careto.setImageResource(R.drawable.contact_icon)
            holder.careto.setColorFilter(cardItem.colorFoto, PorterDuff.Mode.MULTIPLY)
        }else{
            holder.careto.setImageResource(R.drawable.notfound)
        }
        if (cardItem.clickable) {
            holder.itemView.setOnClickListener(View.OnClickListener {
                val inflated = LayoutInflater.from(context).inflate(R.layout.buscar_contacto_inflate, null)
                val searchBar = inflated.findViewById<EditText>(R.id.searchInflateEditText)

                val popupWindow = PopupWindow(
                    inflated,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    true
                )
                popupWindow.showAtLocation(it, Gravity.NO_GRAVITY, 0, 0)
            })
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