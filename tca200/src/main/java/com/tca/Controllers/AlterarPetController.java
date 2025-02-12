package com.tca.Controllers;

import com.tca.DAO.PetDAO;
import com.tca.Models.Pet;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class AlterarPetController {

    @FXML
    private TextField nomeField;
    @FXML
    private TextField racaField;
    @FXML
    private TextField tipoField;
    @FXML
    private TextField idadeField;
    @FXML
    private Button btnMacho;
    @FXML
    private Button btnFemea;
    @FXML
    private Button salvarButton;
    @FXML
    private Button cancelarButton;

    private VerPetsController verPetsController;

    private Pet pet;
    private final PetDAO petDAO = new PetDAO();
    private String sexoSelecionado = null;

    @FXML
    public void initialize() {
        Platform.runLater(this::resetarBotoes);
    }


    public void setPet(Pet pet) {
        this.pet = pet;
        preencherCampos();
    }

    private void preencherCampos() {
        if (pet != null) {
            nomeField.setText(pet.getNome());
            racaField.setText(pet.getRaca());
            tipoField.setText(pet.getTipo());
            idadeField.setText(String.valueOf(pet.getIdade()));

            if ("Macho".equals(pet.getSexo())) {
                selecionarMacho();
            } else if ("Fêmea".equals(pet.getSexo())) {
                selecionarFemea();
            } else {
                resetarBotoes();
            }
        }
    }

    @FXML
    private void selecionarMacho() {
        sexoSelecionado = "Macho";
        atualizarBotoes();
    }

    @FXML
    private void selecionarFemea() {
        sexoSelecionado = "Fêmea";
        atualizarBotoes();
    }

    private void atualizarBotoes() {
        if ("Macho".equals(sexoSelecionado)) {
            btnMacho.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-weight: bold;");
            btnFemea.setStyle("-fx-background-color: #D3D3D3; -fx-text-fill: black; -fx-font-weight: bold;");
        } else if ("Fêmea".equals(sexoSelecionado)) {
            btnFemea.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-weight: bold;");
            btnMacho.setStyle("-fx-background-color: #D3D3D3; -fx-text-fill: black; -fx-font-weight: bold;");
        }
    }

    private void resetarBotoes() {
        btnMacho.setStyle("-fx-background-color: #D3D3D3; -fx-text-fill: black; -fx-font-weight: bold;");
        btnFemea.setStyle("-fx-background-color: #D3D3D3; -fx-text-fill: black; -fx-font-weight: bold;");
    }

    @FXML
    private void salvarAlteracoes() {
        if (pet != null) {
            pet.setNome(nomeField.getText());
            pet.setRaca(racaField.getText());
            pet.setTipo(tipoField.getText());
            pet.setIdade(Integer.parseInt(idadeField.getText()));
            pet.setSexo(sexoSelecionado);

            try {
                petDAO.atualizarPet(pet);

                // Atualiza a lista na tela de Ver Pets
                if (verPetsController != null) {
                    verPetsController.atualizarListaPets();
                }

                fecharJanela();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void cancelarAlteracao() {
        fecharJanela();
    }

    private void fecharJanela() {
        Stage stage = (Stage) salvarButton.getScene().getWindow();
        stage.close();
    }

    public void setVerPetsController(VerPetsController verPetsController) {
        this.verPetsController = verPetsController;
    }    
}