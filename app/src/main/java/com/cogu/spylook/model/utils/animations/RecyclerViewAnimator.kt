package com.cogu.spylook.model.utils.animations

import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAnimator(
    private val recyclerView: RecyclerView,
    private val dataSource: MutableList<*>,
    private val adapter: RecyclerView.Adapter<*>
) {

    /**
     * Anima la eliminación de un elemento específico.
     *
     * @param rowView La vista correspondiente al elemento.
     * @param position La posición del elemento a eliminar.
     */
    fun deleteItemWithAnimation(rowView: View, position: Int, afterDeleteCallBack: () -> Unit = {}, onEmptyCallback: () -> Unit = {}) {
        // Eliminar del dataSource inmediatamente
        if (dataSource.size == 1) {
            onEmptyCallback()
        }
        dataSource.removeAt(position)

        // Actualizar el adaptador inmediatamente
        adapter.notifyItemRemoved(position)

        // Crear y configurar la animación
        val animation = AnimationUtils.loadAnimation(rowView.context, android.R.anim.slide_out_right).apply {
            duration = 300
        }
        rowView.startAnimation(animation)
        // Esperar a que termine la animación antes de interactuar con el RecyclerView
        Handler(Looper.getMainLooper()).postDelayed({ afterDeleteCallBack() }, animation.duration)
    }

    /**
     * Anima la eliminación de todos los elementos con un efecto secuencial.
     *
     * @param onEmptyCallback Acción opcional a ejecutar cuando se eliminen todos los elementos.
     */
    fun deleteAllWithAnimation(onEmptyCallback: () -> Unit = {}) {
        val handler = Handler(Looper.getMainLooper())
        var stopHandler = false

        val runnable = object : Runnable {
            override fun run() {
                if (dataSource.isEmpty()) {
                    stopHandler = true
                    onEmptyCallback()
                }
                if (!stopHandler) {
                    recyclerView.findViewHolderForAdapterPosition(0)?.itemView?.let { rowView ->
                        deleteItemWithAnimation(rowView, 0)
                    }
                    handler.postDelayed(this, 250) // Retardo entre eliminación de elementos
                } else {
                    handler.removeCallbacksAndMessages(null)
                }
            }
        }
        handler.post(runnable)
    }
}
