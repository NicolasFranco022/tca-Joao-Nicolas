package com.tca.Controllers;

import com.tca.DAO.DonoDAO;
import com.tca.Models.Dono;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.SQLException;

public class AlterarDonoController {

    @FXML
    private Label lblTitulo;
    
    @FXML
    private ComboBox<String> cmbOpcao;
    
    @FXML
    private TextField txtNovoValor;
    
    @FXML
    private Button btnSalvar, btnCancelar;

    private Dono dono;
    private DonoDAO donoDAO;

    public AlterarDonoController() {
        this.donoDAO = new DonoDAO();
    }

    @FXML
    public void initialize() {
        cmbOpcao.getItems().addAll("Nome", "Telefone", "E-mail");
        cmbOpcao.setOnAction(event -> txtNovoValor.setPromptText("Novo " + cmbOpcao.getValue().toLowerCase()));
    }

    public void setDono(Dono dono) {
        this.dono = dono;
        lblTitulo.setText("Alterar Dados de " + dono.getNome());
    }

    @FXML
    private void salvarAlteracao() {
        if (cmbOpcao.getValue() == null || txtNovoValor.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Preencha todos os campos!", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        String novoValor = txtNovoValor.getText();
        try {
            switch (cmbOpcao.getValue()) {
                case "Nome":
                    dono.setNome(novoValor);
                    break;
                case "Telefone":
                    dono.setTelefone(novoValor);
                    break;
                case "E-mail":
                    dono.setEmail(novoValor);
                    break;
            }
            
            donoDAO.atualizarDono(dono);
            
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "Alteração realizada com sucesso!", ButtonType.OK);
            successAlert.showAndWait();
            
            fecharTela();
        } catch (SQLException e) {
            e.printStackTrace();
            Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Erro ao alterar dono!", ButtonType.OK);
            errorAlert.showAndWait();
        }
    }

    @FXML
    private void fecharTela() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }
}
