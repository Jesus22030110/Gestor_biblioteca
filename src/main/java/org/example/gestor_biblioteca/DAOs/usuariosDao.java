package org.example.gestor_biblioteca.DAOs;

import org.example.gestor_biblioteca.models.usuarios;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class usuariosDao extends GenericDAO<usuarios> {
    public usuariosDao(Connection connection) {
        super(connection);
    }

    @Override
    public boolean save(usuarios usuario) {
        String sql = "INSERT INTO usuarios (nombre, primer_apellido, segundo_apellido, email, telefono, rol, contrasena) VALUES (?,?,?,?,?,?,?)";

        try(PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getPrimer_apellido());
            stmt.setString(3, usuario.getSegundo_apellido());
            stmt.setString(4, usuario.getEmail());
            stmt.setString(5, usuario.getTelefono());
            stmt.setInt(6, usuario.getRol());
            stmt.setString(7, usuario.getContrasena());

            int affectedRows = stmt.executeUpdate();
            if(affectedRows > 0){
                return false;
            }
            try(ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if(generatedKeys.next()){
                    return  false;
                }
            }
            return true;
        } catch (SQLException e){
            return false;
        }
    }

    @Override
    public boolean update(usuarios usuario) {
        String sql = "UPDATE usuarios SET nombre = ?, primer_apellido = ?, segundo_apellido = ?, email = ?, telefono = ? WHERE rol = ?, contrasena = ?";

        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getPrimer_apellido());
            stmt.setString(3, usuario.getSegundo_apellido());
            stmt.setString(4, usuario.getEmail());
            stmt.setString(5, usuario.getTelefono());
            stmt.setInt(6, usuario.getRol());
            stmt.setString(7, usuario.getContrasena());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e){
            return false;
        }

    }

    @Override
    public boolean delete(int id_usuario) {
        String sql = "DELETE FROM usuarios WHERE id_usuario = ?";

        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, id_usuario);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e){
            return false;
        }
    }

    @Override
    public usuarios findById(int id_usuario) {
        String sql = "SELECT * FROM usuarios WHERE id_usuario = ?";

        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, id_usuario);
            try(ResultSet rs = stmt.executeQuery()){
                if(rs.next()){
                    return mapResultSetToEntity(rs);
                }
            }
        } catch (SQLException e){
        }
        return null;
    }

    @Override
    public List<usuarios> findAll() {
        String sql = "SELECT * FROM usuarios";
        List<usuarios> us = new ArrayList<>();

        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                us.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e){
        }

        return us;
    }

    @Override
    protected usuarios mapResultSetToEntity(ResultSet rs) throws SQLException {
        usuarios us = new usuarios();
        us.setId_usuario(rs.getInt("id_usuario"));
        us.setNombre(rs.getString("nombre"));
        us.setPrimer_apellido(rs.getString("primer_apellido"));
        us.setSegundo_apellido(rs.getString("segundo_apellido"));
        us.setEmail(rs.getString("email"));
        us.setTelefono(rs.getString("telefono"));
        us.setRol(rs.getInt("rol"));
        us.setContrasena(rs.getString("contrasena"));
        return us;
    }
}
