package com.example.appmovtlahuapan;

public class AdquirirColonias {
    private int IdColonia;
    private String Colonia;
    private int IdCodigoPostal;

    public AdquirirColonias() {
    }

    public AdquirirColonias(int idColonia, String colonia, int idCodigoPostal) {
        IdColonia = idColonia;
        Colonia = colonia;
        IdCodigoPostal = idCodigoPostal;
    }

    public int getIdColonia() {
        return IdColonia;
    }

    public String getColonia() {
        return Colonia;
    }

    public int getIdCodigoPostal() {
        return IdCodigoPostal;
    }

    public void setIdColonia(int idColonia) {
        IdColonia = idColonia;
    }

    public void setColonia(String colonia) {
        Colonia = colonia;
    }

    public void setIdCodigoPostal(int idCodigoPostal) {
        IdCodigoPostal = idCodigoPostal;
    }

    @Override
    public String toString(){
        return Colonia;
    }

}
