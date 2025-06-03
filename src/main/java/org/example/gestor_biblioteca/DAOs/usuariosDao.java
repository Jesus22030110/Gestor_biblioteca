package org.example.gestor_biblioteca.DAOs;

import org.example.gestor_biblioteca.models.usuarios;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class usuariosDao extends GenericDAO<usuarios> {
    public usuariosDao(Connection connection) {
        super(connection);
    }

    public boolean save(usuarios usuario) {
        String sql = "INSERT INTO usuarios (nombre, primer_apellido, segundo_apellido, email, telefono, rol, contrasena) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getPrimer_apellido());
            stmt.setString(3, usuario.getSegundo_apellido());
            stmt.setString(4, usuario.getEmail());
            stmt.setString(5, usuario.getTelefono());
            stmt.setInt(6, usuario.getRol());
            stmt.setString(7, usuario.getContrasena());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        usuario.setId_usuario(generatedKeys.getInt(1));
                        return true;
                    }
                }
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Error al guardar usuario: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(usuarios usuario) {
        // Actualizar solo campos permitidos: nombre, email, teléfono
        String sql = "UPDATE usuarios SET nombre = ?, email = ?, telefono = ? WHERE id_usuario = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getTelefono());
            stmt.setInt(4, usuario.getId_usuario());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int id_usuario) {
        String sql = "DELETE FROM usuarios WHERE id_usuario = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id_usuario);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public usuarios findById(int id_usuario) {
        String sql = "SELECT * FROM usuarios WHERE id_usuario = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id_usuario);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEntity(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public usuarios findByEmailOrUsuario(String identificador, String passwordHash) {
        String sql = "SELECT * FROM usuarios WHERE (email = ? OR nombre = ?) AND contrasena = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, identificador);
            stmt.setString(2, identificador);
            stmt.setString(3, passwordHash);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEntity(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<usuarios> findAll() {
        String sql = "SELECT * FROM usuarios";
        List<usuarios> listaUsuarios = new ArrayList<>();

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                listaUsuarios.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaUsuarios;
    }

    @Override
    protected usuarios mapResultSetToEntity(ResultSet rs) throws SQLException {
        usuarios usuario = new usuarios();
        usuario.setId_usuario(rs.getInt("id_usuario"));
        usuario.setNombre(rs.getString("nombre"));
        usuario.setPrimer_apellido(rs.getString("primer_apellido"));
        usuario.setSegundo_apellido(rs.getString("segundo_apellido"));
        usuario.setEmail(rs.getString("email"));
        usuario.setTelefono(rs.getString("telefono"));
        usuario.setRol(rs.getInt("rol"));
        usuario.setContrasena(rs.getString("contrasena"));
        return usuario;
    }
}