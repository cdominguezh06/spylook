package com.cogu.spylook.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cogu.spylook.model.cards.ContactoCardItem;
import com.cogu.spylook.R;
import com.cogu.spylook.view.ContactoActivity;

import java.util.List;

public class PersonaCardAdapter extends RecyclerView.Adapter<PersonaCardAdapter.CardViewHolder>{

    private List<ContactoCardItem> cardItemList;
    private Context context;

    public PersonaCardAdapter(List<ContactoCardItem> cardItemList, Context context) {
        this.cardItemList = cardItemList;
        this.context = context;
    }

    @NonNull
    @Override
    public PersonaCardAdapter.CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.personacard, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonaCardAdapter.CardViewHolder holder, int position) {
        ContactoCardItem cardItem = cardItemList.get(position);
        holder.name.setText(cardItem.getNombre());
        holder.mostknownalias.setText(cardItem.getNickMasConocido());
        holder.careto.setImageResource(cardItem.getFoto());
        if (cardItem.isClickable()){
            holder.itemView.setOnClickListener(l->{
                Intent intent = new Intent(context, ContactoActivity.class);
                intent.putExtra("id", cardItem.getId());
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return cardItemList.size();
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView mostknownalias;
        ImageView careto;
        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            mostknownalias = itemView.findViewById(R.id.mostknownalias);
            careto = itemView.findViewById(R.id.careto);
        }
    }
}
