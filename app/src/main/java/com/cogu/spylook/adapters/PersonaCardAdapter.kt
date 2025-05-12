package com.cogu.spylook.adapters

import android.content.Context
import android.content.Intent
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cogu.spylook.R
import com.cogu.spylook.adapters.PersonaCardAdapter.CardViewHolder
import com.cogu.spylook.model.cards.ContactoCardItem
import com.cogu.spylook.view.ContactoActivity

open class PersonaCardAdapter(
    internal val cardItemList: List<ContactoCardItem>,
    private val context: Context
) : RecyclerView.Adapter<CardViewHolder?>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view =
            LayoutInflater.from(parent.getContext()).inflate(R.layout.personacard, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val cardItem = cardItemList.get(position)
        holder.name.text = cardItem.nombre
        holder.mostknownalias.text = cardItem.alias
        if(cardItem.id !=-1){
            holder.careto.setImageResource(R.drawable.user_icon)
            holder.careto.setColorFilter(cardItem.colorFoto, android.graphics.PorterDuff.Mode.MULTIPLY)
            holder.itemView.setOnTouchListener { v, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    // Guarda las coordenadas del evento táctil
                    v.setTag(R.id.touch_event_x, event.rawX.toInt()) // Guardamos X
                    v.setTag(R.id.touch_event_y, event.rawY.toInt()) // Guardamos Y
                }
                false
            }

            holder.itemView.setOnLongClickListener(View.OnLongClickListener {
                view: View? ->
                val inflater = LayoutInflater.from(context)
                val popupView = inflater.inflate(R.layout.long_press_contact, null) // Diseña tu layout personalizado

                val popupWindow = PopupWindow(
                    popupView,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    true
                )

                val popupNombre = popupView.findViewById<TextView>(R.id.textViewPopUpNombre)
                val popupApodo = popupView.findViewById<TextView>(R.id.textViewPopUpApodo)
                popupNombre.text = cardItem.nombre
                popupApodo.text = cardItem.alias

                val x = view!!.getTag(R.id.touch_event_x) as Int
                val y = view.getTag(R.id.touch_event_y) as Int

                popupWindow.showAtLocation(view,  Gravity.NO_GRAVITY, x, y)

                true

            })
        }else{
            holder.careto.setImageResource(R.drawable.notfound)
        }
        if (cardItem.clickable) {
            holder.itemView.setOnClickListener(View.OnClickListener {
                val intent = Intent(context, ContactoActivity::class.java)
                intent.putExtra("id", cardItem.id)
                context.startActivity(intent)
            })
        }
    }

    override fun getItemCount(): Int {
        return cardItemList.size
    }

    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById<TextView>(R.id.name)
        var mostknownalias: TextView = itemView.findViewById<TextView>(R.id.mostknownalias)
        var careto: ImageView = itemView.findViewById<ImageView>(R.id.careto)
    }
}
