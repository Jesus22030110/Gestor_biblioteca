package org.example.gestor_biblioteca.controllers;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utilidades {
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

    public static void mostrarAlerta(String titulo, String mensaje, AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}