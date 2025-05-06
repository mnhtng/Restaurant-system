package main.java.util;

import main.java.model.Member;

public class Session {
    private static Session instance;
    private Member currentUser;

    private Session() {
        currentUser = null;
    }

    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    public void setCurrentUser(Member user) {
        this.currentUser = user;
    }

    public Member getCurrentUser() {
        return this.currentUser;
    }

    public void clear() {
        this.currentUser = null;
    }

    public boolean isAuthenticated() {
        return this.currentUser != null;
    }
}
