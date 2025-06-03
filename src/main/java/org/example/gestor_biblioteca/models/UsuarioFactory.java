package org.example.gestor_biblioteca.models;

public class UsuarioFactory {
    public static Usuario crearUsuario(usuarios usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("El objeto usuario no puede ser null.");
        }

        return switch (usuario.getRol()) {
            case 2 -> new UsuarioAdmin();
            default -> new UsuarioNormal(usuario);
        };
    }
}