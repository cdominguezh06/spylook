package com.cogu.spylook.adapters.search

import android.content.Context
import android.view.Gravity
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cogu.spylook.R
import com.cogu.spylook.database.AppDatabase
import com.cogu.spylook.model.cards.GrupoCardItem
import kotlinx.coroutines.runBlocking

abstract class BusquedaGrupoCardAdapter(
    internal val cardItemList: List<GrupoCardItem>,
    private val context: Context,
) : RecyclerView.Adapter<BusquedaGrupoCardAdapter.CardViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.grupocard, parent, false)
        return CardViewHolder(view)
    }


    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val cardItem = cardItemList[position]
        holder.name.text = cardItem.nombre
        if (cardItem.idAnotable != -1) {
            runBlocking {
                val miembros = AppDatabase.getInstance(context)!!.grupoDAO()!!
                    .getRelacionesByGrupo(cardItem.idAnotable).size+1
                holder.numeroMiembros.text = "${miembros} miembros"
            }
            holder.careto.setImageResource(R.drawable.group_icon)
            holder.itemView.setOnTouchListener { v, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    v.setTag(R.id.touch_event_x, event.rawX.toInt())
                    v.setTag(R.id.touch_event_y, event.rawY.toInt())
                }
                false
            }

            holder.itemView.setOnLongClickListener(View.OnLongClickListener { view: View? ->
                view?.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                val inflater = LayoutInflater.from(context)
                val popupView = inflater.inflate(
                    R.layout.long_press_contact,
                    null
                )

                val popupWindow = PopupWindow(
                    popupView,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    true
                )

                val popupNombre = popupView.findViewById<TextView>(R.id.textViewPopUp1)
                val popupCantidad = popupView.findViewById<TextView>(R.id.textViewPopUp2)
                popupNombre.text = cardItem.nombre
                popupCantidad.text = cardItemList.size.toString() + " contactos"

                val x = view!!.getTag(R.id.touch_event_x) as Int
                val y = view.getTag(R.id.touch_event_y) as Int

                popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, x-200, y-100)

                true

            })
        } else {
            holder.careto.setImageResource(R.drawable.notfound)
        }
        if (cardItem.clickable) {
            holder.itemView.setOnClickListener(View.OnClickListener {
                onClick(cardItem)
            })
        }
    }

    abstract fun onClick(cardItem: GrupoCardItem)

    override fun getItemCount(): Int {
        return cardItemList.size
    }

    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById(R.id.name)
        var numeroMiembros: TextView = itemView.findViewById(R.id.numberOfMembers)
        var careto: ImageView = itemView.findViewById(R.id.imagen)
    }
}