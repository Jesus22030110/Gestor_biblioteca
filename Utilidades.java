import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** Clase con métodos útiles de propósito general */
public class Utilidades {

    /** Metodo para encriptar texto usando SHA-1 */
    public static String encriptarSHA1(String texto) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] mensajeDigest = md.digest(texto.getBytes());
            BigInteger no = new BigInteger(1, mensajeDigest);
            String hashTexto = no.toString(16);
            while (hashTexto.length() < 40) {
                hashTexto = "0" + hashTexto;
            }
            return hashTexto;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /** Muestra una alerta en pantalla (JavaFX) */
    public static void mostrarAlerta(String titulo, String mensaje, AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}

/** Repositorio genérico preparado para base de datos */
public abstract class Repositorio<T> {

    protected Connection conexion;

    public Repositorio(Connection conexion) {
        this.conexion = conexion;
    }

    /** Metodo para convertir ResultSet a objeto T, debe implementar la subclase */
    protected abstract T mapearObjeto(ResultSet rs) throws SQLException;

    /** Metodo para preparar statement de inserción según T */
    protected abstract void prepararInsert(PreparedStatement ps, T elemento) throws SQLException;

    /** Metodo para obtener la consulta SQL para obtener todos los registros */
    protected abstract String consultaObtenerTodos();

    /** Metodo para obtener la consulta SQL para insertar registro */
    protected abstract String consultaInsertar();

    /** Metodo para obtener la consulta SQL para eliminar registro */
    protected abstract String consultaEliminar();

    /** Metodo para obtener la consulta SQL para limpiar tabla */
    protected abstract String consultaLimpiar();

    /** Metodo para obtener la consulta SQL para contar registros */
    protected abstract String consultaContar();

    public void agregar(T elemento) throws SQLException {
        try (PreparedStatement ps = conexion.prepareStatement(consultaInsertar())) {
            prepararInsert(ps, elemento);
            ps.executeUpdate();
        }
    }

    public void eliminar(T elemento) throws SQLException {
        try (PreparedStatement ps = conexion.prepareStatement(consultaEliminar())) {
            prepararEliminar(ps, elemento);
            ps.executeUpdate();
        }
    }

    /** Metodo para preparar statement de eliminación  */
    protected abstract void prepararEliminar(PreparedStatement ps, T elemento) throws SQLException;

    public List<T> obtenerTodos() throws SQLException {
        List<T> resultados = new ArrayList<>();
        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(consultaObtenerTodos())) {
            while (rs.next()) {
                resultados.add(mapearObjeto(rs));
            }
        }
        return resultados;
    }

    public void limpiar() throws SQLException {
        try (Statement stmt = conexion.createStatement()) {
            stmt.executeUpdate(consultaLimpiar());
        }
    }

    public int contar() throws SQLException {
        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(consultaContar())) {
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                return 0;
            }
        }
    }
}
