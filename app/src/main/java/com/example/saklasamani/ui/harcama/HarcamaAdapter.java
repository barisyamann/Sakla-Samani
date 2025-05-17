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
    private List<Harcama> harcamalar = new ArrayList<>();
    private OnHarcamaLongClickListener longClickListener;
    private OnHarcamaClickListener clickListener;  // Normal tıklama listener

    // Uzun tıklama için interface
    public interface OnHarcamaLongClickListener {
        void onHarcamaLongClicked(Harcama harcama);
    }

    // Normal tıklama için interface
    public interface OnHarcamaClickListener {
        void onHarcamaClicked(Harcama harcama);
    }

    public void setOnHarcamaLongClickListener(OnHarcamaLongClickListener listener) {
        this.longClickListener = listener;
    }

    public void setOnHarcamaClickListener(OnHarcamaClickListener listener) {
        this.clickListener = listener;
    }

    public void setHarcamalar(List<Harcama> harcamaList) {
        this.harcamalar = harcamaList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HarcamaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.harcama_item, parent, false);
        return new HarcamaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HarcamaViewHolder holder, int position) {
        Harcama harcama = harcamalar.get(position);
        holder.bind(harcama);
    }

    @Override
    public int getItemCount() {
        return harcamalar.size();
    }

    class HarcamaViewHolder extends RecyclerView.ViewHolder {
        TextView textViewAmount, textViewCategory, textViewNote;

        HarcamaViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewAmount = itemView.findViewById(R.id.textAmount);
            textViewCategory = itemView.findViewById(R.id.textCategory);
            textViewNote = itemView.findViewById(R.id.textNote);
        }

        void bind(Harcama harcama) {
            textViewAmount.setText(String.format("%.2f ₺", harcama.getAmount()));
            textViewCategory.setText(harcama.getCategory());
            textViewNote.setText(harcama.getNote());

            // Uzun tıklama
            itemView.setOnLongClickListener(v -> {
                if (longClickListener != null) {
                    longClickListener.onHarcamaLongClicked(harcama);
                }
                return true;
            });

            // Normal tıklama
            itemView.setOnClickListener(v -> {
                if (clickListener != null) {
                    clickListener.onHarcamaClicked(harcama);
                }
            });
        }
    }
}
