package com.cogu.spylook.view.widget.services

import android.content.Intent
import android.widget.RemoteViewsService
import com.cogu.spylook.view.widget.factory.ContactWidgetFactory

class ContactWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return ContactWidgetFactory(this.applicationContext, intent)
    }
}