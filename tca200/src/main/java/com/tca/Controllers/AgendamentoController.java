package com.tca.Controllers;

import com.tca.DAO.AgendamentoDAO;
import com.tca.DAO.DonoDAO;
import com.tca.DAO.PetDAO;
import com.tca.Models.Agendamento;
import com.tca.Models.Dono;
import com.tca.Models.Pet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AgendamentoController {

    @FXML
    private TextField nomeDonoField;
    @FXML
    private TextField telefoneDonoField;
    @FXML
    private TextField emailDonoField;

    @FXML
    private TextField nomePetField;
    @FXML
    private TextField racaPetField;
    @FXML
    private TextField tipoPetField;
    @FXML
    private TextField idadePetField;

    @FXML
    private DatePicker dataAgendamentoPicker;
    @FXML
    private TextField motivoAgendamentoField;

    @FXML
    private Button machoButton;
    @FXML
    private Button femeaButton;

    private String sexoPet;

    private final DonoDAO donoDAO = new DonoDAO();
    private final PetDAO petDAO = new PetDAO();
    private final AgendamentoDAO agendamentoDAO = new AgendamentoDAO();

    @FXML
    public void initialize() {
        telefoneDonoField.textProperty().addListener((observable, oldValue, newValue) -> {
            String texto = newValue.replaceAll("[^0-9]", "");
            if (texto.length() > 11) {
                texto = texto.substring(0, 11);
            }
            if (texto.length() > 6) {
                texto = "(" + texto.substring(0, 2) + ") " + texto.substring(2, 7) + "-" + texto.substring(7);
            } else if (texto.length() > 2) {
                texto = "(" + texto.substring(0, 2) + ") " + texto.substring(2);
            }
            telefoneDonoField.setText(texto);
        });
    }
    
        @FXML
    private void salvarAgendamento() {
        try {
            if (!validarCampos()) {
                mostrarMensagemErro("Preencha todos os campos obrigatórios.");
                return;
            }

            Dono dono = new Dono();
            dono.setNome(nomeDonoField.getText());
            dono.setTelefone(telefoneDonoField.getText());
            dono.setEmail(emailDonoField.getText());

            Dono donoExistente = donoDAO.buscarDonoPorTelefoneOuEmail(dono.getTelefone(), dono.getEmail());
            if (donoExistente != null) {
                dono.setId(donoExistente.getId());
            } else {
                donoDAO.inserirDono(dono);
            }

            Pet pet = new Pet();
            pet.setNome(nomePetField.getText());
            pet.setRaca(racaPetField.getText());
            pet.setTipo(tipoPetField.getText());
            pet.setIdade(Integer.parseInt(idadePetField.getText()));
            pet.setSexo(sexoPet);
            pet.setDono(dono);

            Pet petExistente = petDAO.buscarPetPorNomeEDono(pet.getNome(), dono.getId());
            if (petExistente != null) {
                pet.setId(petExistente.getId());
            } else {
                petDAO.inserirPet(pet);
            }

            Agendamento agendamento = new Agendamento();
            agendamento.setPet(pet);
            agendamento.setDono(dono);
            agendamento.setDataAgendamento(dataAgendamentoPicker.getValue());
            agendamento.setMotivoConsulta(motivoAgendamentoField.getText());

           
            if (!agendamentoDAO.isDataDisponivel(agendamento.getDataAgendamento())) {
                mostrarMensagemErro("Já existe um agendamento para esta data!");
                return;
            }
            
            agendamentoDAO.inserirAgendamento(agendamento);

            mostrarMensagemSucesso("Agendamento salvo com sucesso!");

        } catch (SQLException e) {
            mostrarMensagemErro("Erro ao salvar agendamento: " + e.getMessage());
            e.printStackTrace();
        } catch (NumberFormatException e) {
            mostrarMensagemErro("A idade do pet deve ser um número válido.");
        }
    }

        @FXML
    private void cancelarAgendamento() {
        nomeDonoField.clear();
        telefoneDonoField.clear();
        emailDonoField.clear();
        nomePetField.clear();
        racaPetField.clear();
        tipoPetField.clear();
        idadePetField.clear();
        dataAgendamentoPicker.setValue(null);
        motivoAgendamentoField.clear();
        machoButton.setStyle("");
        femeaButton.setStyle("");

        voltarParaTelaInicial();
    }

    private boolean validarCampos() {
        return !nomeDonoField.getText().isEmpty() && !telefoneDonoField.getText().isEmpty()
                && !emailDonoField.getText().isEmpty() && !nomePetField.getText().isEmpty()
                && !racaPetField.getText().isEmpty() && !idadePetField.getText().isEmpty()
                && dataAgendamentoPicker.getValue() != null && !motivoAgendamentoField.getText().isEmpty()
                && sexoPet != null;
    }

    @FXML
    private void selecionarSexo(ActionEvent event) {
        sexoPet = ((Button) event.getSource()).getText();
        machoButton.setStyle("");
        femeaButton.setStyle("");
        ((Button) event.getSource()).setStyle("-fx-background-color: lightgreen;");
    }

    private void mostrarMensagemErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void mostrarMensagemSucesso(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
        voltarParaTelaInicial();
    }

    private void voltarParaTelaInicial() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tca/views/TelaInicial.fxml"));
            Stage stage = (Stage) nomeDonoField.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Tela Inicial");
        } catch (IOException e) {
            Logger.getLogger(AgendamentoController.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
