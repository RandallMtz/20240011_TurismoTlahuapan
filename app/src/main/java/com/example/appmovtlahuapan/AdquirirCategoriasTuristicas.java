package com.example.appmovtlahuapan;

public class AdquirirCategoriasTuristicas {
    private int IdCategoria;
    private String Categoria;
    private String FotoPromocional;

    public AdquirirCategoriasTuristicas(){

    }

    public AdquirirCategoriasTuristicas(int idCategoria, String categoria) {
        IdCategoria = idCategoria;
        Categoria = categoria;
    }

    public AdquirirCategoriasTuristicas(int idCategoria, String categoria, String fotoPromocional) {
        IdCategoria = idCategoria;
        Categoria = categoria;
        FotoPromocional = fotoPromocional;
    }

    public int getIdCategoria() {
        return IdCategoria;
    }

    public String getCategoria() {
        return Categoria;
    }

    public String getFotoPromocional() {
        return FotoPromocional;
    }

    public void setIdCategoria(int IdCategoria) {
        this.IdCategoria = IdCategoria;
    }

    public void setCategoria(String Nivel) {
        this.Categoria = Nivel;
    }

    public void setFotoPromocional(String fotoPromocional) {
        FotoPromocional = fotoPromocional;
    }

    @Override
    public String toString(){
        return Categoria;
    }
}
