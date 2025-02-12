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
        cmbOpcao.setOnAction(event -> {
            txtNovoValor.setPromptText("Novo " + cmbOpcao.getValue().toLowerCase());
            txtNovoValor.clear(); // Limpa o campo ao trocar a opção
            txtNovoValor.textProperty().removeListener((obs, oldText, newText) -> {}); // Remove máscara anterior
            
            if ("Telefone".equals(cmbOpcao.getValue())) {
                aplicarMascaraTelefone();
            }
        });
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

    /**
     * Aplica máscara ao campo de telefone para formatar como (XX) XXXXX-XXXX
     */
    private void aplicarMascaraTelefone() {
        txtNovoValor.textProperty().addListener((obs, oldText, newText) -> {
            String numeros = newText.replaceAll("[^0-9]", ""); // Remove tudo que não for número
            
            if (numeros.length() > 11) {
                numeros = numeros.substring(0, 11); // Limita a 11 caracteres
            }

            String telefoneFormatado = formatarTelefone(numeros);
            txtNovoValor.setText(telefoneFormatado);
            txtNovoValor.positionCaret(telefoneFormatado.length()); // Mantém o cursor no final
        });
    }

    /**
     * Formata a string para o formato (XX) XXXXX-XXXX
     */
    private String formatarTelefone(String numeros) {
        if (numeros.length() <= 2) {
            return "(" + numeros;
        } else if (numeros.length() <= 7) {
            return "(" + numeros.substring(0, 2) + ") " + numeros.substring(2);
        } else {
            return "(" + numeros.substring(0, 2) + ") " + numeros.substring(2, 7) + "-" + numeros.substring(7);
        }
    }
}