package com.cogu.spylook.adapters.slider

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.cogu.spylook.model.entity.Grupo
import com.cogu.spylook.view.groups.fragments.GroupNotesFragment
import com.cogu.spylook.view.groups.fragments.MiembrosFragment

class GroupSliderAdapter(
    fragment: FragmentActivity,
    private val grupo: Grupo,
    private val context: Context?
) : FragmentStateAdapter(fragment) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            1 -> GroupNotesFragment(grupo)
            else -> MiembrosFragment(grupo)
        }
    }

    override fun getItemCount(): Int {
        return 2
    }
}