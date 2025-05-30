package com.cogu.spylook.adapters.cards

import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cogu.spylook.R
import com.cogu.spylook.model.cards.ContactoMiniCard
import com.cogu.spylook.view.contacts.ContactoActivity

open class ContactoMiniCardAdapter(
    internal val cardItemList: MutableList<ContactoMiniCard>,
    private val context: Context,
    private val onClick : () -> Unit
) : RecyclerView.Adapter<ContactoMiniCardAdapter.CardViewHolder?>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.member_minicard, parent, false)
        return CardViewHolder(view)
    }


    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val cardItem = cardItemList[position]
        holder.alias.text = cardItem.alias
        holder.alias.isSelected = true
        if (cardItem.idAnotable != -1) {
            holder.careto.setImageResource(R.drawable.contact_icon)
            holder.careto.setColorFilter(cardItem.colorFoto, PorterDuff.Mode.MULTIPLY)
        } else {
            holder.careto.setImageResource(R.drawable.notfound)
        }
        holder.itemView.setOnClickListener(View.OnClickListener { view: View? ->
            onClick.invoke()
            view?.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
            val intent = Intent(context, ContactoActivity::class.java)
            intent.putExtra("id", cardItem.idAnotable)
            context.startActivity(intent)
        })
    }

    override fun getItemCount(): Int {
        return cardItemList.size
    }

    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var alias: TextView = itemView.findViewById(R.id.textViewApodo)
        var careto: ImageView = itemView.findViewById(R.id.imagenMiniCard)
    }
}