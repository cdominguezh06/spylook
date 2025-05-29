package com.cogu.spylook.adapters.slider

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.cogu.spylook.model.entity.Contacto
import com.cogu.spylook.view.common.fragments.CuentasFragment
import com.cogu.spylook.view.common.fragments.SucesosFragment
import com.cogu.spylook.view.contacts.fragments.AmigosFragment
import com.cogu.spylook.view.contacts.fragments.ContactGroupsFragment
import com.cogu.spylook.view.contacts.fragments.InformacionFragment

class ContactSliderAdapter(
    fragment: FragmentActivity,
    private val contacto: Contacto,
    private val context: Context?
) : FragmentStateAdapter(fragment) {
    lateinit var amigos : AmigosFragment
    lateinit var grupos : ContactGroupsFragment
    lateinit var sucesos : SucesosFragment
    lateinit var cuentas : CuentasFragment
    init {
        amigos = AmigosFragment(contacto, context)
        grupos = ContactGroupsFragment(contacto)
        sucesos = SucesosFragment(contacto, context!!)
        cuentas = CuentasFragment(contacto, context)
    }
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            1 -> amigos
            2 -> grupos
            3 -> sucesos
            4 -> cuentas
            else -> InformacionFragment(contacto)
        }
    }

    override fun getItemCount(): Int {
        return 5
    }
}