package com.example.saklasamani.ui.gelir;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.saklasamani.R;
import com.example.saklasamani.data.ExtraIncomeDao;
import com.example.saklasamani.data.UserDao;
import com.example.saklasamani.manager.SessionManager;
import com.example.saklasamani.model.ExtraIncome;
import com.example.saklasamani.model.User;

import java.util.ArrayList;

public class GelirFragment extends Fragment {

    private TextView tvIncome, tvTotalIncome, tvTotalExtra;
    private EditText etAmount, etNote, etMainIncome;
    private Button btnAddExtraToggle, btnConfirmExtra, btnUpdateIncome;
    private ListView lvExtraIncomes;
    private LinearLayout extraInputLayout;

    private User user;
    private UserDao userDao;
    private ExtraIncomeDao extraIncomeDao;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> incomeNotes;
    private ArrayList<ExtraIncome> extraIncomeList;
    private boolean isExtraInputVisible = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gelir, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // User ve DAO nesnelerini alıyoruz
        user = SessionManager.getInstance().getUser();
        userDao = new UserDao(requireContext());
        extraIncomeDao = new ExtraIncomeDao(requireContext());

        // XML elemanlarını bağlama
        tvIncome = view.findViewById(R.id.tvIncome);
        tvTotalExtra = view.findViewById(R.id.tvTotalExtra);
        tvTotalIncome = view.findViewById(R.id.tvTotalIncome);
        etAmount = view.findViewById(R.id.etExtraAmount);
        etNote = view.findViewById(R.id.etExtraNote);
        btnAddExtraToggle = view.findViewById(R.id.btnAddExtra);
        btnConfirmExtra = view.findViewById(R.id.btnConfirmExtra);
        btnUpdateIncome = view.findViewById(R.id.btnUpdateIncome);
        etMainIncome = view.findViewById(R.id.etMainIncome);
        extraInputLayout = view.findViewById(R.id.extraInputLayout);
        lvExtraIncomes = view.findViewById(R.id.lvExtraIncomes);

        // Ek gelirleri çekiyoruz
        extraIncomeList = new ArrayList<>(extraIncomeDao.getExtraIncomes(user.getUserName()));
        incomeNotes = new ArrayList<>();
        for (ExtraIncome ei : extraIncomeList) {
            incomeNotes.add(ei.getAmount() + "₺ - " + ei.getNote());
        }

        // ListView adapter'ı
        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, incomeNotes);
        lvExtraIncomes.setAdapter(adapter);

        updateUI();

        btnUpdateIncome.setOnClickListener(v -> {
            String incomeStr = etMainIncome.getText().toString().trim();
            if (!incomeStr.isEmpty()) {
                double newIncome = Double.parseDouble(incomeStr);
                double oldIncome = user.getIncome();

                user.setIncome(newIncome);
                userDao.updateIncome(user.getUserName(), newIncome);

                // Gelir değişimi kadar bütçeyi güncelle
                double difference = newIncome - oldIncome;
                if (difference > 0) {
                    userDao.increaseBudget(user.getUserName(), difference);
                } else if (difference < 0) {
                    userDao.decreaseBudget(user.getUserName(), -difference);
                }

                // Güncellenmiş user bilgilerini al ve SessionManager'a kaydet
                user = userDao.getUserByUserName(user.getUserName());
                SessionManager.getInstance().setUser(user);

                updateUI();
                Toast.makeText(requireContext(), "Gelir ve bütçe güncellendi.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Gelir boş olamaz.", Toast.LENGTH_SHORT).show();
            }
        });

        btnAddExtraToggle.setOnClickListener(v -> {
            isExtraInputVisible = !isExtraInputVisible;
            extraInputLayout.setVisibility(isExtraInputVisible ? View.VISIBLE : View.GONE);
        });

        btnConfirmExtra.setOnClickListener(v -> {
            String amountStr = etAmount.getText().toString().trim();
            String note = etNote.getText().toString().trim();

            if (amountStr.isEmpty() || note.isEmpty()) {
                Toast.makeText(requireContext(), "Tüm alanları doldurun.", Toast.LENGTH_SHORT).show();
                return;
            }

            double amount = Double.parseDouble(amountStr);

            extraIncomeDao.addExtraIncome(user.getUserName(), amount, note);
            extraIncomeList.add(new ExtraIncome(amount, note));
            incomeNotes.add(amount + "₺ - " + note);
            adapter.notifyDataSetChanged();

            // Bütçeyi artır
            userDao.increaseBudget(user.getUserName(), amount);
            // Güncellenmiş user bilgilerini al ve SessionManager'a kaydet
            user = userDao.getUserByUserName(user.getUserName());
            SessionManager.getInstance().setUser(user);

            etAmount.setText("");
            etNote.setText("");
            updateUI();
        });

        lvExtraIncomes.setOnItemClickListener((parent, view1, position, id) -> {
            ExtraIncome toRemove = extraIncomeList.get(position);
            boolean deleted = extraIncomeDao.deleteExtraIncome(user.getUserName(), toRemove.getNote());

            if (deleted) {
                Toast.makeText(requireContext(), "Silindi", Toast.LENGTH_SHORT).show();

                // Bütçeden azalt
                userDao.decreaseBudget(user.getUserName(), toRemove.getAmount());

                extraIncomeList.remove(position);
                incomeNotes.remove(position);
                adapter.notifyDataSetChanged();

                // Güncellenmiş user bilgilerini al ve SessionManager'a kaydet
                user = userDao.getUserByUserName(user.getUserName());
                SessionManager.getInstance().setUser(user);

                updateUI();
            }
        });
    }

    private void updateUI() {
        tvIncome.setText("Gelir: " + user.getIncome() + "₺");

        double totalExtra = 0;
        for (ExtraIncome ei : extraIncomeList) {
            totalExtra += ei.getAmount();
        }

        tvTotalExtra.setText("Ek Gelir Toplamı: " + totalExtra + "₺");
        tvTotalIncome.setText("Toplam Gelir: " + (user.getIncome() + totalExtra) + "₺");
        etMainIncome.setText(String.valueOf(user.getIncome()));
    }
}
