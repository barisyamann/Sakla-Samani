package com.example.saklasamani.ui.yatirim;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.saklasamani.R;
import com.example.saklasamani.model.yatirim.Yatirim;
import com.example.saklasamani.model.yatirim.Borsa;
import com.example.saklasamani.model.yatirim.Coin;
import com.example.saklasamani.model.yatirim.DegerliMaden;
import com.example.saklasamani.model.yatirim.Doviz;
import java.util.List;
import java.util.Locale;

public class YatirimAdapter extends RecyclerView.Adapter<YatirimAdapter.YatirimViewHolder> {

    private List<Yatirim> yatirimListesi;
    private OnYatirimClickListener listener;

    public YatirimAdapter(List<Yatirim> yatirimListesi, OnYatirimClickListener listener) {
        this.yatirimListesi = yatirimListesi;
        this.listener = listener;
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
        holder.bind(yatirim, listener);
    }

    @Override
    public int getItemCount() {
        return yatirimListesi.size();
    }

    public static class YatirimViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewYatirimIsmi;
        public TextView textViewYatirimDetay;
        public TextView textViewYatirimTutari;
        public ImageView imageViewSil;
        public ImageView imageViewDuzenle;

        public YatirimViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewYatirimIsmi = itemView.findViewById(R.id.textViewYatirimIsmi);
            textViewYatirimDetay = itemView.findViewById(R.id.textViewYatirimDetay);
            textViewYatirimTutari = itemView.findViewById(R.id.textViewYatirimTutari);
            imageViewSil = itemView.findViewById(R.id.imageViewSil);
            imageViewDuzenle = itemView.findViewById(R.id.imageViewDuzenle);
        }

        public void bind(Yatirim yatirim, OnYatirimClickListener listener) {
            textViewYatirimIsmi.setText(yatirim.getYatirimIsmi());
            textViewYatirimDetay.setText(String.format(Locale.getDefault(),
                    "Adet: %.2f, Birim Fiyat: %.2f TL", yatirim.getYatirimAdeti(), yatirim.getYatirimBirimFiyati()));
            textViewYatirimTutari.setText(String.format(Locale.getDefault(),
                    "Toplam Tutar: %.2f TL", yatirim.yatirimTutariHesapla()));

            // Yeni: Yatırım türüne göre özel detay
            String ekstraBilgi = "";
            if (yatirim instanceof Coin) {
                Coin coin = (Coin) yatirim;
                ekstraBilgi = String.format("Coin Sembol: %s\nCoin Tipi: %s", coin.getCoinSembol(), coin.getCoinTipi());
            } else if (yatirim instanceof Borsa) {
                Borsa hisse = (Borsa) yatirim;
                ekstraBilgi = String.format("Şirket: %s\nSembol: %s", hisse.getSirketAdi(), hisse.getSembol());
            } else if (yatirim instanceof Doviz) {
                Doviz doviz = (Doviz) yatirim;
                ekstraBilgi = String.format("Döviz Cinsi: %s", doviz.getDovizCinsi());
            } else if (yatirim instanceof DegerliMaden) {
                DegerliMaden maden = (DegerliMaden) yatirim;
                ekstraBilgi = String.format("Maden Türü: %s", maden.getMadenTuru());
            }

            // Eğer yeni TextView eklersen:
            // textViewEkstraDetay.setText(ekstraBilgi);

            // Eğer aynı metin alanında göstermek istersen:
            textViewYatirimDetay.append("\n" + ekstraBilgi);

            imageViewSil.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onSilClick(position);
                }
            });

            imageViewDuzenle.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onDuzenleClick(position);
                }
            });
        }

    }

    public interface OnYatirimClickListener {
        void onSilClick(int position);
        void onDuzenleClick(int position);
    }
}