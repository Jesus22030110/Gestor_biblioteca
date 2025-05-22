package org.example.gestor_biblioteca.models;

public class categorias {
    private int id_categoria;
    private String categoria;
    private String descripcion;

    public categorias() {}

    public categorias(int id_categoria, String categoria, String descripcion) {
        this.id_categoria = id_categoria;
        this.categoria = categoria;
        this.descripcion = descripcion;
    }

    public void setId_categoria(int id_categoria) {
        this.id_categoria = id_categoria;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
