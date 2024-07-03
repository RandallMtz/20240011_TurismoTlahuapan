package com.example.appmovtlahuapan;

public class AdquirirNivelesAdministrativos {
    private int IdNivelAdministracion;
    private String NivelAdministracion;

    public AdquirirNivelesAdministrativos(){

    }

    public AdquirirNivelesAdministrativos(int IdNivelAdministracion, String NivelAdministracion){
        this.IdNivelAdministracion = IdNivelAdministracion;
        this.NivelAdministracion = NivelAdministracion;
    }

    public int getIdNivel() {
        return IdNivelAdministracion;
    }

    public String getNivel() {
        return NivelAdministracion;
    }

    public void setIdNivel(int IdNivelAdministracion) {
        this.IdNivelAdministracion = IdNivelAdministracion;
    }

    public void setNivel(String NivelAdministracion) {
        this.NivelAdministracion = NivelAdministracion;
    }

    @Override
    public String toString(){
        return NivelAdministracion;
    }
}
