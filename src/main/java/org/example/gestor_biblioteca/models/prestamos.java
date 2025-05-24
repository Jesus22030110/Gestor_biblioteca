package org.example.gestor_biblioteca.models;

import java.util.Date;

public class prestamos {
    private int id_prestamo;
    private int id_usuario;
    private int id_libro;
    private Date fecha_prestamo;
    private Date fecha_limite_prestamo;
    private Date fecha_devolucion;

    public prestamos() {}

    public prestamos(int id_prestamo, int id_usuario, int id_libro, Date fecha_prestamo, Date fecha_limite_prestamo, Date fecha_devolucion){
        this.id_prestamo = id_prestamo;
        this.id_usuario = id_usuario;
        this.id_libro = id_libro;
        this.fecha_prestamo = fecha_prestamo;
        this.fecha_limite_prestamo = fecha_limite_prestamo;
        this.fecha_devolucion = fecha_devolucion;
    }

    public int getId_prestamo() {
        return id_prestamo;
    }

    public void setId_prestamo(int id_prestamo) {
        this.id_prestamo = id_prestamo;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public int getId_libro() {
        return id_libro;
    }

    public void setId_libro(int id_libro) {
        this.id_libro = id_libro;
    }

    public java.sql.Date getFecha_prestamo() {
        return (java.sql.Date) fecha_prestamo;
    }

    public void setFecha_prestamo(Date fecha_prestamo) {
        this.fecha_prestamo = fecha_prestamo;
    }

    public java.sql.Date getFecha_limite_prestamo() {
        return (java.sql.Date) fecha_limite_prestamo;
    }

    public void setFecha_limite_prestamo(Date fecha_limite_prestamo) {
        this.fecha_limite_prestamo = fecha_limite_prestamo;
    }

    public java.sql.Date getFecha_devolucion() {
        return (java.sql.Date) fecha_devolucion;
    }

    public void setFecha_devolucion(Date fecha_devolucion) {
        this.fecha_devolucion = fecha_devolucion;
    }
}
