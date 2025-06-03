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
        String sql = "INSERT INTO libros (titulo_libro, ano_publicacion, id_editorial, id_autor, id_categoria) VALUES (?, ?, ?, ?, ?)";

        System.out.println("Intentando guardar libro: " + libro.getTitulo_libro());
        System.out.println("Año: " + libro.getAno_publicacion());
        System.out.println("ID Editorial: " + libro.getId_editorial());
        System.out.println("ID Autor: " + libro.getId_autor());
        System.out.println("ID Categoría: " + libro.getId_categoria());

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, libro.getTitulo_libro());
            stmt.setInt(2, libro.getAno_publicacion());
            stmt.setInt(3, libro.getId_editorial());
            stmt.setInt(4, libro.getId_autor());
            stmt.setInt(5, libro.getId_categoria());

            int rowsAffected = stmt.executeUpdate();
            System.out.println("Filas afectadas: " + rowsAffected);

            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int idGenerado = generatedKeys.getInt(1);
                        libro.setId_libro(idGenerado);
                        System.out.println("ID generado: " + idGenerado);
                        return true;
                    } else {
                        System.out.println("No se obtuvo ID generado");
                    }
                }
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Error al guardar libro: " + e.getMessage());
            e.printStackTrace();
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
        List<libros> listaLibros = new ArrayList<>();
        System.out.println("Buscando todos los libros...");

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                libros libro = mapResultSetToEntity(rs);
                listaLibros.add(libro);
                System.out.println("Libro encontrado: " + libro.getTitulo_libro());
            }
            System.out.println("Total libros encontrados: " + listaLibros.size());
        } catch (SQLException e) {
            System.err.println("Error al obtener libros: " + e.getMessage());
            e.printStackTrace();
        }
        return listaLibros;
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
