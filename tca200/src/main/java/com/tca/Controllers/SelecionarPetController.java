package com.tca.Controllers;

import com.tca.DAO.PetDAO;
import com.tca.Models.Pet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class SelecionarPetController {

    @FXML
    private ListView<Pet> listViewPets;

    private PetDAO petDAO = new PetDAO();
    private Pet petSelecionado;

    @FXML
    public void initialize() {
        carregarPets();
    }

    private void carregarPets() {
        List<Pet> pets = petDAO.buscarTodosPets();
        ObservableList<Pet> observablePets = FXCollections.observableArrayList(pets);
        listViewPets.setItems(observablePets);
    }

    @FXML
    private void selecionarPet() {
        petSelecionado = listViewPets.getSelectionModel().getSelectedItem();
        if (petSelecionado != null) {
            abrirTelaCadastroAgendamento();
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

            fecharJanelaAtual();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fecharJanelaAtual() {
        Stage stage = (Stage) listViewPets.getScene().getWindow();
        stage.close();
    }
}