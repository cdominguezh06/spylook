package com.cogu.spylook.model.utils.textWatchers

import android.content.Context
import android.content.Intent
import android.graphics.LinearGradient
import android.graphics.PorterDuff
import android.graphics.Shader
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Scroller
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cogu.spylook.R
import com.cogu.spylook.adapters.cards.GrupoCardAdapter
import com.cogu.spylook.database.AppDatabase
import com.cogu.spylook.mappers.GrupoToCardItem
import com.cogu.spylook.model.cards.GrupoCardItem
import com.cogu.spylook.model.utils.ForegroundShaderSpan
import com.cogu.spylook.model.utils.StringWithSpacesIndexRetriever
import com.cogu.spylook.model.utils.textWatchers.actions.LongTextScrollerAction
import com.cogu.spylook.view.contacts.ContactoActivity
import com.cogu.spylook.view.groups.GrupoActivity
import kotlinx.coroutines.runBlocking
import org.mapstruct.factory.Mappers
import java.util.Locale

class TextWatcherSearchBarGroups(
    private val text: EditText,
    private val recyclerView: RecyclerView?,
    private val context: Context?
) : TextWatcher {
    private val mapper: GrupoToCardItem =
        Mappers.getMapper<GrupoToCardItem>(GrupoToCardItem::class.java)
    private val db: AppDatabase
    private lateinit var collect: MutableList<GrupoCardItem>
    private val retriever = StringWithSpacesIndexRetriever()

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
        val newAdapter = object : GrupoCardAdapter(collect, context!!) {
            override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
                val cardItem = cardItemList[position]
                runBlocking {
                    val miembros = AppDatabase.getInstance(context!!)!!.grupoDAO()!!
                        .getRelacionesByGrupo(cardItem.idAnotable).size + 1
                    holder.numeroMiembros.text = "${miembros} miembros"
                }
                holder.careto.setColorFilter(cardItem.colorFoto, PorterDuff.Mode.MULTIPLY)
                holder.name.apply {
                    setHorizontallyScrolling(true)
                    isHorizontalScrollBarEnabled = false
                    isSingleLine = true
                    ellipsize = null // Desactivar truncamiento
                    textAlignment = TextView.TEXT_ALIGNMENT_VIEW_START
                    val scroller = Scroller(context)
                    setScroller(scroller)
                    scroller.startScroll(LongTextScrollerAction.lastScroll.toInt(), 0,
                        LongTextScrollerAction.lastDistance.toInt(), 0)
                    invalidate()
                }
                holder.name.text = SpannableString(cardItem.nombre).apply {
                    cardItem.nombre = cardItem.nombre.let {
                        val spannable = SpannableString(it)
                        var startIndex = retriever.getStartIndex(busqueda, cardItem.nombre)

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
                                retriever.getSpanIntervalJump(busqueda, cardItem.nombre, startIndex),
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                            )
                            holder.name.post(LongTextScrollerAction(holder.name, startIndex, busqueda))

                        }
                        spannable.toString()
                    }
                }
                if (cardItem.idAnotable != -1) {
                    holder.careto.setImageResource(R.drawable.group_icon)
                } else {
                    holder.careto.setImageResource(R.drawable.notfound)
                }
                if (cardItem.clickable) {
                    holder.itemView.setOnClickListener(View.OnClickListener { l: View? ->
                        val intent = Intent(context, GrupoActivity::class.java)
                        intent.putExtra("id", cardItem.idAnotable)
                        context!!.startActivity(intent)
                    })
                }
            }
        }
        recyclerView!!.setLayoutManager(LinearLayoutManager(context))
        recyclerView.setAdapter(newAdapter)
    }

    override fun afterTextChanged(s: Editable?) {
    }
}