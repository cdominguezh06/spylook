package com.cogu.spylook.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cogu.spylook.R
import com.cogu.spylook.adapters.PersonaCardAdapter.CardViewHolder
import com.cogu.spylook.model.cards.ContactoCardItem
import com.cogu.spylook.view.ContactoActivity

class PersonaCardAdapter(
    private val cardItemList: MutableList<ContactoCardItem>,
    private val context: Context
) : RecyclerView.Adapter<CardViewHolder?>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view =
            LayoutInflater.from(parent.getContext()).inflate(R.layout.personacard, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val cardItem = cardItemList.get(position)
        holder.name.setText(cardItem.getNombre())
        holder.mostknownalias.setText(cardItem.getNickMasConocido())
        holder.careto.setImageResource(cardItem.getFoto())
        if (cardItem.isClickable()) {
            holder.itemView.setOnClickListener(View.OnClickListener { l: View? ->
                val intent = Intent(context, ContactoActivity::class.java)
                intent.putExtra("id", cardItem.getId())
                context.startActivity(intent)
            })
        }
    }

    override fun getItemCount(): Int {
        return cardItemList.size
    }

    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView
        var mostknownalias: TextView
        var careto: ImageView

        init {
            name = itemView.findViewById<TextView>(R.id.name)
            mostknownalias = itemView.findViewById<TextView>(R.id.mostknownalias)
            careto = itemView.findViewById<ImageView>(R.id.careto)
        }
    }
}
