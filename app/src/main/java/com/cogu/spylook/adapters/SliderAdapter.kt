package com.cogu.spylook.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.cogu.spylook.model.entity.Contacto
import com.cogu.spylook.view.fragments.AmigosFragment
import com.cogu.spylook.view.fragments.InformacionFragment

class SliderAdapter(
    fragment: FragmentActivity,
    private val contacto: Contacto,
    private val context: Context?
) : FragmentStateAdapter(fragment) {
    override fun createFragment(position: Int): Fragment {
        when (position) {
            1 -> return AmigosFragment(contacto, context)
            else -> return InformacionFragment(contacto)
        }
    }

    override fun getItemCount(): Int {
        return 2
    }
}
