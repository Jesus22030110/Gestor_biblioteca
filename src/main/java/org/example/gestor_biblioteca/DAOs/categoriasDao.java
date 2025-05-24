package org.example.gestor_biblioteca.DAOs;

import org.example.gestor_biblioteca.models.categorias;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class categoriasDao extends GenericDAO<categorias> {

    public categoriasDao(Connection connection){
        super(connection);
    }

    @Override
    public boolean save(categorias cat){
        String sql  = "INSERT INTO categroias (categoria, descripcion) VALUES (?,?)";

        try(PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            stmt.setString(1, cat.getCategoria());
            stmt.setString(2, cat.getDescripcion());

            int affectedRows = stmt.executeUpdate();
            if(affectedRows > 0){
                return false;
            }
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if(generatedKeys.next()){
                    cat.setId_categoria(generatedKeys.getInt(1));
                } else{
                    return false;
                }
            }
            return  true;
        } catch (SQLException e){
            return false;
        }
    }

    @Override
    public boolean update(categorias cat){
        String sql = "UPDATE categorias SET categoria = ?, descripcion = ? WHERE id_categoria = ?";

        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1, cat.getCategoria());
            stmt.setString(2, cat.getDescripcion());
            stmt.setInt(3,cat.getId_categoria());

            return stmt.executeUpdate() > 0;
        } catch(SQLException e){
            return false;
        }
    }

    @Override
    public boolean delete(int id_categoria) {
        String sql = "DELETE FROM categorias WHERE id_categoria = ?";

        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, id_categoria);
            return stmt.executeUpdate() > 0;
        } catch(SQLException e){
            return false;
        }

    }

    @Override
    public categorias findById(int id_categoria) {
        String sql = "SELECT * FROM categorias WHERE id_categoria = ?";

        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, id_categoria);
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
    public List<categorias> findAll() {
        String sql = "SELECT * FROM categorias";
        List<categorias> cat = new ArrayList<>();

        try(PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()){

            while (rs.next()){
                cat.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e){
        }
        return cat;
    }

    @Override
    protected categorias mapResultSetToEntity(ResultSet rs) throws SQLException{
        categorias cat = new categorias();
        cat.setId_categoria(rs.getInt("id_categoria"));
        cat.setCategoria(rs.getString("categoria"));
        cat.setDescripcion(rs.getString("descripcion"));
        return cat;
    }
}
