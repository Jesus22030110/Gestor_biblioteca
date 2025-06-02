package org.example.gestor_biblioteca.models;

public class usuarios {
    private int id_usuario;
    private String nombre;
    private String primer_apellido;
    private String segundo_apellido;
    private String email;
    private String telefono;
    private int rol;
    private String contrasena;

    public usuarios() {}

    public usuarios(int id_usuario, String nombre, String primer_apellido,
                    String segundo_apellido, String email, String telefono,
                    int rol, String contrasena) {
        this.id_usuario = id_usuario;
        this.nombre = nombre;
        this.primer_apellido = primer_apellido;
        this.segundo_apellido = segundo_apellido;
        this.email = email;
        this.telefono = telefono;
        this.rol = rol;
        this.contrasena = contrasena;
    }

    // Getters y Setters
    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPrimer_apellido() {
        return primer_apellido;
    }

    public void setPrimer_apellido(String primer_apellido) {
        this.primer_apellido = primer_apellido;
    }

    public String getSegundo_apellido() {
        return segundo_apellido;
    }

    public void setSegundo_apellido(String segundo_apellido) {
        this.segundo_apellido = segundo_apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public int getRol() {
        return rol;
    }

    public void setRol(int rol) {
        this.rol = rol;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
}