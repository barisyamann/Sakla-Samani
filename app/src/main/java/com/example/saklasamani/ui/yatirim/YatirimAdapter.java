package com.example.saklasamani.ui.yatirim;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.saklasamani.R;
import com.example.saklasamani.model.yatirim.Yatirim;
import java.util.List;
import java.util.Locale;

public class YatirimAdapter extends RecyclerView.Adapter<YatirimAdapter.YatirimViewHolder> {

    private List<Yatirim> yatirimListesi;

    public YatirimAdapter(List<Yatirim> yatirimListesi) {
        this.yatirimListesi = yatirimListesi;
    }

    public void setYatirimListesi(List<Yatirim> yatirimListesi) {
        this.yatirimListesi = yatirimListesi;
        notifyDataSetChanged(); // Veri değiştiğinde RecyclerView'ı güncelle
    }

    @NonNull
    @Override
    public YatirimViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_yatirim, parent, false);
        return new YatirimViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull YatirimViewHolder holder, int position) {
        Yatirim yatirim = yatirimListesi.get(position);
        holder.textViewYatirimIsmi.setText(yatirim.getYatirimIsmi());
        holder.textViewYatirimDetay.setText(String.format(Locale.getDefault(),
                "Adet: %.2f, Birim Fiyat: %.2f TL", yatirim.getYatirimAdeti(), yatirim.getYatirimBirimFiyati()));
        holder.textViewYatirimTutari.setText(String.format(Locale.getDefault(),
                "Toplam Tutar: %.2f TL", yatirim.yatirimTutariHesapla()));
    }

    @Override
    public int getItemCount() {
        return yatirimListesi.size();
    }

    public static class YatirimViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewYatirimIsmi;
        public TextView textViewYatirimDetay;
        public TextView textViewYatirimTutari;

        public YatirimViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewYatirimIsmi = itemView.findViewById(R.id.textViewYatirimIsmi);
            textViewYatirimDetay = itemView.findViewById(R.id.textViewYatirimDetay);
            textViewYatirimTutari = itemView.findViewById(R.id.textViewYatirimTutari);
        }
    }
}