package com.cogu.spylook.adapters.cards

import android.content.Context
import android.content.Intent
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
import com.cogu.spylook.adapters.search.BusquedaGrupoCardAdapter
import com.cogu.spylook.database.AppDatabase
import com.cogu.spylook.mappers.ContactoToMiniCard
import com.cogu.spylook.mappers.GrupoToCardItem
import com.cogu.spylook.model.cards.ContactoMiniCard
import com.cogu.spylook.model.cards.GrupoCardItem
import com.cogu.spylook.model.entity.ContactoEntity
import com.cogu.spylook.model.entity.ContactoAmistadCrossRef
import com.cogu.spylook.model.entity.ContactoGrupoCrossRef
import com.cogu.spylook.model.utils.animations.RecyclerViewAnimator
import com.cogu.spylook.model.utils.textWatchers.TextWatcherSearchBarGruposDeContacto
import com.cogu.spylook.view.contacts.fragments.ContactGroupsFragment
import com.cogu.spylook.view.groups.GrupoActivity
import kotlinx.coroutines.runBlocking
import org.mapstruct.factory.Mappers

open class GruposDeContactoCardAdapter(
    internal val cardItemList: MutableList<GrupoCardItem>,
    private val context: Context,
    private val contactoEntity: ContactoEntity
) : RecyclerView.Adapter<GruposDeContactoCardAdapter.CardViewHolder?>() {
    private lateinit var onClickFunction: (GrupoCardItem) -> Unit
    private lateinit var mapper: GrupoToCardItem
    private lateinit var recyclerAnimator: RecyclerViewAnimator

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.grupo_card, parent, false)
        return CardViewHolder(view)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerAnimator = RecyclerViewAnimator(recyclerView, cardItemList, this)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        mapper = Mappers.getMapper(GrupoToCardItem::class.java)
        val cardItem = cardItemList[position]
        holder.name.text = cardItem.nombre
        holder.name.isSelected = true
        if (cardItem.idAnotable != -1) {
            runBlocking {
                val miembros = AppDatabase.getInstance(context)!!.grupoDAO()!!
                    .getRelacionesByGrupo(cardItem.idAnotable).size + 1
                holder.numeroMiembros.text = "${miembros} miembros"
                holder.numeroMiembros.isSelected = true
                holder.careto.setColorFilter(cardItem.colorFoto, android.graphics.PorterDuff.Mode.MULTIPLY)
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
                titulo.text = "Miembros"
                val miembrosRecycler = popupView.findViewById<RecyclerView>(R.id.recyclerLongPress)
                runBlocking {
                    val grupoDao = AppDatabase.getInstance(context)!!.grupoDAO()!!
                    val contactoDao = AppDatabase.getInstance(context)!!.contactoDAO()!!
                    val mapper =
                        Mappers.getMapper<ContactoToMiniCard>(ContactoToMiniCard::class.java)
                    val miembros =
                        mutableListOf<ContactoMiniCard>()
                            .apply {
                                val grupo = grupoDao.findGrupoById(cardItem.idAnotable)!!
                                add(mapper.toMiniCard(contactoDao.findContactoById(grupo.idCreador)))
                            }.apply {
                                addAll(
                                    grupoDao
                                        .getRelacionesByGrupo(cardItem.idAnotable)
                                        .map { mapper.toMiniCard(contactoDao.findContactoById(it.idContacto)) }
                                )
                            }
                    miembrosRecycler.layoutManager = LinearLayoutManager(context)
                    miembrosRecycler.adapter =
                        ContactoMiniCardAdapter(miembros, context, onClick = {
                            popupWindow.dismiss()
                        })
                    val buttonEliminar = popupView.findViewById<TextView>(R.id.buttonEliminar)
                    buttonEliminar.setOnClickListener { view: View? ->
                        view?.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                        val dao = AppDatabase.getInstance(context)!!.grupoDAO()!!
                        runBlocking {
                            val grupo = dao.findGrupoById(cardItem.idAnotable)!!
                            if (contactoEntity.idAnotable == grupo.idCreador) {
                                AlertDialog.Builder(context)
                                    .setTitle("Si continúas borrarás completamente el grupo")
                                    .setMessage(
                                        "El contacto \"${contactoEntity.alias}\" es el creador de este grupo, eliminar " +
                                                "el grupo de la lista implica borrarlo permanentemente"
                                    )
                                    .setPositiveButton("Continuar") { dialog, which ->
                                        runBlocking {
                                            dao.deleteGrupoAnotable(cardItem.idAnotable)
                                            val index = cardItemList.indexOf(cardItem)
                                            recyclerAnimator.deleteItemWithAnimation(
                                                holder.itemView,
                                                index,
                                                onEmptyCallback = {
                                                    cardItemList.add(GrupoCardItem.DEFAULT_FOR_EMPTY_LIST)
                                                },
                                                afterDeleteCallBack = {
                                                    popupWindow.dismiss()
                                                })
                                        }
                                    }.setNegativeButton("Cancelar") { dialog, which ->
                                        dialog.dismiss()
                                        popupWindow.dismiss()
                                    }.show()
                            } else {
                                val crossRef = ContactoGrupoCrossRef(
                                    idContacto = contactoEntity.idAnotable,
                                    idGrupo = cardItem.idAnotable
                                )
                                dao.deleteMiembroDeGrupo(crossRef)
                                val index = cardItemList.indexOf(cardItem)
                                recyclerAnimator.deleteItemWithAnimation(
                                    holder.itemView,
                                    index,
                                    onEmptyCallback = {
                                        cardItemList.add(GrupoCardItem.DEFAULT_FOR_EMPTY_LIST)
                                    },
                                    afterDeleteCallBack = {
                                        popupWindow.dismiss()
                                    })
                            }
                        }
                    }
                    val x = view!!.getTag(R.id.touch_event_x) as Int
                    val y = view.getTag(R.id.touch_event_y) as Int

                    popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, x, y - 100)

                    true
                }
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
                var lista = listOf<GrupoCardItem>()
                runBlocking {
                    val grupoDAO = AppDatabase.getInstance(context)!!
                        .grupoDAO()!!
                    lista = grupoDAO.getGrupos().map { c -> mapper.toCardItem(c) }
                    val busquedaComoMiembro =
                        grupoDAO.findGruposByMiembro(contactoEntity.idAnotable).map {
                            grupoDAO.findGrupoById(it.idGrupo)!!
                        }.toMutableList()
                    val busquedaComoCreador =
                        grupoDAO.findGruposByCreador(contactoEntity.idAnotable).map {
                            grupoDAO.findGrupoById(it.idAnotable)!!
                        }.toMutableList()
                    busquedaComoCreador.addAll(busquedaComoMiembro)
                    val excluded = busquedaComoCreador.distinct().map {
                        mapper.toCardItem(it)
                    }.toMutableList()
                    lista = lista.filter { c -> excluded.contains(c) == false }
                }
                recycler.layoutManager = LinearLayoutManager(context)
                fun onClick(cardItem: GrupoCardItem) {
                    val agregar =
                        ContactGroupsFragment.grupos[ContactGroupsFragment.grupos.size - 1]
                    ContactGroupsFragment.grupos.removeAt(ContactGroupsFragment.grupos.size - 1)
                    ContactGroupsFragment.grupos.add(cardItem)
                    ContactGroupsFragment.grupos.add(agregar)
                    val amistad = ContactoAmistadCrossRef(contactoEntity.idAnotable, cardItem.idAnotable)
                    runBlocking {
                        AppDatabase.getInstance(context)!!.grupoDAO()!!.insertarRelaciones(
                            listOf(ContactoGrupoCrossRef(contactoEntity.idAnotable, cardItem.idAnotable))
                        )
                        notifyDataSetChanged()
                        dialog.dismiss()
                    }
                }
                onClickFunction = ::onClick
                val busquedaContactoCardAdapter =
                    object : BusquedaGrupoCardAdapter(lista, context) {
                        override fun onClick(cardItem: GrupoCardItem) {
                            onClick(cardItem)
                        }

                    }
                recycler.adapter = busquedaContactoCardAdapter
                dialog.show()
                searchBar.addTextChangedListener(
                    TextWatcherSearchBarGruposDeContacto(
                        searchBar,
                        recycler,
                        onClickFunction,
                        context,
                        contactoEntity
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
                val intent = Intent(context, GrupoActivity::class.java)
                intent.putExtra("id", cardItem.idAnotable)
                context.startActivity(intent)
            })
        }
    }

    override fun getItemCount(): Int {
        return cardItemList.size
    }

    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById(R.id.name)
        var numeroMiembros: TextView = itemView.findViewById(R.id.numberOfMembers)
        var careto: ImageView = itemView.findViewById(R.id.imagen)
    }

}