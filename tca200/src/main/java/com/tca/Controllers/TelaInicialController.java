package com.tca.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class TelaInicialController {

    @FXML
    private Button btnCriarAgendamento;

    @FXML
    private Button btnVerAgendamentos;

    @FXML
    private Button btnCriarDono;

    @FXML
    private Button btnCriarPet;

    @FXML
    private void CadastrarAgendamentos() {
        System.out.println("Clicou no botão Criar Agendamento");

        try {   
            Parent root = FXMLLoader.load(getClass().getResource("/com/tca/Views/TelaCadastrarAgendamento.fxml"));
            Stage stage = (Stage) btnCriarAgendamento.getScene().getWindow();
            stage.setScene(new Scene(root, 600, 860));
            stage.centerOnScreen();
            stage.setTitle("Cadastrar Novo Agendamento");
            stage.show();      
        } catch (Exception e) {
            System.out.println("Erro ao trocar para a tela de agendamento: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void VerAgendamentos() {
        System.out.println("Clicou no botão Ver Agendamentos");

        try {   
            Parent root = FXMLLoader.load(getClass().getResource("/com/tca/Views/TelaVerAgendamentos.fxml"));
            Stage stage = (Stage) btnVerAgendamentos.getScene().getWindow();
            stage.setScene(new Scene(root, 600, 860));
            stage.centerOnScreen();
            stage.setTitle("Ver Consultas");
            stage.show();     
        } catch (Exception e) {
            System.out.println("Erro ao trocar para a tela de agendamento: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void CadastrarDono() {
        System.out.println("Clicou no botão Criar Dono");

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tca/Views/TelaCadastrarDono.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root, 400, 300));
            stage.setTitle("Cadastrar Novo Dono");
            stage.show();
        } catch (Exception e) {
            System.out.println("Erro ao abrir a tela de cadastro de dono: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void CadastrarPet() {
        System.out.println("Clicou no botão Criar Pet");

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tca/Views/TelaSelecionarDono.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root, 400, 300));
            stage.setTitle("Selecionar Dono");
            stage.show();
        } catch (Exception e) {
            System.out.println("Erro ao abrir a tela de selecionar dono: " + e.getMessage());
            e.printStackTrace();
        }
    }
}