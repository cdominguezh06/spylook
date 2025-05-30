package com.cogu.spylook.model.utils.textWatchers

import android.app.Dialog
import android.content.Context
import android.graphics.LinearGradient
import android.graphics.PorterDuff
import android.graphics.Shader
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cogu.spylook.R
import com.cogu.spylook.adapters.search.BusquedaContactoCardAdapter
import com.cogu.spylook.database.AppDatabase
import com.cogu.spylook.mappers.ContactoToCardItem
import com.cogu.spylook.model.cards.ContactoCardItem
import com.cogu.spylook.model.entity.Contacto
import com.cogu.spylook.model.utils.ForegroundShaderSpan
import com.cogu.spylook.model.utils.StringWithSpacesIndexRetriever
import com.cogu.spylook.model.utils.textWatchers.actions.LongTextScrollerAction
import kotlinx.coroutines.runBlocking
import org.mapstruct.factory.Mappers
import java.util.Locale
import kotlin.collections.listOf

class TextWatcherSearchBarMiembros(
    private val text: EditText,
    private val recyclerView: RecyclerView?,
    private val onClickFunction: (ContactoCardItem) -> Unit,
    private val context: Context?,
    private val currentMiembroId: Int,
    private val onExclude: () -> List<Contacto>,
) : TextWatcher {
    private val mapper: ContactoToCardItem =
        Mappers.getMapper<ContactoToCardItem>(ContactoToCardItem::class.java)
    private val db: AppDatabase
    private lateinit var baseAdapter : BusquedaContactoCardAdapter
    private lateinit var collect: MutableList<ContactoCardItem>
    private val retriever = StringWithSpacesIndexRetriever()

    init {
        this.db = AppDatabase.getInstance(context!!)!!
        baseAdapter = recyclerView?.adapter as BusquedaContactoCardAdapter
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        var contactos = listOf<Contacto>()
        val filter = onExclude.invoke()
        val busqueda = text.getText().toString().lowercase(
            Locale.getDefault()
        ).replace(" ", "")
        runBlocking {
            contactos =
                db.contactoDAO()!!.getContactos();
            collect = contactos
                .filter { !filter.contains(it) }
                .map { contacto -> mapper.toCardItem(contacto) }
                .filter { it.idAnotable != currentMiembroId }
                .toMutableList()
            collect.ifEmpty { collect.add(ContactoCardItem.DEFAULT_FOR_EMPTY_LIST) }
        }

       busqueda.ifEmpty {
            baseAdapter.cardItemList = collect
            baseAdapter.notifyItemRangeChanged(0, collect.size)
            recyclerView?.adapter = baseAdapter
            retriever.contador = 0
            LongTextScrollerAction.lastScroll = 0.0f
            return@onTextChanged
        }

        collect = collect.filter {
            it.alias.replace(" ","").lowercase(Locale.getDefault()).contains(busqueda)
        }
            .filter { it.idAnotable > 0 }
            .toMutableList()
        collect.ifEmpty {
            collect.clear()
            collect.add(ContactoCardItem.DEFAULT_FOR_NO_RESULTS)
            baseAdapter.cardItemList = collect
            baseAdapter.notifyItemRangeChanged(0, collect.size)
            recyclerView?.adapter = baseAdapter
            return@onTextChanged
        }
        val newAdapter = object : BusquedaContactoCardAdapter(collect) {
            override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
                val cardItem = cardItemList[position]
                holder.name.text = cardItem.nombre
                holder.mostknownalias.text = SpannableString(cardItem.alias).apply {
                    cardItem.alias = cardItem.alias.let {
                        val spannable = SpannableString(it)
                        val startIndex = retriever.getStartIndex(busqueda, cardItem.alias)
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
                            holder.mostknownalias.post(LongTextScrollerAction(holder.mostknownalias, startIndex, busqueda))
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
                    holder.itemView.setOnClickListener(View.OnClickListener {
                        onClickFunction(cardItem)
                    })
                }
            }

            override fun onClick(item: ContactoCardItem) {
                onClickFunction
            }
        }
        recyclerView!!.setLayoutManager(LinearLayoutManager(context))
        recyclerView.setAdapter(newAdapter)
    }

    override fun afterTextChanged(s: Editable?) {
    }
}