package com.cogu.spylook.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.cogu.spylook.R
import com.cogu.spylook.model.cards.AnotacionCardItem

class AnotacionCardAdapter(
    private val cardItemList: MutableList<AnotacionCardItem>,
    private val context: Context
) : RecyclerView.Adapter<CardViewHolder?>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.anotacion_card, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val cardItem = cardItemList[position]
        holder.titulo.text = cardItem.titulo
        holder.fecha.text = cardItem.fecha
        holder.itemView.setOnClickListener(View.OnClickListener { l: View? ->
            if (position == cardItemList.size - 1){
                val mostrarAnotacion = LayoutInflater.from(context).inflate(R.layout.new_anotacion, null)
                val dialog = AlertDialog.Builder(context, R.style.CustomDialog).setView(mostrarAnotacion).create();
                val guardar = mostrarAnotacion.findViewById<TextView>(R.id.buttonGuardarAnotacion)
                guardar.setOnClickListener {
                    dialog.dismiss()
                }
                dialog.show()
            } else {
            }
        })
    }

    override fun getItemCount(): Int {
        return cardItemList.size
    }
}


class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var titulo: TextView = itemView.findViewById<TextView>(R.id.textTitulo)
    var fecha: TextView = itemView.findViewById<TextView>(R.id.textFecha)
}