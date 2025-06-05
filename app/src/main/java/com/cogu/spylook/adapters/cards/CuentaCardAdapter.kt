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
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cogu.spylook.R
import com.cogu.spylook.database.AppDatabase
import com.cogu.spylook.mappers.ContactoToMiniCard
import com.cogu.spylook.model.cards.ContactoMiniCard
import com.cogu.spylook.model.cards.CuentaCardItem
import com.cogu.spylook.model.entity.AnotableEntity
import com.cogu.spylook.model.entity.CuentaContactoCrossRef
import com.cogu.spylook.model.utils.animations.RecyclerViewAnimator
import com.cogu.spylook.view.accounts.CuentaActivity
import com.cogu.spylook.view.accounts.NuevaCuentaActivity
import kotlinx.coroutines.runBlocking
import org.mapstruct.factory.Mappers

class CuentaCardAdapter(
    internal val cardItemList: MutableList<CuentaCardItem>,
    private val context: Context,
    private val anotableEntityOrigen: AnotableEntity
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
        holder.nombre.isSelected = true
        holder.link.text = cardItem.link
        holder.link.isSelected = true
        holder.redSocial.text = cardItem.redSocial
        holder.redSocial.isSelected = true
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
                    runBlocking {
                        val cuenta = AppDatabase.getInstance(context)!!
                            .cuentaDAO()!!.findCuentaById(cardItem.idAnotable)!!
                        if (cuenta.idPropietario == anotableEntityOrigen.idAnotable){
                            AlertDialog.Builder(context)
                                .setTitle("Eliminar cuenta")
                                .setMessage("Desea eliminar por completo la cuenta \"${cardItem.nombre}\"? \n\nEste proceso no se puede deshacer.")
                                .setPositiveButton("Continuar") { dialog, which ->
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
                                }.setNegativeButton("Cancelar") { dialog, which ->
                                    dialog.dismiss()
                                    popupWindow.dismiss()
                                }.show()
                        }else{
                            AlertDialog.Builder(context)
                                .setTitle("Eliminar relacion")
                                .setMessage("Desea quitar al usuario \"${anotableEntityOrigen.nombre}\" de la cuenta \"${cardItem.nombre}\"?")
                                .setPositiveButton("Continuar") { dialog, which ->
                                    val dao = AppDatabase.getInstance(context)!!.cuentaDAO()!!
                                    runBlocking {
                                        val ref = CuentaContactoCrossRef(idCuenta = cardItem.idAnotable, idContacto = anotableEntityOrigen.idAnotable)
                                        dao.deleteMiembroDeCuenta(ref)
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
                                }.setNegativeButton("Cancelar") { dialog, which ->
                                    dialog.dismiss()
                                    popupWindow.dismiss()
                                }.show()
                        }
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
                intent.putExtra("id", anotableEntityOrigen.idAnotable)
                context.startActivity(intent)
            })
            return
        }
        if (cardItem.clickable) {
            holder.itemView.setOnClickListener(View.OnClickListener { view: View? ->
                view?.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                val intent = Intent(context, CuentaActivity::class.java)
                intent.putExtra("id", cardItem.idAnotable)
                intent.putExtra("idOrigen", anotableEntityOrigen.idAnotable)
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