package com.tca.Controllers;

import com.tca.Models.Agendamento;
import com.tca.DAO.AgendamentoDAO;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.SQLException;

public class AlterarAgendamentosController {

    @FXML
    private TextField motivoConsultaField;

    @FXML
    private DatePicker dataAgendamentoPicker;

    @FXML
    private Button salvarButton;
    @FXML
    private Button cancelarButton;

    @FXML
    private CheckBox alterarMotivoCheckBox;
    @FXML
    private CheckBox alterarDataCheckBox;

    private Agendamento agendamento;
    private final AgendamentoDAO agendamentoDAO = new AgendamentoDAO();

    public void setAgendamento(Agendamento agendamento) {
        this.agendamento = agendamento;
        motivoConsultaField.setText(agendamento.getMotivoConsulta());
        dataAgendamentoPicker.setValue(agendamento.getDataAgendamento());

        toggleDataField();
        toggleMotivoField();
    }

    @FXML
    public void cancelar() {
        fecharJanela();
    }

    private void fecharJanela() {
        Stage stage = (Stage) salvarButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void toggleDataField() {
        dataAgendamentoPicker.setDisable(!alterarDataCheckBox.isSelected());
    }

    @FXML
    public void toggleMotivoField() {
        motivoConsultaField.setEditable(alterarMotivoCheckBox.isSelected());
    }

    @FXML
    public void salvar() {
        if (agendamento != null) {
            if (alterarMotivoCheckBox.isSelected()) {
                agendamento.setMotivoConsulta(motivoConsultaField.getText());
            }

            if (alterarDataCheckBox.isSelected()) {
                agendamento.setDataAgendamento(dataAgendamentoPicker.getValue());

                if (!agendamentoDAO.isDataDisponivel(agendamento.getDataAgendamento())) {
                    exibirMensagemErro("Não é possível alterar! Já existe um agendamento para esta data.");
                    return;
                }
            }

            try {
                agendamentoDAO.alterarAgendamento(agendamento);
                exibirMensagemSucesso("Agendamento alterado com sucesso!");
                fecharJanela();
            } catch (SQLException e) {
                exibirMensagemErro("Erro ao atualizar agendamento: " + e.getMessage());
            }
        }
    }

    private void exibirMensagemErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void exibirMensagemSucesso(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}