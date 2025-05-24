package org.example.gestor_biblioteca.DAOs;

import org.example.gestor_biblioteca.models.libros;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class librosDao extends GenericDAO<libros> {
    public librosDao(Connection connection) {
        super(connection);
    }

    @Override
    public boolean save(libros libro) {
        String sql = "INSERT INTO libros (titulo_libro, ano_publicacion, id_editorial, id_autor, id_categoria) VALUES (?,?,?,?,?)";

        try(PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            stmt.setString(1, libro.getTitulo_libro());
            stmt.setInt(2, libro.getAno_publicacion());
            stmt.setInt(3, libro.getId_editorial());
            stmt.setInt(4, libro.getId_autor());
            stmt.setInt(5, libro.getId_categoria());

            int rowsAffected = stmt.executeUpdate();
            if(rowsAffected > 0){
                return false;
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    libro.setId_libro(generatedKeys.getInt(1));
                } else {
                    return false;
                }
            }
            return true;
        } catch (SQLException e){
            return false;
        }

    }

    @Override
    public boolean update(libros libro) {
        String sql = "UPDATE libros SET titulo_libro = ?, ano_publicacion = ?, id_editorial = ?, id_autor = ? WHERE id_libro = ?";

        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1, libro.getTitulo_libro());
            stmt.setInt(2, libro.getAno_publicacion());
            stmt.setInt(3, libro.getId_editorial());
            stmt.setInt(4, libro.getId_autor());
            stmt.setInt(5, libro.getId_libro());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e){
            return false;
        }
    }

    @Override
    public boolean delete(int id_libro) {
        String sql = "DELETE FROM libros WHERE id_libro = ?";

        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, id_libro);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e){
            return false;
        }

    }

    @Override
    public libros findById(int id_libro) {
        String sql = "SELECT * FROM libros WHERE id_libro = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, id_libro);
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
    public List<libros> findAll() {
        String sql = "SELECT * FROM libros";
        List<libros> lib = new ArrayList<>();

        try(PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()){
                while (rs.next()){
                    lib.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e){
        }
        return lib;
    }

    @Override
    protected libros mapResultSetToEntity(ResultSet rs) throws SQLException {
        libros lib = new libros();
        lib.setId_libro(rs.getInt("id_libro"));
        lib.setTitulo_libro(rs.getString("titulo_libro"));
        lib.setAno_publicacion(rs.getInt("ano_publicacion"));
        lib.setId_editorial(rs.getInt("id_editorial"));
        lib.setId_autor(rs.getInt("id_autor"));
        lib.setId_categoria(rs.getInt("id_categoria"));
        return lib;
    }
}
