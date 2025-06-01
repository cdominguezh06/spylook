package com.cogu.spylook.view.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.cogu.spylook.R
import com.cogu.spylook.view.contacts.ContactoActivity
import com.cogu.spylook.view.widget.services.ContactWidgetService

class CustomProvider : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for(appWidgetId in appWidgetIds) {
            val intent = Intent(context, ContactoActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE)
            val views = RemoteViews(context.packageName, R.layout.contact_widget_layout)
            views.setPendingIntentTemplate(R.id.widget_contacts_list, pendingIntent)
            val intentService = Intent(context, ContactWidgetService::class.java)
            appWidgetManager.notifyAppWidgetViewDataChanged(intArrayOf(appWidgetId), R.id.widget_contacts_list)
            views.setRemoteAdapter(R.id.widget_contacts_list, intentService)
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}