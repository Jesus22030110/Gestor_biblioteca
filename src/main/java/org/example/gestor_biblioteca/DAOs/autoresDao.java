package org.example.gestor_biblioteca.DAOs;


import org.example.gestor_biblioteca.models.autores;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class autoresDao extends GenericDAO<autores> {

public autoresDao(Connection connection) {
    super(connection);
}

    @Override
    public boolean save(autores autores) {
        String sql = "INSERT INTO autores (nombre, primer_apellido, segundo_apellido) VALUES (?,?,?)";

        try(PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            stmt.setString(1, autores.getNombre());
            stmt.setString(2, autores.getPrimer_apellido());
            stmt.setString(3, autores.getSegundo_apellido());

            int affectedRows = stmt.executeUpdate();

            if(affectedRows == 0){
                return false;
            }

            try(ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if(generatedKeys.next()){
                    autores.setId_autor(generatedKeys.getInt(1));

                } else{
                    return false;
                }
            }

            return true;
        } catch (SQLException e){
            return false;
        }
    }

    @Override
    public boolean update(autores autor){
        String sql = "UPDATE autores SET nombre = ?, primer_apellido = ?, segundo_apellido = ? WHERE id_autor = ?";

        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1, autor.getNombre());
            stmt.setString(2, autor.getPrimer_apellido());
            stmt.setString(3, autor.getSegundo_apellido());
            stmt.setInt(4, autor.getId_autor());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e){
            return false;
        }
    }

    @Override
    public boolean delete(int id_autor) {
        String sql = "DELETE FROM autores WHERE id_autor = ?";

        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, id_autor);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e){
            return false;
        }
    }

    @Override
    public autores findById(int id) {
        String sql = "SELECT * FROM autores WHERE id_autores = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, id);

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
    public List<autores> findAll(){
        String sql = "SELECT * FROM autores";
        List<autores> autor = new ArrayList<>();

        try(PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()){

            while (rs.next()){
                autor.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e){

        }
        return autor;
    }

    @Override
    protected autores mapResultSetToEntity(ResultSet rs) throws SQLException {
        autores autor = new autores();
        autor.setId_autor(rs.getInt("id_autor"));
        autor.setNombre(rs.getString("nombre"));
        autor.setPrimer_apellido(rs.getString("primer_apellido"));
        autor.setSegundo_apellido(rs.getString("segundo_apellido"));
        return autor;
    }

}
