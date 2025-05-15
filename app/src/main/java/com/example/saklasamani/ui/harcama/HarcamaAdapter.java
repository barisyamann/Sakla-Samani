package com.example.saklasamani.ui.harcama;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.saklasamani.R;
import com.example.saklasamani.model.Harcama;

import java.util.ArrayList;
import java.util.List;

public class HarcamaAdapter extends RecyclerView.Adapter<HarcamaAdapter.HarcamaViewHolder> {

    private List<Harcama> harcamaList = new ArrayList<>();

    @NonNull
    @Override
    public HarcamaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.harcama_item, parent, false);
        return new HarcamaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HarcamaViewHolder holder, int position) {
        Harcama harcama = harcamaList.get(position);
        holder.textAmount.setText(String.format("Tutar: %.2f ₺", harcama.getAmount()));

        holder.textCategory.setText("Kategori: " + harcama.getCategory());
        holder.textNote.setText("Not: " + harcama.getNote());
    }

    @Override
    public int getItemCount() {
        return harcamaList.size();
    }

    public void setHarcamalar(List<Harcama> list) {
        this.harcamaList = list;
        notifyDataSetChanged();
    }

    public static class HarcamaViewHolder extends RecyclerView.ViewHolder {
        TextView textAmount, textNote, textCategory;

        public HarcamaViewHolder(@NonNull View itemView) {
            super(itemView);
            textAmount = itemView.findViewById(R.id.textAmount);
            textNote = itemView.findViewById(R.id.textNote);
            textCategory = itemView.findViewById(R.id.textCategory);
        }
    }
}
