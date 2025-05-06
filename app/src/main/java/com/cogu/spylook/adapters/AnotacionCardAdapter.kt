package com.cogu.spylook.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.cogu.spylook.R
import com.cogu.spylook.bbdd.AppDatabase
import com.cogu.spylook.mappers.AnotacionToCardItem
import com.cogu.spylook.model.cards.AnotacionCardItem
import com.cogu.spylook.model.entity.Anotacion
import kotlinx.coroutines.runBlocking
import org.mapstruct.factory.Mappers
import java.time.LocalDate

class AnotacionCardAdapter(
    private val cardItemList: MutableList<AnotacionCardItem>,
    private val context: Context,
    private val usuarioId: Int
) : RecyclerView.Adapter<CardViewHolder?>() {
    private val mapper = Mappers.getMapper(AnotacionToCardItem::class.java)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.anotacion_card, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val cardItem = cardItemList[position]
        holder.titulo.text = cardItem.titulo
        holder.fecha.text = cardItem.fecha
        holder.itemView.setOnClickListener(View.OnClickListener { l: View? ->
            if (position == 0) {
                writeNewAnotacion()
            } else {
                visualizeAnotacion(position)
            }
        })
    }

    override fun getItemCount(): Int {
        return cardItemList.size
    }

    fun visualizeAnotacion(position: Int) {
        val mostrarAnotacion =
            LayoutInflater.from(context)
                .inflate(R.layout.mostrar_anotacion, null)
        val titulo = mostrarAnotacion.findViewById<TextView>(R.id.textViewTitulo)
        titulo.text = cardItemList[position].titulo
        val fecha = mostrarAnotacion.findViewById<TextView>(R.id.textViewFecha)
        fecha.text = cardItemList[position].fecha
        val descripcion = mostrarAnotacion.findViewById<TextView>(R.id.textViewDescription)
        descripcion.text = cardItemList[position].descripcion
        val dialog = AlertDialog.Builder(context, R.style.CustomDialog)
            .setView(mostrarAnotacion)
            .create()
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val editar = mostrarAnotacion.findViewById<TextView>(R.id.buttonEditarAnotacion)
        val cerrar = mostrarAnotacion.findViewById<TextView>(R.id.buttonCerrar)
        val borrar = mostrarAnotacion.findViewById<TextView>(R.id.buttonBorrarAnotacion)
        editar.setOnClickListener {
            val anotacion = cardItemList[position]
            val anotacionView =
                LayoutInflater.from(context)
                    .inflate(R.layout.new_anotacion, null)
            anotacionView.findViewById<TextView>(R.id.editTextText).text =
                anotacion.titulo
            anotacionView.findViewById<TextView>(R.id.editTextText2).text =
                anotacion.descripcion
            writeNewAnotacion(anotacionView, position)
            titulo.text = cardItemList[position].titulo
            fecha.text = cardItemList[position].fecha
            descripcion.text = cardItemList[position].descripcion
        }
        cerrar.setOnClickListener {
            dialog.dismiss()
        }
        borrar.setOnClickListener {
            val db: AppDatabase = AppDatabase.getInstance(context)!!
            runBlocking {
                db.anotacionDAO()!!.deleteAnotacion(
                    db.anotacionDAO()
                        ?.findAnotacionById(cardItemList[position].id)!!
                )
                cardItemList.removeAt(position)
                notifyItemRemoved(position)
            }
            dialog.dismiss()
        }

        dialog.show()
    }

    fun writeNewAnotacion(
        view: View = LayoutInflater.from(context)
            .inflate(R.layout.new_anotacion, null),
        id: Int = 0,
        position: Int = 0
    ) {
        val db: AppDatabase = AppDatabase.getInstance(context)!!
        val dialog = AlertDialog.Builder(context, R.style.CustomDialog)
            .setView(view)
            .create();
        val guardar =
            view.findViewById<TextView>(R.id.buttonEditarAnotacion)
        guardar.setOnClickListener {
            val anotacion = Anotacion()
            if (id != 0) {
                anotacion.id = id
            }
            anotacion.fecha = LocalDate.now()
            anotacion.titulo =
                view.findViewById<TextView>(R.id.editTextText).text.toString()
            anotacion.descripcion =
                view.findViewById<TextView>(R.id.editTextText2).text.toString()
            anotacion.idContacto = usuarioId
            runBlocking {
                saveAnotacion(db, anotacion, position)
            }
            dialog.dismiss()
        }
        dialog.show()
    }

    private suspend fun saveAnotacion(db: AppDatabase, anotacion: Anotacion, position: Int) {
        var element = mapper.toCardItem(anotacion)!!
        if (position == 0) {
            db.anotacionDAO()!!.addAnotacion(anotacion)
            cardItemList.add(element)
            notifyItemInserted(cardItemList.size - 1)
            return
        }
        val editado = mapper.toAnotacion(cardItemList[position])
        editado.titulo = anotacion.titulo
        editado.descripcion = anotacion.descripcion
        editado.fecha = anotacion.fecha
        db.anotacionDAO()!!.addAnotacion(editado)
        cardItemList[position] = mapper.toCardItem(editado)!!
        notifyItemChanged(position)
    }
}

class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var titulo: TextView = itemView.findViewById<TextView>(R.id.textTitulo)
    var fecha: TextView = itemView.findViewById<TextView>(R.id.textFecha)
}