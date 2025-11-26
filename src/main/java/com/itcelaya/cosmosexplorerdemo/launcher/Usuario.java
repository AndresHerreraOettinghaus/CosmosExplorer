package com.itcelaya.cosmosexplorerdemo.launcher;

public class Usuario {
    private int id;
    private String nombreUsuario;
    private String username;
    private String rol;

    public Usuario(int id, String nombreUsuario, String username, String rol) {
        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.username = username;
        this.rol = rol;
    }

    // Getters
    public int getId() { return id; }
    public String getNombreUsuario() { return nombreUsuario; }
    public String getUsername() { return username; }
    public String getRol() { return rol; }
}
