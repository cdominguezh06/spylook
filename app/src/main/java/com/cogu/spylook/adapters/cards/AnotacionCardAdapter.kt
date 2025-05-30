package com.cogu.spylook.adapters.cards

import android.content.Context
import android.text.method.ScrollingMovementMethod
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.cogu.spylook.R
import com.cogu.spylook.database.AppDatabase
import com.cogu.spylook.mappers.AnotacionToCardItem
import com.cogu.spylook.model.cards.AnotacionCardItem
import com.cogu.spylook.model.entity.Anotacion
import com.cogu.spylook.model.utils.converters.DateConverters
import kotlinx.coroutines.runBlocking
import org.mapstruct.factory.Mappers
import java.time.LocalDateTime

class AnotacionCardAdapter(
    private val cardItemList: MutableList<AnotacionCardItem>,
    private val context: Context,
    private val anotableId: Int
) : RecyclerView.Adapter<AnotacionCardAdapter.CardViewHolder>() {
    private val mapper = Mappers.getMapper(AnotacionToCardItem::class.java)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.anotacion_card, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val cardItem = cardItemList[position]
        holder.titulo.text = cardItem.titulo
        holder.titulo.isSelected = true
        holder.fecha.text = cardItem.fecha
        holder.fecha.isSelected = true
        holder.itemView.setOnClickListener(View.OnClickListener { l: View? ->
            l?.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
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
        titulo.isSelected = true
        val fecha = mostrarAnotacion.findViewById<TextView>(R.id.textViewFecha)
        fecha.text = cardItemList[position].fecha
        val descripcion = mostrarAnotacion.findViewById<TextView>(R.id.textViewDescription)
        descripcion.text = cardItemList[position].descripcion
        descripcion.movementMethod = ScrollingMovementMethod()
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
        editar.setOnClickListener {l: View? ->
            l?.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
            val anotacion = cardItemList[position]
            val anotacionView =
                LayoutInflater.from(context)
                    .inflate(R.layout.new_anotacion, null)
            anotacionView.findViewById<TextView>(R.id.editTextText).text =
                anotacion.titulo
            anotacionView.findViewById<TextView>(R.id.editTextText2).text =
                anotacion.descripcion
            writeNewAnotacion(anotacionView, position, dialog)
        }
        cerrar.setOnClickListener { l: View? ->
            l?.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
            dialog.dismiss()
        }
        borrar.setOnClickListener {l: View? ->
            l?.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
            val db: AppDatabase = AppDatabase.getInstance(context)!!
            runBlocking {
                db.anotacionDAO()!!.deleteAnotacion(
                    mapper.toAnotacion(cardItemList[position])
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
        position: Int = 0,
        secondDialog: AlertDialog? = null
    ) {
        val db: AppDatabase = AppDatabase.getInstance(context)!!
        val dialog = AlertDialog.Builder(context, R.style.CustomDialog)
            .setView(view)
            .create()
        val guardar =
            view.findViewById<TextView>(R.id.buttonEditarAnotacion)
        guardar.setOnClickListener {
            runBlocking {
                if (position == 0) {
                    createAnotacion(view, db)
                } else {
                    updateAnotacion(view, db, position)
                }
                dialog.dismiss()
                if (secondDialog != null) {
                    secondDialog.dismiss()
                    visualizeAnotacion(position)
                }
            }
        }
        dialog.show()
    }

    private suspend fun createAnotacion(view: View, db: AppDatabase) {
        val anotacion = Anotacion()
        anotacion.fecha = DateConverters.toDateTimeString(LocalDateTime.now())
        anotacion.titulo =
            view.findViewById<TextView>(R.id.editTextText).text.toString()
        anotacion.descripcion =
            view.findViewById<TextView>(R.id.editTextText2).text.toString()
        anotacion.idAnotable = anotableId
        val id = db.anotacionDAO()!!.addAnotacion(anotacion)
        anotacion.id = id.toInt()
        val element = mapper.toCardItem(anotacion)
        cardItemList.add(element)
        notifyItemInserted(cardItemList.size - 1)
    }

    private suspend fun updateAnotacion(view: View, db: AppDatabase, position: Int) {
        val editado = mapper.toAnotacion(cardItemList[position])
        editado.fecha = DateConverters.toDateTimeString(LocalDateTime.now())
        editado.titulo =
            view.findViewById<TextView>(R.id.editTextText).text.toString()
        editado.descripcion =
            view.findViewById<TextView>(R.id.editTextText2).text.toString()
        editado.idAnotable = anotableId
        val id = db.anotacionDAO()!!.addAnotacion(editado)
        editado.id = id.toInt()
        cardItemList[position] = mapper.toCardItem(editado)
        notifyItemChanged(position)
    }
    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var titulo: TextView = itemView.findViewById<TextView>(R.id.textTitulo)
        var fecha: TextView = itemView.findViewById<TextView>(R.id.textFecha)
    }
}
