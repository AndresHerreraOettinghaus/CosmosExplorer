package com.itcelaya.cosmosexplorerdemo.models.planet;

public class Apod {
    private int idApod;
    private String fecha;
    private String titulo;
    private String explicacion;
    private String tipoMedia;
    private String url;
    private String version;
    private String fechaGuardado;

    public Apod(String fecha, String titulo, String explicacion, String tipoMedia, String url, String version) {
        this.fecha = fecha;
        this.titulo = titulo;
        this.explicacion = explicacion;
        this.tipoMedia = tipoMedia;
        this.url = url;
        this.version = version;
    }

    // Getters y setters
    public int getIdApod() { return idApod; }
    public void setIdApod(int idApod) { this.idApod = idApod; }

    public String getFecha() { return fecha; }
    public String getTitulo() { return titulo; }
    public String getExplicacion() { return explicacion; }
    public String getTipoMedia() { return tipoMedia; }
    public String getUrl() { return url; }
    public String getVersion() { return version; }

    public String getFechaGuardado() { return fechaGuardado; }
    public void setFechaGuardado(String fechaGuardado) { this.fechaGuardado = fechaGuardado; }
}
