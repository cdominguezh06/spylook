package com.cogu.spylook.adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cogu.spylook.model.unimplemented.SpinnerableClass;

import java.util.List;

public class SpinnerAdapter<T extends SpinnerableClass> extends ArrayAdapter<T> {

    private Context listener;
    private List<T> list;
    private List<Integer> ids;
    public SpinnerAdapter(Context listener, List<T> list, List<Integer> ids) {
        super(listener, ids.get(0), list);
        this.listener = listener;
        this.list = list;
        this.ids = ids;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(listener);
            convertView = inflater.inflate(ids.get(0), parent, false);
        }
        T item = list.get(position);

        ImageView imageView = convertView.findViewById(ids.get(1));
        if (imageView != null) {
            imageView.setImageResource(item.getImage());
        }

        return convertView;
    }

    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(listener);
            convertView = inflater.inflate(ids.get(0), parent, false);
        }
        T item = list.get(position);

        ImageView imageView = convertView.findViewById(ids.get(1));

        if (imageView != null) {
            imageView.setImageResource(item.getImage());
        }

        return convertView;
    }
}