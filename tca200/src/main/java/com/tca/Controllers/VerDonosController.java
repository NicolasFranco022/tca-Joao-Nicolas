package com.tca.Controllers;

import com.tca.DAO.DonoDAO;
import com.tca.Models.Dono;
import com.tca.db.DatabaseConnector;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class VerDonosController {

    @FXML
    private TableView<Dono> donosTable;

    @FXML
    private TableColumn<Dono, String> nomeDonoColumn;
    @FXML
    private TableColumn<Dono, String> telefoneDonoColumn;
    @FXML
    private TableColumn<Dono, String> emailDonoColumn;

    @FXML
    private TableColumn<Dono, Void> acoesColumn;

    @FXML
    private Button voltarButton;

    private ObservableList<Dono> donosList;
    private DonoDAO donoDAO = new DonoDAO();

    @FXML
    public void initialize() {
        donosList = FXCollections.observableArrayList();
        configurarColunas();
        carregarDonos();
        donosTable.setItems(donosList);
    }

    private void configurarColunas() {
        nomeDonoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNome()));
        telefoneDonoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTelefone()));
        emailDonoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));

        acoesColumn.setCellFactory(column -> new TableCell<>() {
            private final Button excluirButton = new Button("Excluir");
            private final Button alterarButton = new Button("Alterar");

            {
                excluirButton.setOnAction(event -> handleExcluir(getTableView().getItems().get(getIndex())));
                alterarButton.setOnAction(event -> handleAlterar(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(new HBox(5, excluirButton, alterarButton));
                }
            }
        });
    }

    private void carregarDonos() {
        donosList.clear();
        try {
            List<Dono> donos = donoDAO.buscarTodosDonos();
            donosList.addAll(donos);
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro ao carregar donos");
            alert.setContentText("Houve um problema ao buscar os donos no banco de dados.");
            alert.showAndWait();
        }
    }
    
    private void handleAlterar(Dono dono) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tca/Views/TelaAlterarDono.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
    
            AlterarDonoController controller = loader.getController();
            controller.setDono(dono);
    
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Alterar Dono");
            stage.showAndWait();
    
            carregarDonos();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    

    private void handleExcluir(Dono dono) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirmação de Exclusão");
        confirmAlert.setHeaderText("Deseja realmente excluir este dono?");
        confirmAlert.setContentText("Essa ação é irreversível e também excluirá todos os pets e agendamentos relacionados.");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try (Connection conn = DatabaseConnector.getConnection()) {
                if (conn == null) {
                    System.err.println("Erro ao conectar ao banco de dados.");
                    return;
                }

                conn.setAutoCommit(false);
                try {
                    donoDAO.excluirDono(dono.getId());
                    conn.commit();

                    donosList.remove(dono);
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Sucesso");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("Dono excluído com sucesso!");
                    successAlert.showAndWait();

                } catch (SQLException e) {
                    conn.rollback();
                    System.err.println("Erro ao excluir dono: " + e.getMessage());
                    e.printStackTrace();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("A exclusão foi cancelada pelo usuário.");
        }
    }

    @FXML
    private void handleVoltarTelaInicial() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tca/Views/TelaInicial.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));

            Stage currentStage = (Stage) donosTable.getScene().getWindow();
            currentStage.close();

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Tela Inicial");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro ao carregar a tela inicial");
            alert.setContentText("Houve um erro ao tentar carregar a tela inicial.");
            alert.showAndWait();
        }
    }
}
