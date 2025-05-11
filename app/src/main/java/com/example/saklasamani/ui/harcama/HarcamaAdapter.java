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

public class HarcamaAdapter extends RecyclerView.Adapter<HarcamaAdapter.ViewHolder> {

    private List<Harcama> harcamaList = new ArrayList<>();

    public void setHarcamalar(List<Harcama> list) {
        this.harcamaList = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textAmount, textNote;

        public ViewHolder(View itemView) {
            super(itemView);
            textAmount = itemView.findViewById(R.id.textAmount);
            textNote = itemView.findViewById(R.id.textNote);
        }
    }

    @NonNull
    @Override
    public HarcamaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.harcama_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Harcama harcama = harcamaList.get(position);
        holder.textAmount.setText(String.format("Tutar: %.2f ₺", harcama.getAmount()));
        holder.textNote.setText("Not: " + harcama.getNote());
    }

    @Override
    public int getItemCount() {
        return harcamaList.size();
    }
}