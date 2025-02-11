package com.tca.Controllers;

import com.tca.DAO.PetDAO;
import com.tca.Models.Pet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class SelecionarPetController {

    @FXML
    private ListView<Pet> listViewPets;

    @FXML
    private Button btnVoltar;

    private PetDAO petDAO = new PetDAO();
    private Pet petSelecionado;

    @FXML
    public void initialize() {
        carregarPets();
    }

    private void carregarPets() {
        List<Pet> pets = petDAO.buscarTodosPets();
        if (pets.isEmpty()) {
            mostrarAlerta("Nenhum pet cadastrado!", "Cadastre um pet antes de continuar.");
            voltarTelaInicial(null); // Retorna para a tela inicial automaticamente
        } else {
            ObservableList<Pet> observablePets = FXCollections.observableArrayList(pets);
            listViewPets.setItems(observablePets);
        }
    }

    @FXML
    private void voltarTelaInicial(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tca/Views/TelaInicial.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Tela Inicial");
            stage.show();

            fecharJanelaAtual(event); // Fecha a janela atual ao abrir a tela inicial

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Erro ao voltar", "Não foi possível carregar a tela inicial.");
        }
    }

    @FXML
    private void selecionarPet() {
        petSelecionado = listViewPets.getSelectionModel().getSelectedItem();
        if (petSelecionado != null) {
            abrirTelaCadastroAgendamento();
        } else {
            mostrarAlerta("Seleção obrigatória", "Por favor, selecione um pet antes de continuar.");
        }
    }

    private void abrirTelaCadastroAgendamento() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tca/Views/TelaCadastrarAgendamento.fxml"));
            Parent root = loader.load();

            AgendamentoController agendamentoController = loader.getController();
            agendamentoController.setPetSelecionado(petSelecionado);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            fecharJanelaAtual(null); // Fecha a tela de seleção de pets

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fecharJanelaAtual(ActionEvent event) {
        Stage stage;
        if (event != null) {
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        } else {
            stage = (Stage) listViewPets.getScene().getWindow();
        }
        stage.close();
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
