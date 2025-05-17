package com.example.saklasamani.manager;

import android.content.Context;

import com.example.saklasamani.data.UserDao;
import com.example.saklasamani.data.YatirimDao;
import com.example.saklasamani.model.yatirim.Borsa;
import com.example.saklasamani.model.yatirim.Coin;
import com.example.saklasamani.model.User;
import com.example.saklasamani.model.yatirim.DegerliMaden;
import com.example.saklasamani.model.yatirim.Doviz;

import java.util.List;

public class YatirimManager {

    private YatirimDao yatirimDao;
    private UserDao userDao;

    public YatirimManager(Context context) {
        yatirimDao = new YatirimDao(context);
        userDao = new UserDao(context);
    }

    // --- COIN EKLE ---
    public boolean addCoinInvestment(Coin coin) {
        User user = userDao.getUserByUserName(coin.getUserName());
        double investmentAmount = coin.yatirimTutariHesapla();

        if (user != null && user.getBudget() >= investmentAmount) {
            yatirimDao.addCoin(coin);
            userDao.updateBudget(coin.getUserName(), user.getBudget() - investmentAmount);
            SessionManager.getInstance().setUser(userDao.getUserByUserName(coin.getUserName()));
            return true;
        }
        return false;
    }


    // --- TÜM COINLERİ GETİR ---
    public List<Coin> getAllCoins(String userName) {
        return yatirimDao.getCoins(userName);
    }

    // --- BORSA EKLE ---
    public boolean addBorsaInvestment(Borsa borsa) {
        User user = userDao.getUserByUserName(borsa.getUserName());
        double investmentAmount = borsa.yatirimTutariHesapla();

        if (user != null && user.getBudget() >= investmentAmount) {
            yatirimDao.addBorsa(borsa);
            userDao.updateBudget(borsa.getUserName(), user.getBudget() - investmentAmount);
            SessionManager.getInstance().setUser(userDao.getUserByUserName(borsa.getUserName()));
            return true;
        }
        return false;
    }


    // --- TÜM BORSALARI GETİR ---
    public List<Borsa> getAllBorsalar(String userName) {
        return yatirimDao.getBorsalar(userName);
    }

    // --- DÖVİZ EKLE ---
    public boolean addDovizInvestment(Doviz doviz) {
        User user = userDao.getUserByUserName(doviz.getUserName());
        double investmentAmount = doviz.yatirimTutariHesapla();

        if (user != null && user.getBudget() >= investmentAmount) {
            yatirimDao.addDoviz(doviz);
            userDao.updateBudget(doviz.getUserName(), user.getBudget() - investmentAmount);
            SessionManager.getInstance().setUser(userDao.getUserByUserName(doviz.getUserName()));
            return true;
        }
        return false;
    }


    // --- TÜM DÖVİZLERİ GETİR ---
    public List<Doviz> getAllDovizler(String userName) {
        return yatirimDao.getDovizler(userName);
    }

    // --- DEĞERLİ MADEN EKLE ---
    public boolean addDegerliMadenInvestment(DegerliMaden degerliMaden) {
        User user = userDao.getUserByUserName(degerliMaden.getUserName());
        double investmentAmount = degerliMaden.yatirimTutariHesapla();

        if (user != null && user.getBudget() >= investmentAmount) {
            yatirimDao.addDegerliMaden(degerliMaden);
            userDao.updateBudget(degerliMaden.getUserName(), user.getBudget() - investmentAmount);

            // SessionManager'daki kullanıcıyı güncelle
            SessionManager.getInstance().setUser(userDao.getUserByUserName(degerliMaden.getUserName()));

            return true; // yatırım ve bütçe işlemi başarılı
        }

        return false; // yetersiz bütçe
    }

    // --- TÜM DEĞERLİ MADENLERİ GETİR ---
    public List<DegerliMaden> getAllDegerliMadenler(String userName) {
        return yatirimDao.getDegerliMadenler(userName);
    }
}