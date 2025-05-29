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
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cogu.spylook.R
import com.cogu.spylook.adapters.search.BusquedaContactoCardAdapter
import com.cogu.spylook.database.AppDatabase
import com.cogu.spylook.mappers.ContactoToCardItem
import com.cogu.spylook.model.cards.ContactoCardItem
import com.cogu.spylook.model.entity.Contacto
import com.cogu.spylook.model.entity.ContactoAmistadCrossRef
import com.cogu.spylook.model.utils.textWatchers.TextWatcherSearchBarMiembros
import com.cogu.spylook.view.contacts.ContactoActivity
import com.cogu.spylook.view.contacts.fragments.AmigosFragment
import kotlinx.coroutines.runBlocking
import org.mapstruct.factory.Mappers

open class AmigoCardAdapter(
    internal val cardItemList: List<ContactoCardItem>,
    private val context: Context,
    private val contacto: Contacto,
    val amigos : MutableList<ContactoCardItem>
) : RecyclerView.Adapter<AmigoCardAdapter.CardViewHolder?>() {
    private lateinit var onClickFunction: (ContactoCardItem) -> Unit
    private lateinit var mapper: ContactoToCardItem
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.personacard, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        mapper = Mappers.getMapper(ContactoToCardItem::class.java)
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
                popupNombre.text = cardItem.nombre
                popupApodo.text = cardItem.alias

                val x = view!!.getTag(R.id.touch_event_x) as Int
                val y = view.getTag(R.id.touch_event_y) as Int

                popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, x - 200, y - 100)

                true

            })
        } else {
            holder.careto.setImageResource(R.drawable.notfound)
            holder.itemView.setOnClickListener { view: View? ->
                view?.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                val inflated =
                    LayoutInflater.from(context).inflate(R.layout.buscar_contacto_inflate, null)
                val searchBar = inflated.findViewById<EditText>(R.id.searchInflateEditText)
                val recycler = inflated.findViewById<RecyclerView>(R.id.recyclerBusquedaContactos)
                val dialog = AlertDialog.Builder(context, R.style.CustomDialog)
                    .setView(inflated)
                    .create()
                var lista = mutableListOf<ContactoCardItem>()
                runBlocking {
                    val contactoDAO = AppDatabase.getInstance(context)!!
                        .contactoDAO()!!
                    lista =
                        contactoDAO.getContactos().map { c -> mapper.toCardItem(c) }.toMutableList()
                    val busquedaComoContacto =
                        contactoDAO.getAmistadesPorContacto(contacto.idAnotable).map {
                            contactoDAO.findContactoById(it.idAmigo)
                        }.toMutableList()
                    val busquedaComoAmigo =
                        contactoDAO.getContactosPorAmigo(contacto.idAnotable).map {
                            contactoDAO.findContactoById(it.idContacto)
                        }.toMutableList()
                    busquedaComoAmigo.addAll(busquedaComoContacto)
                    busquedaComoAmigo.add(contacto)
                    val excluded = busquedaComoAmigo.distinct().map {
                        mapper.toCardItem(it)
                    }.toMutableList()
                    lista = lista.filter { c -> excluded.contains(c) == false }.toMutableList()
                    lista.ifEmpty { lista.add(ContactoCardItem.DEFAULT_FOR_EMPTY_LIST) }
                }
                recycler.layoutManager = LinearLayoutManager(context)
                fun onClick(cardItem: ContactoCardItem) {
                    val agregar = amigos[amigos.size - 1]
                    amigos.removeAt(amigos.size - 1)
                    amigos.add(cardItem)
                    amigos.add(agregar)
                    val amistad = ContactoAmistadCrossRef(contacto.idAnotable, cardItem.idAnotable)
                    runBlocking {
                        AppDatabase.getInstance(context)!!.contactoDAO()!!.insertAmistad(amistad)
                        notifyDataSetChanged()
                        dialog.dismiss()
                    }
                }
                onClickFunction = ::onClick
                val busquedaContactoCardAdapter =
                    object : BusquedaContactoCardAdapter(lista, context) {
                        override fun onClick(cardItem: ContactoCardItem) {
                            onClick(cardItem)
                        }
                    }
                recycler.adapter = busquedaContactoCardAdapter
                dialog.show()
                searchBar.addTextChangedListener(
                    TextWatcherSearchBarMiembros(
                        searchBar,
                        recycler,
                        onClickFunction,
                        context,
                        contacto.idAnotable,
                        onExclude = {
                            runBlocking {
                                val dao = AppDatabase.getInstance(context)!!.contactoDAO()!!
                                dao.getAmistadesPorContacto(contacto.idAnotable)
                                    .map { dao.findContactoById(it.idAmigo) }
                                    .toMutableList()
                                    .apply {
                                        addAll(
                                            dao.getContactosPorAmigo(contacto.idAnotable)
                                                .map { dao.findContactoById(it.idContacto) }
                                        )
                                    }
                            }
                        }
                    )
                )
                dialog.window?.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
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