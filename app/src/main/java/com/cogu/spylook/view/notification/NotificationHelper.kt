package com.cogu.spylook.view.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.PorterDuff
import com.cogu.spylook.R
import android.widget.RemoteViews
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.NotificationCompat
import androidx.core.graphics.createBitmap
import com.cogu.spylook.model.entity.Contacto
import com.cogu.spylook.view.common.MainActivity
import com.cogu.spylook.view.contacts.ContactoActivity

object NotificationHelper {
    private const val CHANNEL_ID = "notificacion_contacto"
    private const val CHANNEL_NAME = "Notificación de Contacto"
    private const val NOTIFICATION_ID = 10
    var lastContact : Contacto? = null

    private fun createNotificationChannel(context: Context) {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Notificación persistente del contacto"
        }
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }

    fun showOpenContactNotification(context: Context, contacto: Contacto) {
        createNotificationChannel(context)
        lastContact?.apply {
          if(lastContact!! == contacto) return@apply
            lastContact = contacto
        }

        val customLayout = RemoteViews(context.packageName, R.layout.persistent_notification_open_contact)
        customLayout.setTextViewText(R.id.contactAlias, contacto.alias)
        val image = AppCompatResources.getDrawable(context, R.drawable.contact_icon)?.mutate()
        image?.setTint(contacto.colorFoto)
        image?.setTintMode(PorterDuff.Mode.MULTIPLY)
        val bitmap = createBitmap(100, 100)
        val canvas = Canvas(bitmap)
        image?.setBounds(0, 0, canvas.width, canvas.height)
        image?.draw(canvas)
        customLayout.setImageViewBitmap(R.id.contactImage, bitmap)
        val smallLayout = RemoteViews(context.packageName, R.layout.persistent_notification_small)
        smallLayout.setTextViewText(R.id.notification_title, "Contacto activo: ${contacto.alias}")

        val contactoIntent = Intent(context, ContactoActivity::class.java)
            .apply {
                putExtra("id", contacto.idAnotable)
            }
        val pendingIntent = TaskStackBuilder.create(context).run {
            addParentStack(ContactoActivity::class.java)
            addNextIntent(contactoIntent)
            getPendingIntent(400 + contacto.idAnotable,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        }


        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.spylookicon_nobg)
            .setContentTitle("Contacto activo: ${contacto.alias}")
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCustomContentView(smallLayout)
            .setCustomBigContentView(customLayout)
            .setContentIntent(pendingIntent)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .build()

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    fun cancel(context: Context) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.cancel(NOTIFICATION_ID)
    }
}