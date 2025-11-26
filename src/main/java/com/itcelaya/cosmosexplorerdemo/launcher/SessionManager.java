package com.itcelaya.cosmosexplorerdemo.launcher;


public class SessionManager {
    private static SessionManager instance;
    private Usuario usuarioLogueado;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void login(Usuario usuario) {
        this.usuarioLogueado = usuario;
    }

    public void logout() {
        this.usuarioLogueado = null;
    }

    public Usuario getUsuarioLogueado() {
        return usuarioLogueado;
    }

    public boolean isLoggedIn() {
        return usuarioLogueado != null;
    }
}
