package com.example.appmovtlahuapan;

public class AdquirirCodigosPostales {
    private int IdCodigoPostal;
    private String CodigoPostal;

    public AdquirirCodigosPostales() {
    }

    public AdquirirCodigosPostales(int idCodigoPostal, String codigoPostal) {
        IdCodigoPostal = idCodigoPostal;
        CodigoPostal = codigoPostal;
    }

    public int getIdCodigoPostal() {
        return IdCodigoPostal;
    }

    public String getCodigoPostal() {
        return CodigoPostal;
    }

    public void setIdCodigoPostal(int idCodigoPostal) {
        IdCodigoPostal = idCodigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        CodigoPostal = codigoPostal;
    }

    @Override
    public String toString(){
        return CodigoPostal;
    }
}
