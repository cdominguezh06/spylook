package com.cogu.spylook.adapters.slider

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.cogu.domain.model.Cuenta
import com.cogu.spylook.view.accounts.fragments.CuentaDataFragment
import com.cogu.spylook.view.accounts.fragments.UsuariosCuentaFragment
import com.cogu.spylook.view.common.fragments.AnotacionesFragment

class CuentaSliderAdapter(
    fragment: FragmentActivity,
    private val cuentaEntity: Cuenta,
    private val context: Context?
) : FragmentStateAdapter(fragment) {
    lateinit var anotaciones : AnotacionesFragment
    lateinit var usuarios : UsuariosCuentaFragment
    init {
        anotaciones = AnotacionesFragment(cuentaEntity, context!!)
        usuarios = UsuariosCuentaFragment(cuentaEntity)
    }
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            1 -> usuarios
            2 -> anotaciones
            else -> CuentaDataFragment(cuentaEntity, context!!)
        }
    }

    override fun getItemCount(): Int {
        return 3
    }
}