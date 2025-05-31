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
import android.widget.Scroller
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cogu.spylook.R
import com.cogu.spylook.adapters.cards.GrupoCardAdapter
import com.cogu.spylook.adapters.search.BusquedaGrupoCardAdapter
import com.cogu.spylook.database.AppDatabase
import com.cogu.spylook.mappers.GrupoToCardItem
import com.cogu.spylook.model.cards.GrupoCardItem
import com.cogu.spylook.model.entity.Contacto
import com.cogu.spylook.model.entity.Grupo
import com.cogu.spylook.model.utils.ForegroundShaderSpan
import com.cogu.spylook.model.utils.StringWithSpacesIndexRetriever
import com.cogu.spylook.model.utils.textWatchers.actions.LongTextScrollerAction
import kotlinx.coroutines.runBlocking
import org.mapstruct.factory.Mappers
import java.util.Locale
import kotlin.collections.ifEmpty

class TextWatcherSearchBarGruposDeContacto(
    private val text: EditText,
    private val recyclerView: RecyclerView?,
    private val onClickFunction: (GrupoCardItem) -> Unit,
    private val context: Context?,
    private val contacto: Contacto
) : TextWatcher {
    private val mapper: GrupoToCardItem =
        Mappers.getMapper<GrupoToCardItem>(GrupoToCardItem::class.java)
    private val db: AppDatabase
    private lateinit var collect: MutableList<GrupoCardItem>
    private val retriever = StringWithSpacesIndexRetriever()
    private val fitting: MutableList<GrupoCardItem> = mutableListOf()

    init {
        this.db = AppDatabase.getInstance(context!!)!!
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        val busqueda = text.getText().toString().lowercase(
            Locale.getDefault()
        ).replace(" ", "")
        runBlocking {
            collect = db.grupoDAO()!!
                .getGrupos()
                .map { c -> mapper.toCardItem(c) }
                .toMutableList()
            val exclude = db.grupoDAO()!!.findGruposByCreador(contacto.idAnotable)
                .toMutableList()
                .apply {
                    addAll(
                        db.grupoDAO()!!.findGruposByMiembro(contacto.idAnotable)
                            .map { db.grupoDAO()!!.findGrupoById(it.idGrupo)!! })
                }
                .map { mapper.toCardItem(it) }
            collect = collect.filter { !exclude.contains(it) }.toMutableList()
        }
        busqueda.ifEmpty {
            recyclerView!!.setAdapter(GrupoCardAdapter(collect, context!!))
            retriever.contador = 0
            LongTextScrollerAction.lastScroll = 0.0f
            LongTextScrollerAction.lastDistance = 0.0f
            return@onTextChanged
        }
        collect = collect.filter { c ->
            c.nombre.replace(" ", "").lowercase(Locale.getDefault()).contains(busqueda)
        }.toMutableList()
        collect.ifEmpty {
            collect.add(GrupoCardItem.DEFAULT_FOR_NO_RESULTS)
            recyclerView!!.setAdapter(GrupoCardAdapter(collect, context!!))
            return@onTextChanged
        }

        val newAdapter = object : BusquedaGrupoCardAdapter(collect, context!!) {
            override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
                val cardItem = cardItemList[position]
                holder.careto.setColorFilter(
                    cardItem.colorFoto,
                    android.graphics.PorterDuff.Mode.MULTIPLY
                )
                holder.name.apply {
                    if (!fitting.contains(cardItem)) {
                        setHorizontallyScrolling(true)
                        isHorizontalScrollBarEnabled = false
                        isSingleLine = true
                        ellipsize = null // Desactivar truncamiento
                        textAlignment = TextView.TEXT_ALIGNMENT_VIEW_START
                        val scroller = Scroller(context)
                        setScroller(scroller)
                        scroller.startScroll(
                            LongTextScrollerAction.lastScroll.toInt(), 0,
                            LongTextScrollerAction.lastDistance.toInt(), 0
                        )
                        invalidate()
                    }
                }
                holder.name.text = SpannableString(cardItem.nombre).apply {
                    cardItem.nombre = cardItem.nombre.let {
                        val spannable = SpannableString(it)
                        val startIndex = retriever.getStartIndex(busqueda, cardItem.nombre)
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
                                retriever.getSpanIntervalJump(
                                    busqueda,
                                    cardItem.nombre,
                                    startIndex
                                ),
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                            )
                            holder.name.post(
                                LongTextScrollerAction(
                                    holder.name,
                                    startIndex,
                                    busqueda,
                                    onFitOnScreen = {
                                        if (!fitting.contains(cardItem)) {
                                            fitting.add(cardItem)
                                        }
                                    })
                            )

                        }
                        spannable.toString()
                    }
                }
                if (cardItem.idAnotable != -1) {
                    runBlocking {
                        val miembros = AppDatabase.getInstance(context!!)!!.grupoDAO()!!
                            .getRelacionesByGrupo(cardItem.idAnotable).size + 1
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

                        popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, x - 200, y - 100)

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

    override fun afterTextChanged(s: Editable?) {
    }
}