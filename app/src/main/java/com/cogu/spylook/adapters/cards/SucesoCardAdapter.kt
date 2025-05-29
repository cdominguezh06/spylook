package com.cogu.spylook.adapters.cards

import android.content.Context
import android.content.Intent
import android.view.Gravity
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cogu.spylook.R
import com.cogu.spylook.database.AppDatabase
import com.cogu.spylook.mappers.ContactoToMiniCard
import com.cogu.spylook.model.cards.ContactoMiniCard
import com.cogu.spylook.model.cards.SucesoCardItem
import com.cogu.spylook.model.utils.animations.RecyclerViewAnimator
import com.cogu.spylook.view.contacts.ContactoActivity
import com.cogu.spylook.view.sucesos.NuevoSucesoActivity
import kotlinx.coroutines.runBlocking
import org.mapstruct.factory.Mappers

open class SucesoCardAdapter(
    internal val cardItemList: MutableList<SucesoCardItem>,
    private val context: Context,
) : RecyclerView.Adapter<SucesoCardAdapter.CardViewHolder?>() {
    private lateinit var recyclerAnimator: RecyclerViewAnimator
    private lateinit var implicados: MutableList<ContactoMiniCard>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.suceso_card, parent, false)
        return CardViewHolder(view)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerAnimator = RecyclerViewAnimator(recyclerView, cardItemList, this)
    }


    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val cardItem = cardItemList[position]
        holder.titulo.text = cardItem.nombre
        holder.fecha.text = cardItem.fecha.toString()
        holder.imagen.setImageResource(R.drawable.suceso_icon)
        if (cardItem.idAnotable != -1) {
            holder.imagen.setColorFilter(cardItem.colorFoto, android.graphics.PorterDuff.Mode.MULTIPLY)
            holder.itemView.setOnTouchListener { v, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    v.setTag(R.id.touch_event_x, event.rawX.toInt())
                    v.setTag(R.id.touch_event_y, event.rawY.toInt())
                }
                false
            }
            runBlocking {
                val sucesoDao = AppDatabase.getInstance(context)!!.sucesoDAO()!!
                val contactoDao = AppDatabase.getInstance(context)!!.contactoDAO()!!
                val mapper =
                    Mappers.getMapper<ContactoToMiniCard>(ContactoToMiniCard::class.java)
                implicados =
                    mutableListOf<ContactoMiniCard>()
                        .apply {
                            val suceso = sucesoDao.findSucesoById(cardItem.idAnotable)!!
                            add(mapper.toMiniCard(contactoDao.findContactoById(suceso.idCausante)))
                            addAll(
                                sucesoDao.getRelacionesBySuceso(suceso.idAnotable).map {
                                    mapper.toMiniCard(contactoDao.findContactoById(it.idContacto))
                                }
                            )
                        }
                holder.cantidadImplicados.text = "${implicados.size} implicados"
            }
            holder.itemView.setOnLongClickListener(View.OnLongClickListener { view: View? ->
                view?.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                val inflater = LayoutInflater.from(context)
                val popupView = inflater.inflate(
                    R.layout.long_press_suceso,
                    null
                )

                val popupWindow = PopupWindow(
                    popupView,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    true
                )

                val implicadosRecyclerView = popupView.findViewById<RecyclerView>(R.id.recyclerLongPress)
                implicadosRecyclerView.layoutManager = LinearLayoutManager(context)
                implicadosRecyclerView.adapter =
                    ContactoMiniCardAdapter(implicados, context, onClick = {
                        popupWindow.dismiss()
                    })
                val x = view!!.getTag(R.id.touch_event_x) as Int
                val y = view.getTag(R.id.touch_event_y) as Int

                popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, x, y - 100)

                true
            })
        }
        if(position==0){
            holder.itemView.setOnClickListener(View.OnClickListener { view: View? ->
                view?.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                val intent = Intent(context, NuevoSucesoActivity::class.java)
                context.startActivity(intent)
            })
            return
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
        var titulo: TextView = itemView.findViewById(R.id.textTitulo)
        var fecha: TextView = itemView.findViewById(R.id.textFecha)
        var cantidadImplicados: TextView = itemView.findViewById(R.id.textCantidadImplicados)
        var imagen : ImageView = itemView.findViewById<ImageView>(R.id.imagenSuceso)
    }
}
