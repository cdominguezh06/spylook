package com.cogu.spylook.model.utils.textWatchers

import android.content.Context
import android.graphics.LinearGradient
import android.graphics.Shader
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.view.Gravity
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.PopupWindow
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cogu.spylook.R
import com.cogu.spylook.adapters.cards.GrupoCardAdapter
import com.cogu.spylook.adapters.search.BusquedaGrupoCardAdapter
import com.cogu.spylook.database.AppDatabase
import com.cogu.spylook.mappers.GrupoToCardItem
import com.cogu.spylook.model.cards.GrupoCardItem
import com.cogu.spylook.model.entity.Grupo
import com.cogu.spylook.model.utils.ForegroundShaderSpan
import kotlinx.coroutines.runBlocking
import org.mapstruct.factory.Mappers
import java.util.Locale

class TextWatcherSearchBarGruposDeContacto(
    private val text: EditText,
    private val recyclerView: RecyclerView?,
    private val onClickFunction: (GrupoCardItem) -> Unit,
    private val context: Context?
) : TextWatcher {
    private val mapper: GrupoToCardItem =
        Mappers.getMapper<GrupoToCardItem>(GrupoToCardItem::class.java)
    private val db: AppDatabase

    init {
        this.db = AppDatabase.getInstance(context!!)!!
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        val busqueda = text.getText().toString().lowercase(
            Locale.getDefault()
        )
        if (busqueda.isEmpty()) {
            runBlocking {
                val grupos = db.grupoDAO()!!.getGrupos()
                val collect =
                    grupos.map { mapper.toCardItem(it) }.toMutableList()
                recyclerView!!.setLayoutManager(LinearLayoutManager(context))
                recyclerView.setAdapter(GrupoCardAdapter(collect, context!!))
            }
        } else {
            runBlocking {
                val grupos =
                    db.grupoDAO()!!.getGrupos()
                val collect = grupos
                    .filter { i: Grupo? ->
                        i!!.nombre.lowercase(Locale.getDefault()).contains(busqueda)
                    }
                    .map { mapper.toCardItem(it) }
                    .toMutableList()
                if (collect.isEmpty()) {
                    collect.add(
                        GrupoCardItem(
                            -1,
                            "Sin resultados",
                            false
                        )
                    )
                }

                val newAdapter = object : BusquedaGrupoCardAdapter(collect, context!!) {
                    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
                        val cardItem = cardItemList[position]
                        holder.name.text = SpannableString(cardItem.nombre).apply {
                            cardItem.nombre = cardItem.nombre.let {
                                val spannable = SpannableString(it)
                                val startIndex = it.lowercase(Locale.getDefault()).indexOf(busqueda)
                                if (startIndex >= 0) {
                                    val shader = LinearGradient(
                                        0f, 0f, holder.name.textSize * 2, 0f,
                                        intArrayOf(
                                            context!!.getColor(R.color.red),
                                            context.getColor(R.color.yellow),
                                            context.getColor(R.color.green),

                                            ),
                                        floatArrayOf(0f, 0.5f, 1f),
                                        Shader.TileMode.MIRROR
                                    )
                                    setSpan(
                                        ForegroundShaderSpan(shader),
                                        startIndex,
                                        startIndex + busqueda.length,
                                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                                    )
                                }
                                spannable.toString()
                            }
                        }
                        if (cardItem.idAnotable != -1) {
                            runBlocking {
                                val miembros = AppDatabase.getInstance(context!!)!!.grupoDAO()!!
                                    .obtenerRelacionesPorGrupo(cardItem.idAnotable).size+1
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

                    override fun onClick(item: GrupoCardItem) {
                        onClickFunction(item)
                    }
                }
                recyclerView!!.setLayoutManager(LinearLayoutManager(context))
                recyclerView.setAdapter(newAdapter)
            }
        }
    }

    override fun afterTextChanged(s: Editable?) {
    }
}