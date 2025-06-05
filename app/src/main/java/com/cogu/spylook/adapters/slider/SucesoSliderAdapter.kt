package com.cogu.spylook.adapters.slider

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.cogu.spylook.model.entity.SucesoEntity
import com.cogu.spylook.view.common.fragments.AnotacionesFragment
import com.cogu.spylook.view.sucesos.fragments.ImplicadosFragment
import com.cogu.spylook.view.sucesos.fragments.SucesoDataFragment

class SucesoSliderAdapter(
    fragment: FragmentActivity,
    private val sucesoEntity: SucesoEntity,
    private val context: Context?
) : FragmentStateAdapter(fragment) {
    lateinit var anotaciones : AnotacionesFragment
    lateinit var implicados : ImplicadosFragment
    init {
        anotaciones = AnotacionesFragment(sucesoEntity, context!!)
        implicados = ImplicadosFragment(sucesoEntity)
    }
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            1 -> implicados
            2 -> anotaciones
            else -> SucesoDataFragment(sucesoEntity, context!!)
        }
    }

    override fun getItemCount(): Int {
        return 3
    }
}