package com.tca.Controllers;

import java.io.IOException;

import com.tca.DAO.PetDAO;
import com.tca.Models.Dono;
import com.tca.Models.Pet;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PetController {

    @FXML
    private TextField txtNomePet, txtRaca, txtTipo, txtIdade;

    @FXML
    private Button btnSalvar, btnCancelar, btnMacho, btnFemea;

    private PetDAO petDAO = new PetDAO();
    private Dono donoSelecionado;
    private String sexoSelecionado = null;

    public void setDonoSelecionado(Dono dono) {
        this.donoSelecionado = dono;
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

    @FXML
    private void salvarPet() {
        if (donoSelecionado == null) {
            mostrarAlerta("Erro", "Nenhum dono foi selecionado!");
            return;
        }

        String nome = txtNomePet.getText().trim();
        String raca = txtRaca.getText().trim();
        String tipo = txtTipo.getText().trim();
        String sexo = sexoSelecionado;
        if (sexo == null || sexo.isEmpty()) {
            mostrarAlerta("Erro", "Selecione o sexo do pet.");
            return;
        }
        int idade;

        try {
            idade = Integer.parseInt(txtIdade.getText().trim());
        } catch (NumberFormatException e) {
            mostrarAlerta("Erro", "Idade inválida! Digite um número.");
            return;
        }

        if (nome.isEmpty() || raca.isEmpty() || tipo.isEmpty() || sexo.isEmpty()) {
            mostrarAlerta("Erro", "Todos os campos devem ser preenchidos.");
            return;
        }

        Pet pet = new Pet(0, nome, raca, tipo, idade, sexo, donoSelecionado);

        try {
            petDAO.inserirPet(pet);
            mostrarAlerta("Sucesso", "Pet cadastrado com sucesso!");

            fecharJanela();
        } catch (Exception e) {
            mostrarAlerta("Erro", "Não foi possível salvar o pet.");
            e.printStackTrace();
        }
    }

    @FXML
    private void cancelar() {
        fecharJanela();
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void fecharJanela() {
        if (btnCancelar != null && btnCancelar.getScene() != null) {
            Stage stage = (Stage) btnCancelar.getScene().getWindow();
            stage.close();
        }
    }    
}