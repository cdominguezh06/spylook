package com.cogu.spylook.view.notification.receiver

import android.accessibilityservice.AccessibilityService
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.cogu.spylook.view.accounts.NuevaCuentaActivity
import com.cogu.spylook.view.contacts.NuevoContactoActivity
import com.cogu.spylook.view.sucesos.NuevoSucesoActivity

class NotificationActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val contactoId = intent.getIntExtra("id", -1)
        when (intent.action) {
            "ACTION_EDITAR" -> {
                val editIntent = Intent(context, NuevoContactoActivity::class.java).apply {
                    putExtra("idEdit", contactoId)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(editIntent)
            }
            "ACTION_SUCESO" -> {
                val sucesoIntent = Intent(context, NuevoSucesoActivity::class.java).apply {
                    putExtra("id", contactoId)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(sucesoIntent)
            }
            "ACTION_CUENTA" -> {
                val cuentaIntent = Intent(context, NuevaCuentaActivity::class.java).apply {
                    putExtra("id", contactoId)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(cuentaIntent)
            }
        }
    }
}