package org.example.gestor_biblioteca.DAOs;

import org.example.gestor_biblioteca.models.editoriales;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class editorialesDao extends GenericDAO<editoriales>{

    public editorialesDao(Connection connection) {
        super(connection);
    }

    @Override
    public boolean save(editoriales editorial) {
        String sql = "INSERT INTO editoriales (editorial) VALUES (?)";

        try(PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            stmt.setString(1, editorial.getEditorial());

            int rowsAffected = stmt.executeUpdate();
            if(rowsAffected > 0){
                return false;
            }

            try(ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if(generatedKeys.next()){
                    editorial.setId_editorial(generatedKeys.getInt(1));
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
    public boolean update(editoriales editorial) {
        String sql = "UPDATE editoriales SET editorial = ? WHERE id_editorial = ?";

        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1, editorial.getEditorial());
            stmt.setInt(2, editorial.getId_editorial());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e){
            return false;
        }

    }

    @Override
    public boolean delete(int id_editorial) {
        String sql = "DELETE FROM editoriales WHERE id_editorial = ?";

        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, id_editorial);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e){
            return false;
        }

    }

    @Override
    public editoriales findById(int id_editorial) {
        String sql = "SELECT * FROM editoriales WHERE id_editorial = ?";

        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, id_editorial);
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
    public List<editoriales> findAll() {
        String sql = "SELECT * FROM editoriales";
        List<editoriales> edit = new ArrayList<>();

        try(PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()){

            while (rs.next()){
                edit.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e){
        }
        return edit;
    }

    @Override
    protected editoriales mapResultSetToEntity(ResultSet rs) throws SQLException {
        editoriales edit = new editoriales();
        edit.setId_editorial(rs.getInt("id_editorial"));
        edit.setEditorial(rs.getString("editorial"));
        return edit;
    }

}
