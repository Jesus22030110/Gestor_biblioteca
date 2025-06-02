package org.example.gestor_biblioteca.models;

public class UsuarioFactory {
    public static Usuario crearUsuario(int rol) {
        return switch (rol) {
            case 2 -> new UsuarioAdmin();
            default -> new UsuarioNormal();
        };
    }
}