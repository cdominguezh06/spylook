package com.cogu.spylook.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import com.cogu.spylook.model.unimplemented.SpinnerableClass

class SpinnerAdapter<T : SpinnerableClass?>(
    listener: Context,
    list: MutableList<T?>,
    ids: MutableList<Int?>
) : ArrayAdapter<T?>(listener, ids.get(0)!!, list) {
    private val listener: Context?
    private val list: MutableList<T?>
    private val ids: MutableList<Int?>

    init {
        this.listener = listener
        this.list = list
        this.ids = ids
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            val inflater = LayoutInflater.from(listener)
            convertView = inflater.inflate(ids.get(0)!!, parent, false)
        }
        val item = list.get(position)

        val imageView = convertView.findViewById<ImageView?>(ids.get(1)!!)
        if (imageView != null) {
            imageView.setImageResource(item!!.image)
        }

        return convertView
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            val inflater = LayoutInflater.from(listener)
            convertView = inflater.inflate(ids.get(0)!!, parent, false)
        }
        val item = list.get(position)

        val imageView = convertView.findViewById<ImageView?>(ids.get(1)!!)

        if (imageView != null) {
            imageView.setImageResource(item!!.image)
        }

        return convertView
    }
}