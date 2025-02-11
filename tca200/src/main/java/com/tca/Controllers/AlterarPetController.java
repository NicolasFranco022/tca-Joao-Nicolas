package com.tca.Controllers;

import com.tca.DAO.PetDAO;
import com.tca.Models.Pet;
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
    private TextField sexoField;
    @FXML
    private Button salvarButton;
    @FXML
    private Button cancelarButton;

    private Pet pet;
    private final PetDAO petDAO = new PetDAO();

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
            sexoField.setText(pet.getSexo());
        }
    }

    @FXML
    private void salvarAlteracoes() {
        if (pet != null) {
            pet.setNome(nomeField.getText());
            pet.setRaca(racaField.getText());
            pet.setTipo(tipoField.getText());
            pet.setIdade(Integer.parseInt(idadeField.getText()));
            pet.setSexo(sexoField.getText());

            try {
                petDAO.atualizarPet(pet);
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
}