package com.cogu.spylook.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.cogu.spylook.model.entity.Contacto;
import com.cogu.spylook.view.fragments.AmigosFragment;
import com.cogu.spylook.view.fragments.InformacionFragment;

public class SliderAdapter extends FragmentStateAdapter {

    private Contacto contacto;
    private Context context;
    public SliderAdapter(@NonNull FragmentActivity fragment, Contacto contacto, Context context) {
        super(fragment);
        this.contacto = contacto;
        this.context = context;
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new AmigosFragment(contacto, context);
            default:
                return new InformacionFragment(contacto);
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
