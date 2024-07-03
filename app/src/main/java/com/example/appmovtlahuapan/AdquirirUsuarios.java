package com.example.appmovtlahuapan;

public class AdquirirUsuarios {
    private int IdUsuario;
    private String Usuario;
    private String Nombre;
    private String ApePater;
    private String ApeMater;
    private String Correo;
    private String Telefono;

    public AdquirirUsuarios() {
    }

    public AdquirirUsuarios(String usuario, String nombre, String apePater, String apeMater, String correo, String telefono) {
        Usuario = usuario;
        Nombre = nombre;
        ApePater = apePater;
        ApeMater = apeMater;
        Correo = correo;
        Telefono = telefono;
    }

    public int getIdUsuario() {
        return IdUsuario;
    }

    public String getUsuario() {
        return Usuario;
    }

    public String getNombre() {
        return Nombre;
    }

    public String getApePater() {
        return ApePater;
    }

    public String getApeMater() {
        return ApeMater;
    }

    public String getCorreo() {
        return Correo;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setIdUsuario(int idUsuario) {
        IdUsuario = idUsuario;
    }

    public void setUsuario(String usuario) {
        Usuario = usuario;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public void setApePater(String apePater) {
        ApePater = apePater;
    }

    public void setApeMater(String apeMater) {
        ApeMater = apeMater;
    }

    public void setCorreo(String correo) {
        Correo = correo;
    }

    public void setTelefono(String telefono) {
        Telefono = telefono;
    }

    @Override
    public String toString(){
        return Usuario;
    }
}
