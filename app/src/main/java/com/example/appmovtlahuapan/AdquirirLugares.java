package com.example.appmovtlahuapan;

public class AdquirirLugares {
    private int IdLugar;
    private String Nombre;
    private int CostoEntrada;
    private String Calle;
    private String NoInt;
    private String NoExt;;
    private String Colonia;

    public AdquirirLugares() {
    }

    public AdquirirLugares(String nombre, int costoEntrada, String calle, String noInt, String noExt, String colonia) {
        Nombre = nombre;
        CostoEntrada = costoEntrada;
        Calle = calle;
        NoInt = noInt;
        NoExt = noExt;
        Colonia = colonia;
    }

    public int getIdLugar() {
        return IdLugar;
    }

    public String getNombre() {
        return Nombre;
    }

    public int getCostoEntrada() {
        return CostoEntrada;
    }

    public String getCalle() {
        return Calle;
    }

    public String getNoInt() {
        return NoInt;
    }

    public String getNoExt() {
        return NoExt;
    }

    public String getColonia() {
        return Colonia;
    }

    public void setIdLugar(int idLugar) {
        IdLugar = idLugar;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public void setCostoEntrada(int costoEntrada) {
        CostoEntrada = costoEntrada;
    }

    public void setCalle(String calle) {
        Calle = calle;
    }

    public void setNoInt(String noInt) {
        NoInt = noInt;
    }

    public void setNoExt(String noExt) {
        NoExt = noExt;
    }

    public void setColonia(String colonia) {
        Colonia = colonia;
    }

    @Override
    public String toString(){
        return Nombre;
    }
}
