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
import com.cogu.spylook.model.cards.CuentaCardItem
import com.cogu.spylook.model.entity.Anotable
import com.cogu.spylook.model.utils.animations.RecyclerViewAnimator
import com.cogu.spylook.view.accounts.CuentaActivity
import com.cogu.spylook.view.accounts.NuevaCuentaActivity
import kotlinx.coroutines.runBlocking
import org.mapstruct.factory.Mappers

class CuentaCardAdapter(
    internal val cardItemList: MutableList<CuentaCardItem>,
    private val context: Context,
    private val anotableOrigen: Anotable
) : RecyclerView.Adapter<CuentaCardAdapter.CardViewHolder?>() {
    private lateinit var recyclerAnimator: RecyclerViewAnimator
    private lateinit var usuarios: MutableList<ContactoMiniCard>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.account_card, parent, false)
        return CardViewHolder(view)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerAnimator = RecyclerViewAnimator(recyclerView, cardItemList, this)
    }


    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val cardItem = cardItemList[position]
        holder.nombre.text = cardItem.nombre
        holder.link.text = cardItem.link
        holder.link.isSelected = true
        holder.redSocial.text = cardItem.redSocial
        holder.imagen.setImageResource(R.drawable.account_icon)
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
                val cuentaDAO = AppDatabase.getInstance(context)!!.cuentaDAO()!!
                val contactoDao = AppDatabase.getInstance(context)!!.contactoDAO()!!
                val mapper =
                    Mappers.getMapper<ContactoToMiniCard>(ContactoToMiniCard::class.java)
                usuarios =
                    mutableListOf<ContactoMiniCard>()
                        .apply {
                            val cuenta = cuentaDAO.findCuentaById(cardItem.idAnotable)!!
                            add(mapper.toMiniCard(contactoDao.findContactoById(cuenta.idPropietario)))
                            addAll(
                                cuentaDAO.findContactosByCuenta(cuenta.idAnotable).map {
                                    mapper.toMiniCard(contactoDao.findContactoById(it.idContacto))
                                }
                            )
                        }
            }
            holder.itemView.setOnLongClickListener(View.OnLongClickListener { view: View? ->
                view?.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                val inflater = LayoutInflater.from(context)
                val popupView = inflater.inflate(
                    R.layout.long_press_list,
                    null
                )

                val popupWindow = PopupWindow(
                    popupView,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    true
                )
                val titulo = popupView.findViewById<TextView>(R.id.textTitle)
                titulo.text = "Usuarios de la cuenta"
                val usuariosRecyclerView = popupView.findViewById<RecyclerView>(R.id.recyclerLongPress)
                usuariosRecyclerView.layoutManager = LinearLayoutManager(context)
                usuariosRecyclerView.adapter =
                    ContactoMiniCardAdapter(usuarios, context, onClick = {
                        popupWindow.dismiss()
                    })
                val buttonEliminar = popupView.findViewById<TextView>(R.id.buttonEliminar)
                buttonEliminar.setOnClickListener {view: View? ->
                    view?.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                    val dao = AppDatabase.getInstance(context)!!.cuentaDAO()!!
                    runBlocking {
                        dao.deleteCuentaAnotable(cardItem.idAnotable)
                        val index = cardItemList.indexOf(cardItem)
                        recyclerAnimator.deleteItemWithAnimation(
                            holder.itemView,
                            index,
                            onEmptyCallback = {
                                cardItemList.add(CuentaCardItem.DEFAULT_FOR_ADD)
                            },
                            afterDeleteCallBack = {
                                popupWindow.dismiss()
                            })
                    }
                }
                val x = view!!.getTag(R.id.touch_event_x) as Int
                val y = view.getTag(R.id.touch_event_y) as Int

                popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, x, y - 100)

                true
            })
        }
        if(position==0){
            holder.itemView.setOnClickListener(View.OnClickListener { view: View? ->
                view?.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                val intent = Intent(context, NuevaCuentaActivity::class.java)
                intent.putExtra("id", anotableOrigen.idAnotable)
                context.startActivity(intent)
            })
            return
        }
        if (cardItem.clickable) {
            holder.itemView.setOnClickListener(View.OnClickListener { view: View? ->
                view?.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                val intent = Intent(context, CuentaActivity::class.java)
                intent.putExtra("id", cardItem.idAnotable)
                context.startActivity(intent)
            })
        }
    }

    override fun getItemCount(): Int {
        return cardItemList.size
    }

    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nombre: TextView = itemView.findViewById(R.id.textNickCuenta)
        var link: TextView = itemView.findViewById(R.id.textLinkCuenta)
        var redSocial: TextView = itemView.findViewById(R.id.textRedSocialCuenta)
        var imagen : ImageView = itemView.findViewById<ImageView>(R.id.imagenCuenta)
    }
}