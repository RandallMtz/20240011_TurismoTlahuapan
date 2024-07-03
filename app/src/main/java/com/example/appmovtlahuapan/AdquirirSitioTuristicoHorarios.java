package com.example.appmovtlahuapan;

public class AdquirirSitioTuristicoHorarios {
    private int IdHorario;
    private String Dia;
    private String HoraInicio;
    private String HoraFin;
    private String IdLugar;

    public AdquirirSitioTuristicoHorarios() {
    }

    public AdquirirSitioTuristicoHorarios(int idHorario, String dia, String horaIn, String horaFn, String idLugar) {
        IdHorario = idHorario;
        Dia = dia;
        HoraInicio = horaIn;
        HoraFin = horaFn;
        IdLugar = idLugar;
    }

    public int getIdHorario() {
        return IdHorario;
    }

    public String getDia() {
        return Dia;
    }

    public String getHoraIn() {
        return HoraInicio;
    }

    public String getHoraFn() {
        return HoraFin;
    }

    public String getIdLugar() {
        return IdLugar;
    }

    public void setIdHorario(int idHorario) {
        IdHorario = idHorario;
    }

    public void setDia(String dia) {
        Dia = dia;
    }

    public void setHoraIn(String horaIn) {
        HoraInicio = horaIn;
    }

    public void setHoraFn(String horaFn) {
        HoraFin = horaFn;
    }

    public void setIdLugar(String idLugar) {
        IdLugar = idLugar;
    }

}
