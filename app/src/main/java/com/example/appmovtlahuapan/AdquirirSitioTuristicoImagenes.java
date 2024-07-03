package com.example.appmovtlahuapan;

public class AdquirirSitioTuristicoImagenes {
    private String IdImagen;
    private String Foto;
    private String Nombre;
    private String IdLugar;
    private String Descripcion;

    public AdquirirSitioTuristicoImagenes() {
    }

    public AdquirirSitioTuristicoImagenes(String idImagen, String foto, String nombre, String idLugar, String descripcion) {
        IdImagen = idImagen;
        Foto = foto;
        Nombre = nombre;
        IdLugar = idLugar;
        Descripcion = descripcion;
    }

    public String getIdImagen() {
        return IdImagen;
    }

    public String getFoto() {
        return Foto;
    }

    public String getNombre() {
        return Nombre;
    }

    public String getIdLugar() {
        return IdLugar;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setIdImagen(String idImagen) {
        IdImagen = idImagen;
    }

    public void setFoto(String foto) {
        this.Foto = foto;
    }

    public void setNombre(String nombre) {
        this.Nombre = nombre;
    }

    public void setIdLugar(String idLugar) {
        IdLugar = idLugar;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }
}
