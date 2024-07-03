package com.example.appmovtlahuapan;

public class AdquirirSitioTuristicoActividades {
    private int IdActividad;
    private String Nombre;
    private String Costo;
    private String Descripcion;

    public AdquirirSitioTuristicoActividades() {
    }

    public AdquirirSitioTuristicoActividades(String nombre, String costo, String descripcion) {
        Nombre = nombre;
        Costo = costo;
        Descripcion = descripcion;
    }

    public int getIdActividad() {
        return IdActividad;
    }

    public String getNombre() {
        return Nombre;
    }

    public String getCosto() {
        return Costo;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setIdActividad(int idActividad) {
        IdActividad = idActividad;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public void setCosto(String costo) {
        Costo = costo;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    @Override
    public String toString(){
        return Nombre;
    }
}
