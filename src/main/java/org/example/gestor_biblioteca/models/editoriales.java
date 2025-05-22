package org.example.gestor_biblioteca.models;

public class editoriales {
    private int id_editorial;
    private String editorial;

    public editoriales(){}

    public editoriales(int id_editorial, String editorial) {
        this.id_editorial = id_editorial;
        this.editorial = editorial;
    }

    public int getId_editorial() {
        return id_editorial;
    }

    public void setId_editorial(int id_editorial) {
        this.id_editorial = id_editorial;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }
}
