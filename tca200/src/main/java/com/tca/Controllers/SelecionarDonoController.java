package com.tca.Controllers;

import com.tca.DAO.DonoDAO;
import com.tca.Models.Dono;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class SelecionarDonoController {

    @FXML
    private ComboBox<Dono> comboDonos;

    @FXML
    private Button btnProximo;

    private DonoDAO donoDAO = new DonoDAO();
    private Dono donoSelecionado;

    @FXML
    public void initialize() {
        carregarDonos();
    }

    private void carregarDonos() {
        try {
            List<Dono> donos = donoDAO.buscarTodosDonos();

            if (donos.isEmpty()) {
                mostrarAlerta("Nenhum dono cadastrado!", "Cadastre um dono antes de criar um pet.");
                fecharJanela();
            } else {
                ObservableList<Dono> donosObservable = FXCollections.observableArrayList(donos);
                comboDonos.setItems(donosObservable);
                
                // Atualizar donoSelecionado quando um dono for escolhido
                comboDonos.setOnAction(event -> donoSelecionado = comboDonos.getSelectionModel().getSelectedItem());
            }
        } catch (SQLException e) {
            mostrarAlerta("Erro no banco de dados", "Não foi possível carregar os donos.");
            e.printStackTrace();
        }
    }

    @FXML
    private void proximo() {
        if (donoSelecionado == null) {
            mostrarAlerta("Seleção obrigatória", "Selecione um dono antes de prosseguir.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tca/Views/TelaCadastrarPet.fxml"));
            Parent root = loader.load();

            PetController petController = loader.getController();
            petController.setDonoSelecionado(donoSelecionado);

            Stage stage = (Stage) btnProximo.getScene().getWindow();
            stage.setScene(new Scene(root, 600, 860));
            stage.centerOnScreen();
            stage.setTitle("Cadastrar Novo Pet");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Não foi possível abrir a tela de cadastro de pet.");
        }
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void fecharJanela() {
        Stage stage = (Stage) comboDonos.getScene().getWindow();
        stage.close();
    }
}