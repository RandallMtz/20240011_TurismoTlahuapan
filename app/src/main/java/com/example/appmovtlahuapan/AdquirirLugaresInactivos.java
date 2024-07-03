package com.example.appmovtlahuapan;

public class AdquirirLugaresInactivos {
    private String Nombre;
    private int IdLugar;

    public AdquirirLugaresInactivos() {
    }

    public AdquirirLugaresInactivos(String nombre, int idLugar) {
        Nombre = nombre;
        IdLugar = idLugar;
    }

    public String getNombre() {
        return Nombre;
    }

    public int getId() {
        return IdLugar;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public void setIdLugar(int idLugar) {
        IdLugar = idLugar;
    }
}
