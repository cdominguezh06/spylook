package com.cogu.spylook.view.widget.factory

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.PorterDuff
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.createBitmap
import com.cogu.spylook.R
import com.cogu.spylook.database.AppDatabase
import com.cogu.spylook.mappers.ContactoToCardItem
import com.cogu.spylook.model.cards.ContactoCardItem
import kotlinx.coroutines.runBlocking
import org.mapstruct.factory.Mappers

class ContactWidgetFactory(private val context: Context, private val intent: Intent) : RemoteViewsService.RemoteViewsFactory {

    private val mapper : ContactoToCardItem = Mappers.getMapper<ContactoToCardItem>(ContactoToCardItem::class.java)
    private val contactList = mutableListOf<ContactoCardItem>()
    private var appWidgetId: Int = AppWidgetManager.INVALID_APPWIDGET_ID

    override fun onCreate() {
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)    
        loadContacts()
    }

    private fun loadContacts() {
        runBlocking {
            contactList.clear()
            contactList.apply {
                addAll(AppDatabase.getInstance(context)!!
                    .contactoDAO()!!
                    .getContactos()
                    .map {mapper.toCardItem(it)})
            }
        }
    }

    override fun onDataSetChanged() {
        loadContacts()
    }

    override fun onDestroy() {
    }

    override fun getCount(): Int = contactList.size

    override fun getViewAt(position: Int): RemoteViews {
        val contact = contactList[position]
        val views = RemoteViews(context.packageName, R.layout.contact_widget_card)
        val image = AppCompatResources.getDrawable(context, R.drawable.contact_icon)?.mutate()
        image?.setTint(contact.colorFoto)
        image?.setTintMode(PorterDuff.Mode.MULTIPLY)
        val bitmap = createBitmap(100, 100)
        val canvas = Canvas(bitmap)
        image?.setBounds(0, 0, canvas.width, canvas.height)
        image?.draw(canvas)
        views.setTextViewText(R.id.nombre, contact.nombre)
        views.setTextViewText(R.id.alias, contact.alias)
        views.setImageViewBitmap(R.id.imagen, bitmap)
        val fillInIntent = Intent()
        fillInIntent.putExtra("id", contact.idAnotable)
        views.setOnClickFillInIntent(R.id.layout_contact_widget_card, fillInIntent)
        return views
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(position: Int): Long = position.toLong()

    override fun hasStableIds(): Boolean = true
}