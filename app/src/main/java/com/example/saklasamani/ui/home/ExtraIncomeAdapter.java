package com.example.saklasamani.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.saklasamani.R;
import com.example.saklasamani.model.ExtraIncome;

import java.util.List;

public class ExtraIncomeAdapter extends RecyclerView.Adapter<ExtraIncomeAdapter.ViewHolder> {

    public interface OnDeleteClickListener {
        void onDelete(int position);
    }

    private List<ExtraIncome> extraIncomes;
    private OnDeleteClickListener listener;

    public ExtraIncomeAdapter(List<ExtraIncome> extraIncomes, OnDeleteClickListener listener) {
        this.extraIncomes = extraIncomes;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView extraIncomeAmount;
        Button btnDeleteExtra;

        public ViewHolder(View view) {
            super(view);
            extraIncomeAmount = view.findViewById(R.id.extraIncomeAmount);
            btnDeleteExtra = view.findViewById(R.id.btnDeleteExtra);
        }
    }

    @NonNull
    @Override
    public ExtraIncomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_extra_income, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ExtraIncomeAdapter.ViewHolder holder, int position) {
        ExtraIncome income = extraIncomes.get(position);
        holder.extraIncomeAmount.setText(income.getAmount() + "₺ - " + income.getNote());

        holder.btnDeleteExtra.setOnClickListener(view -> {
            if (listener != null) {
                listener.onDelete(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return extraIncomes.size();
    }
}
