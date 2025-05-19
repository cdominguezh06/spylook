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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cogu.spylook.R
import com.cogu.spylook.adapters.GrupoCardAdapter
import com.cogu.spylook.database.AppDatabase
import com.cogu.spylook.mappers.GrupoToCardItem
import com.cogu.spylook.model.cards.GrupoCardItem
import com.cogu.spylook.model.utils.ForegroundShaderSpan
import com.cogu.spylook.view.ContactoActivity
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

    init {
        this.db = AppDatabase.getInstance(context!!)!!
        runBlocking {
            collect = db.grupoDAO()!!
                    .getGrupos()
                    .map { c -> mapper.toCardItem(c) }
                    .toMutableList()
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        val busqueda = text.getText().toString().lowercase(
            Locale.getDefault()
        )
        if (busqueda.isEmpty()) {
            runBlocking {
                recyclerView?.setAdapter(GrupoCardAdapter(collect, context!!))
            }
        } else {
            runBlocking {
                if (collect.isEmpty()) {
                    collect.add(
                        GrupoCardItem(
                            -1,
                            "Sin resultados",
                            0,
                            false
                        )
                    )
                }

                val newAdapter = object : GrupoCardAdapter(collect, context!!) {
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
                        if (cardItem.idGrupo != -1) {
                            holder.careto.setImageResource(R.drawable.user_icon)
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
                                intent.putExtra("id", cardItem.idGrupo)
                                context!!.startActivity(intent)
                            })
                        }
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