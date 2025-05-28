package com.cogu.spylook.adapters.cards

import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.view.Gravity
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cogu.spylook.R
import com.cogu.spylook.adapters.cards.ContactoCardAdapter.CardViewHolder
import com.cogu.spylook.database.AppDatabase
import com.cogu.spylook.model.cards.ContactoCardItem
import com.cogu.spylook.model.utils.animations.RecyclerViewAnimator
import com.cogu.spylook.view.contacts.ContactoActivity
import kotlinx.coroutines.runBlocking

open class ContactoCardAdapter(
    internal val cardItemList: MutableList<ContactoCardItem>,
    private val context: Context,
) : RecyclerView.Adapter<CardViewHolder?>() {
    private lateinit var recyclerAnimator: RecyclerViewAnimator

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.personacard, parent, false)
        return CardViewHolder(view)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerAnimator = RecyclerViewAnimator(recyclerView, cardItemList, this)
    }


    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val cardItem = cardItemList[position]
        holder.name.text = cardItem.nombre
        holder.mostknownalias.text = cardItem.alias
        if (cardItem.idAnotable != -1) {
            holder.careto.setImageResource(R.drawable.contact_icon)
            holder.careto.setColorFilter(cardItem.colorFoto, PorterDuff.Mode.MULTIPLY)
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
                ) // Diseña tu layout personalizado

                val popupWindow = PopupWindow(
                    popupView,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    true
                )

                val popupNombre = popupView.findViewById<TextView>(R.id.textViewPopUp1)
                val popupApodo = popupView.findViewById<TextView>(R.id.textViewPopUp2)
                val popupBoton = popupView.findViewById<Button>(R.id.buttonEliminar)
                popupNombre.text = cardItem.nombre
                popupApodo.text = cardItem.alias
                popupBoton.setOnClickListener { v ->
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
                    val dao = AppDatabase.getInstance(context)!!.contactoDAO()!!
                    runBlocking {
                        dao.deleteContactoWithAnotableById(cardItem.idAnotable)
                        val index = cardItemList.indexOf(cardItem)
                        recyclerAnimator.deleteItemWithAnimation(
                            holder.itemView,
                            index,
                            onEmptyCallback = {
                                cardItemList.add(ContactoCardItem.DEFAULT_FOR_EMPTY_LIST)
                            },
                            afterDeleteCallBack = {
                                popupWindow.dismiss()
                            })
                    }

                }
                val x = view!!.getTag(R.id.touch_event_x) as Int
                val y = view.getTag(R.id.touch_event_y) as Int

                popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, x - 200, y - 100)

                true

            })
        } else {
            holder.careto.setImageResource(R.drawable.notfound)
        }
        if (cardItem.clickable) {
            holder.itemView.setOnClickListener(View.OnClickListener { view: View? ->
                view?.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                val intent = Intent(context, ContactoActivity::class.java)
                intent.putExtra("id", cardItem.idAnotable)
                context.startActivity(intent)
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
