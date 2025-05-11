package com.example.saklasamani.manager;

import com.example.saklasamani.model.User;

public class SessionManager {
    private static SessionManager instance;
    private User currentUser;

    private SessionManager() { }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void setUser(User user) {
        this.currentUser = user;
    }

    public User getUser() {
        return currentUser;
    }

    public void clearUser() {
        currentUser = null;
    }

}
