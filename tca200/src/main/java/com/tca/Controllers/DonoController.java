package com.tca.Controllers;

import com.tca.DAO.DonoDAO;
import com.tca.Models.Dono;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DonoController {

    @FXML
    private TextField txtNome;

    @FXML
    private TextField txtTelefone;

    @FXML
    private TextField txtEmail;

    @FXML
    private Button btnSalvar;

    @FXML
    private Button btnCancelar;

    private final DonoDAO donoDAO = new DonoDAO();

    @FXML
    public void initialize() {
        txtTelefone.textProperty().addListener((observable, oldValue, newValue) -> {
            String texto = newValue.replaceAll("[^0-9]", "");
            if (texto.length() > 11) {
                texto = texto.substring(0, 11);
            }
            if (texto.length() > 6) {
                texto = "(" + texto.substring(0, 2) + ") " + texto.substring(2, 7) + "-" + texto.substring(7);
            } else if (texto.length() > 2) {
                texto = "(" + texto.substring(0, 2) + ") " + texto.substring(2);
            }
            txtTelefone.setText(texto);
        });
    }

    @FXML
    private void salvarDono() {
        String nome = txtNome.getText();
        String telefone = txtTelefone.getText();
        String email = txtEmail.getText();

        if (nome.isEmpty() || telefone.isEmpty() || email.isEmpty()) {
            System.out.println("Todos os campos devem ser preenchidos!");
            return;
        }

        Dono novoDono = new Dono();
        novoDono.setNome(nome);
        novoDono.setTelefone(telefone);
        novoDono.setEmail(email);

        try {
            donoDAO.inserirDono(novoDono);
            mostrarMensagemSucesso("Dono Cadastrado com sucesso!");
            fecharJanela();
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar dono: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void mostrarMensagemSucesso(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    @FXML
    private void cancelar() {
        fecharJanela();
    }

    private void fecharJanela() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }
}