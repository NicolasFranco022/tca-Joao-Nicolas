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
}
