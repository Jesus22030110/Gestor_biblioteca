package org.example.gestor_biblioteca.models;

import java.sql.Date;

public class prestamos {
    private int id_prestamo;
    private int id_usuario;
    private int id_libro;
    private Date fecha_prestamo;
    private Date fecha_limite_prestamo;
    private Date fecha_devolucion;

    public Date getFecha_prestamo() {
        return fecha_prestamo;
    }

    public void setFecha_prestamo(Date fecha_prestamo) {
        this.fecha_prestamo = fecha_prestamo;
    }

    public Date getFecha_limite_prestamo() {
        return fecha_limite_prestamo;
    }

    public void setFecha_limite_prestamo(Date fecha_limite_prestamo) {
        this.fecha_limite_prestamo = fecha_limite_prestamo;
    }

    public Date getFecha_devolucion() {
        return fecha_devolucion;
    }

    public void setFecha_devolucion(Date fecha_devolucion) {
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
}