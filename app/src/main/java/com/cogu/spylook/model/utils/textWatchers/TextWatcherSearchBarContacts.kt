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
import android.view.View
import android.widget.EditText
import android.widget.Scroller
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cogu.data.database.AppDatabase
import com.cogu.data.mappers.toModel
import com.cogu.spylook.R
import com.cogu.spylook.adapters.cards.ContactoCardAdapter
import com.cogu.spylook.mappers.toCardItem
import com.cogu.spylook.model.cards.ContactoCardItem
import com.cogu.spylook.model.utils.ForegroundShaderSpan
import com.cogu.spylook.model.utils.StringWithSpacesIndexRetriever
import com.cogu.spylook.model.utils.textWatchers.actions.LongTextScrollerAction
import com.cogu.spylook.view.contacts.ContactoActivity
import kotlinx.coroutines.runBlocking
import java.util.Locale

class TextWatcherSearchBarContacts(
    private val text: EditText,
    private val recyclerView: RecyclerView?,
    private val context: Context?
) : TextWatcher {
    private val db: AppDatabase
    private lateinit var collect: MutableList<ContactoCardItem>
    private val retriever = StringWithSpacesIndexRetriever()
    private val fitting : MutableList<ContactoCardItem> = mutableListOf()

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
        collect = db.contactoDAO()!!
            .getContactos()
            .map { it.toModel().toCardItem() }
            .toMutableList()
    }
    busqueda.ifEmpty {
        recyclerView!!.setAdapter(ContactoCardAdapter(collect, context!!))
        retriever.contador = 0
        LongTextScrollerAction.lastScroll = 0.0f
        LongTextScrollerAction.lastDistance = 0.0f
        return@onTextChanged
    }
    collect = collect.filter { c ->
        c.alias.replace(" ", "").lowercase(Locale.getDefault()).contains(busqueda)
    }.toMutableList()
    collect.ifEmpty {
        collect.add(ContactoCardItem.DEFAULT_FOR_NO_RESULTS)
        recyclerView!!.setAdapter(ContactoCardAdapter(collect, context!!))
        return@onTextChanged
    }
    val newAdapter = object : ContactoCardAdapter(collect, context!!) {
        override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
            val cardItem = cardItemList[position]
            holder.name.text = cardItem.nombre
            holder.mostknownalias.apply {
                if(!fitting.contains(cardItem)){
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
            holder.mostknownalias.text = SpannableString(cardItem.alias).apply {
                cardItem.alias = cardItem.alias.let {
                    val spannable = SpannableString(it)
                    var startIndex = retriever.getStartIndex(busqueda, cardItem.alias)
                    if (startIndex >= 0) {
                        val shader = LinearGradient(
                            0f, 0f, holder.mostknownalias.textSize * 2, 0f,
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
                            retriever.getSpanIntervalJump(busqueda, cardItem.alias, startIndex),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        holder.mostknownalias.post(
                            LongTextScrollerAction(
                                holder.mostknownalias,
                                startIndex,
                                busqueda,
                                onFitOnScreen = {
                                    if(!fitting.contains(cardItem)){
                                        fitting.add(cardItem)
                                    }
                                }
                            )
                        )
                    }
                    spannable.toString()
                }

            }

            if (cardItem.idAnotable != -1) {
                holder.careto.setImageResource(R.drawable.contact_icon)
                holder.careto.setColorFilter(
                    cardItem.colorFoto,
                    PorterDuff.Mode.MULTIPLY
                )
            } else {
                holder.careto.setImageResource(R.drawable.notfound)
            }
            if (cardItem.clickable) {
                holder.itemView.setOnClickListener(View.OnClickListener { l: View? ->
                    val intent = Intent(context, ContactoActivity::class.java)
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

