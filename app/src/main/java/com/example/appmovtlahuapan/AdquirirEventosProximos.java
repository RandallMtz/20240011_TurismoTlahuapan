package com.example.appmovtlahuapan;

public class AdquirirEventosProximos {
    private String IdEvento;
    private String Nombre;
    private String Descripcion;
    private String Foto;
    private String FotoNombre;
    private String Fecha;
    private String FechaFinal;
    private String Hora;
    private String HoraFinal;
    private String Lugar;

    public AdquirirEventosProximos() {
    }

    public AdquirirEventosProximos(String nombre, String descripcion, String foto, String fotoNombre, String fecha, String fechaFinal, String hora, String horafinal, String lugar) {
        Nombre = nombre;
        Descripcion = descripcion;
        Foto = foto;
        FotoNombre = fotoNombre;
        Fecha = fecha;
        FechaFinal = fechaFinal;
        Hora = hora;
        HoraFinal = horafinal;
        Lugar = lugar;
    }

    public AdquirirEventosProximos(String idEvento, String nombre, String descripcion, String foto, String fotoNombre, String fecha, String fechaFinal, String hora, String horaFinal, String lugar) {
        IdEvento = idEvento;
        Nombre = nombre;
        Descripcion = descripcion;
        Foto = foto;
        FotoNombre = fotoNombre;
        Fecha = fecha;
        FechaFinal = fechaFinal;
        Hora = hora;
        HoraFinal = horaFinal;
        Lugar = lugar;
    }

    public String getIdEvento() {
        return IdEvento;
    }

    public String getNombre() {
        return Nombre;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public String getFoto() {
        return Foto;
    }

    public String getFotoNombre() {
        return FotoNombre;
    }

    public String getFecha() {
        return Fecha;
    }

    public String getFechaFinal() {return FechaFinal;}

    public String getHora() {
        return Hora;
    }

    public String getHoraFinal() {return HoraFinal;}

    public String getLugar() {
        return Lugar;
    }

    public void setIdEvento(String idEvento) {
        IdEvento = idEvento;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public void setFoto(String foto) {
        Foto = foto;
    }

    public void setFotoNombre(String fotoNombre) {
        FotoNombre = fotoNombre;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public void setFechaFinal(String fechaFinal) {FechaFinal = fechaFinal;}

    public void setHora(String hora) {
        Hora = hora;
    }

    public void setHoraFinal(String horaFinal) {HoraFinal = horaFinal;}

    public void setLugar(String lugar) {
        Lugar = lugar;
    }

    @Override
    public String toString(){
        return Nombre;
    }
}
