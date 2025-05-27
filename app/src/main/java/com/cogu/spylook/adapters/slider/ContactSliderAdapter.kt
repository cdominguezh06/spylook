package com.cogu.spylook.adapters.slider

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.cogu.spylook.model.entity.Contacto
import com.cogu.spylook.view.contacts.fragments.AmigosFragment
import com.cogu.spylook.view.contacts.fragments.InformacionFragment

class ContactSliderAdapter(
    fragment: FragmentActivity,
    private val contacto: Contacto,
    private val context: Context?
) : FragmentStateAdapter(fragment) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            1 -> AmigosFragment(contacto, context)
            else -> InformacionFragment(contacto)
        }
    }

    override fun getItemCount(): Int {
        return 2
    }
}