package com.cogu.spylook.view.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import com.cogu.spylook.R
import android.widget.RemoteViews
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.NotificationCompat
import androidx.core.graphics.createBitmap
import com.cogu.spylook.model.entity.Contacto
import com.cogu.spylook.view.notification.receiver.NotificationActionReceiver

object NotificationHelper {
    private const val CHANNEL_ID = "notificacion_contacto"
    private const val CHANNEL_NAME = "Notificación de Contacto"
    private const val NOTIFICATION_ID = 10

    fun showContactNotification(context: Context, contacto: Contacto, alias: String) {
        createNotificationChannel(context)

        val customLayout = RemoteViews(context.packageName, R.layout.persistent_notification)
        customLayout.setTextViewText(R.id.contactAlias, alias)
        val image = AppCompatResources.getDrawable(context, R.drawable.contact_icon)?.mutate()
        image?.setTint(contacto.colorFoto)
        image?.setTintMode(PorterDuff.Mode.MULTIPLY)
        val bitmap = createBitmap(100, 100)
        val canvas = Canvas(bitmap)
        image?.setBounds(0, 0, canvas.width, canvas.height)
        image?.draw(canvas)
        customLayout.setImageViewBitmap(R.id.contactImage, bitmap)
        val imageViews = listOf<Int>(R.id.editImage, R.id.sucesoImage, R.id.cuentaImage)
        listOf<Int>(R.drawable.edit_icon, R.drawable.suceso_icon, R.drawable.account_icon)
            .zip(imageViews)
            .forEach { (drawable, imageViewId) ->
                val icon = AppCompatResources.getDrawable(context, drawable)?.mutate()
                val gradient = AppCompatResources.getDrawable(context, R.drawable.rainbow_gradient)?.mutate()
                val gradientBitmap = createBitmap(100, 100)
                val gradientCanvas = Canvas(gradientBitmap)
                gradient?.setBounds(0, 0, 100, 100)
                gradient?.draw(gradientCanvas)
                val iconBitmap = createBitmap(100, 100)
                val iconCanvas = Canvas(iconBitmap)
                icon?.setBounds(0, 0, iconCanvas.width, iconCanvas.height)
                icon?.draw(iconCanvas)
                val resultBitmap = createBitmap(100, 100)
                val resultCanvas = Canvas(resultBitmap)
                resultCanvas.drawBitmap(iconBitmap, 0f, 0f, null)

                val paint = Paint()
                paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
                resultCanvas.drawBitmap(gradientBitmap, 0f, 0f, paint)
                paint.xfermode = null

                customLayout.setImageViewBitmap(imageViewId, resultBitmap)
            }
        val editarPendingIntent = PendingIntent.getBroadcast(
            context,
            100 + contacto.idAnotable,
            Intent(context, NotificationActionReceiver::class.java).apply {
                action = "ACTION_EDITAR"
                putExtra("id", contacto.idAnotable)
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        customLayout.setOnClickPendingIntent(R.id.layoutEditar, editarPendingIntent)

        val sucesoPendingIntent = PendingIntent.getBroadcast(
            context,
            200 + contacto.idAnotable,
            Intent(context, NotificationActionReceiver::class.java).apply {
                action = "ACTION_SUCESO"
                putExtra("id", contacto.idAnotable)
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        customLayout.setOnClickPendingIntent(R.id.layoutSuceso, sucesoPendingIntent)

        val cuentaPendingIntent = PendingIntent.getBroadcast(
            context,
            300 + contacto.idAnotable,
            Intent(context, NotificationActionReceiver::class.java).apply {
                action = "ACTION_CUENTA"
                putExtra("id", contacto.idAnotable)
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        customLayout.setOnClickPendingIntent(R.id.layoutCuenta, cuentaPendingIntent)

        val smallLayout = RemoteViews(context.packageName, R.layout.persistent_notification_small)
        smallLayout.setTextViewText(R.id.notification_title, "Contacto activo: ${contacto.alias}")
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.spylookicon_nobg)
            .setContentTitle("Contacto activo: ${contacto.alias}")
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCustomContentView(smallLayout)
            .setCustomBigContentView(customLayout)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .build()

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

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

    fun cancel(context: Context) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.cancel(NOTIFICATION_ID)
    }
}