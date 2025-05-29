package com.cogu.spylook.adapters.slider

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.cogu.spylook.model.entity.Suceso
import com.cogu.spylook.view.common.fragments.AnotacionesFragment
import com.cogu.spylook.view.common.sucesos.fragments.ImplicadosFragment
import com.cogu.spylook.view.common.sucesos.fragments.SucesoDataFragment

class SucesoSliderAdapter(
    fragment: FragmentActivity,
    private val suceso: Suceso,
    private val context: Context?
) : FragmentStateAdapter(fragment) {
    lateinit var anotaciones : AnotacionesFragment
    lateinit var implicados : ImplicadosFragment
    init {
        anotaciones = AnotacionesFragment(suceso, context!!)
        implicados = ImplicadosFragment(suceso)
    }
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            1 -> implicados
            2 -> anotaciones
            else -> SucesoDataFragment(suceso, context!!)
        }
    }

    override fun getItemCount(): Int {
        return 3
    }
}