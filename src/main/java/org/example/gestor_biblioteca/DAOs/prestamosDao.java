package org.example.gestor_biblioteca.DAOs;

import org.example.gestor_biblioteca.models.prestamos;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class prestamosDao extends GenericDAO<prestamos> {
    public prestamosDao(Connection connection) {
        super(connection);
    }

    @Override
    public boolean save(prestamos prestamo) {
        String sql = "INSERT INTO prestamos (id_usuario, id_libro, fecha_prestamo, fecha_limite_prestamo, fecha_devolucion) VALUES (?,?,?,?,?)";

        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, prestamo.getId_usuario());
            stmt.setInt(2, prestamo.getId_libro());
            stmt.setDate(3, prestamo.getFecha_prestamo());
            stmt.setDate(4, prestamo.getFecha_limite_prestamo());
            stmt.setDate(5, prestamo.getFecha_devolucion());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                return false;
            }

            try(ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return false;
                }
            }
            return true;
        } catch (SQLException e){
            return false;
        }
    }

    @Override
    public boolean update(prestamos prestamo) {
        String sql = "UPDATE perstamos SET id_usuario = ?, id_libro = ?, fecha_prestamo = ?, fecha_limite_prestamo = ?, fecha_devolucion = ? WHERE id_usuario = ?";

        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, prestamo.getId_usuario());
            stmt.setInt(2, prestamo.getId_libro());
            stmt.setDate(3, prestamo.getFecha_prestamo());
            stmt.setDate(4, prestamo.getFecha_limite_prestamo());
            stmt.setDate(5, prestamo.getFecha_devolucion());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e){
            return false;
        }
    }

    @Override
    public boolean delete(int id_prestamo) {
        String sql = "DELETE FROM prestamos WHERE id_prestamo = ?";

        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, id_prestamo);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e){
            return false;
        }

    }

    @Override
    public prestamos findById(int id_prestamo) {
        String sql = "SELECT * FROM prestamos WHERE id_prestamo = ?";

        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, id_prestamo);
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
    public List<prestamos> findAll() {
        String sql = "SELECT * FROM prestamos";
        List<prestamos> prest = new ArrayList<>();

        try(PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery()){
            while(rs.next()){
                prest.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
        }
        return prest;
    }

    @Override
    protected prestamos mapResultSetToEntity(ResultSet rs) throws SQLException {
        prestamos prest = new prestamos();
        prest.setId_prestamo(rs.getInt("id_prestamo"));
        prest.setId_usuario(rs.getInt("id_usuario"));
        prest.setId_libro(rs.getInt("id_libro"));
        prest.setFecha_prestamo(rs.getDate("fecha_prestamo"));
        prest.setFecha_limite_prestamo(rs.getDate("fecha_limite_devolucion"));
        prest.setFecha_devolucion(rs.getDate("fecha_devolucion"));
        return prest;
    }


}