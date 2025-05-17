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
    public void addCoinInvestment(Coin coin) {
        User user = userDao.getUserByUserName(coin.getUserName());
        double investmentAmount = coin.yatirimTutariHesapla();

        if (user != null && user.getBudget() >= investmentAmount) {
            yatirimDao.addCoin(coin);

            double newBudget = user.getBudget() - investmentAmount;
            userDao.updateBudget(coin.getUserName(), newBudget);

            // Güncel kullanıcıyı SessionManager'a yaz (SessionManager sınıfının varlığını varsayıyorum)
            User updatedUser = userDao.getUserByUserName(coin.getUserName());
            SessionManager.getInstance().setUser(updatedUser);
        }
    }

    // --- TÜM COINLERİ GETİR ---
    public List<Coin> getAllCoins(String userName) {
        return yatirimDao.getCoins(userName);
    }

    // --- BORSA EKLE ---
    public void addBorsaInvestment(Borsa borsa) {
        User user = userDao.getUserByUserName(borsa.getUserName());
        double investmentAmount = borsa.yatirimTutariHesapla();

        if (user != null && user.getBudget() >= investmentAmount) {
            yatirimDao.addBorsa(borsa);

            double newBudget = user.getBudget() - investmentAmount;
            userDao.updateBudget(borsa.getUserName(), newBudget);

            // Güncel kullanıcıyı SessionManager'a yaz
            User updatedUser = userDao.getUserByUserName(borsa.getUserName());
            SessionManager.getInstance().setUser(updatedUser);
        }
    }

    // --- TÜM BORSALARI GETİR ---
    public List<Borsa> getAllBorsalar(String userName) {
        return yatirimDao.getBorsalar(userName);
    }

    // --- DÖVİZ EKLE ---
    public void addDovizInvestment(Doviz doviz) {
        User user = userDao.getUserByUserName(doviz.getUserName());
        double investmentAmount = doviz.yatirimTutariHesapla();

        if (user != null && user.getBudget() >= investmentAmount) {
            yatirimDao.addDoviz(doviz);

            double newBudget = user.getBudget() - investmentAmount;
            userDao.updateBudget(doviz.getUserName(), newBudget);

            // Güncel kullanıcıyı SessionManager'a yaz
            User updatedUser = userDao.getUserByUserName(doviz.getUserName());
            SessionManager.getInstance().setUser(updatedUser);
        }
    }

    // --- TÜM DÖVİZLERİ GETİR ---
    public List<Doviz> getAllDovizler(String userName) {
        return yatirimDao.getDovizler(userName);
    }

    // --- DEĞERLİ MADEN EKLE ---
    public void addDegerliMadenInvestment(DegerliMaden degerliMaden) {
        User user = userDao.getUserByUserName(degerliMaden.getUserName());
        double investmentAmount = degerliMaden.yatirimTutariHesapla();

        if (user != null && user.getBudget() >= investmentAmount) {
            yatirimDao.addDegerliMaden(degerliMaden);

            double newBudget = user.getBudget() - investmentAmount;
            userDao.updateBudget(degerliMaden.getUserName(), newBudget);

            // Güncel kullanıcıyı SessionManager'a yaz
            User updatedUser = userDao.getUserByUserName(degerliMaden.getUserName());
            SessionManager.getInstance().setUser(updatedUser);
        }
    }

    // --- TÜM DEĞERLİ MADENLERİ GETİR ---
    public List<DegerliMaden> getAllDegerliMadenler(String userName) {
        return yatirimDao.getDegerliMadenler(userName);
    }
}