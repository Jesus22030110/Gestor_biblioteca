package org.example.gestor_biblioteca.models;

public class libros {
    private int id_libro;
    private String titulo_libro;
    private int ano_publicacion;
    private int id_editorial;
    private int id_autor;
    private int id_categoria;

    public libros(){}

    public libros(int id_libro, String titulo_libro, int ano_publicacion, int id_editorial, int id_autor) {
        this.id_libro = id_libro;
        this.titulo_libro = titulo_libro;
        this.ano_publicacion = ano_publicacion;
        this.id_editorial = id_editorial;
        this.id_autor = id_autor;
        this.id_categoria = id_categoria;
    }

    public int getId_libro() {
        return id_libro;
    }

    public void setId_libro(int id_libro) {
        this.id_libro = id_libro;
    }

    public String getTitulo_libro() {
        return titulo_libro;
    }

    public void setTitulo_libro(String titulo_libro) {
        this.titulo_libro = titulo_libro;
    }

    public int getAno_publicacion() {
        return ano_publicacion;
    }

    public void setAno_publicacion(int ano_publicacion) {
        this.ano_publicacion = ano_publicacion;
    }

    public int getId_editorial() {
        return id_editorial;
    }

    public void setId_editorial(int id_editorial) {
        this.id_editorial = id_editorial;
    }

    public int getId_autor() {
        return id_autor;
    }

    public void setId_autor(int id_autor) {
        this.id_autor = id_autor;
    }

    public int getId_categoria() {
        return id_categoria;
    }

    public void setId_categoria(int id_categoria) {
        this.id_categoria = id_categoria;
    }

}
