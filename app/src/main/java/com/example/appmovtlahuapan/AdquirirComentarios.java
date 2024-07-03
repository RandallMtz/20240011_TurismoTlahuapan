package com.example.appmovtlahuapan;

public class AdquirirComentarios {
    private String IdComentario;
    private String Comentario;
    private String Fecha;
    private String IdUsuario;
    private String IdLugar;
    private String Nombre;

    public AdquirirComentarios() {
    }

    public AdquirirComentarios(String idComentario, String comentario, String fecha, String idUsuario, String idLugar, String nombre) {
        IdComentario = idComentario;
        Comentario = comentario;
        Fecha = fecha;
        IdUsuario = idUsuario;
        IdLugar = idLugar;
        Nombre = nombre;
    }

    public String getIdComentario() {
        return IdComentario;
    }

    public String getComentario() {
        return Comentario;
    }

    public String getFecha() {
        return Fecha;
    }

    public String getIdUsuario() {
        return IdUsuario;
    }

    public String getIdLugar() {
        return IdLugar;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setIdComentario(String idComentario) {
        IdComentario = idComentario;
    }

    public void setComentario(String comentario) {
        Comentario = comentario;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public void setIdUsuario(String idUsuario) {
        IdUsuario = idUsuario;
    }

    public void setIdLugar(String idLugar) {
        IdLugar = idLugar;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    @Override
    public String toString(){
        return Comentario;
    }

}
