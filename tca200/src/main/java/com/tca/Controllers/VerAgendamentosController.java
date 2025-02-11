package com.tca.Controllers;

import com.tca.DAO.AgendamentoDAO;
import com.tca.Models.Agendamento;
import com.tca.Models.Dono;
import com.tca.Models.Pet;
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
import javafx.util.Callback;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class VerAgendamentosController {

    @FXML
    private TableView<Agendamento> agendamentosTable;

    @FXML
    private TableColumn<Agendamento, String> nomeDonoColumn;
    @FXML
    private TableColumn<Agendamento, String> nomePetColumn;
    @FXML
    private TableColumn<Agendamento, String> dataAgendamentoColumn;
    @FXML
    private TableColumn<Agendamento, String> motivoConsultaColumn;
    @FXML
    private TableColumn<Agendamento, Void> acoesColumn;

    @FXML
    private Button voltarButton;

    private ObservableList<Agendamento> agendamentosList;
    private final AgendamentoDAO agendamentoDAO = new AgendamentoDAO(); // Instância única do DAO

    @FXML
    public void initialize() {
        agendamentosList = FXCollections.observableArrayList();
        configurarColunas();
        carregarAgendamentos();
        agendamentosTable.setItems(agendamentosList);
    }

    private void configurarColunas() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Nome do Dono
        nomeDonoColumn.setCellValueFactory(cellData -> {
            Pet pet = cellData.getValue().getPet();
            Dono dono = (pet != null) ? pet.getDono() : null;
            return new SimpleStringProperty((dono != null) ? dono.getNome() : "Desconhecido");
        });

        // Nome do Pet
        nomePetColumn.setCellValueFactory(cellData -> {
            Pet pet = cellData.getValue().getPet();
            return new SimpleStringProperty((pet != null) ? pet.getNome() : "Desconhecido");
        });

        // Data do Agendamento (com verificação para evitar NullPointerException)
        dataAgendamentoColumn.setCellValueFactory(cellData -> {
            LocalDate data = cellData.getValue().getDataAgendamento();
            return new SimpleStringProperty((data != null) ? data.format(formatter) : "Não Informado");
        });

        // Motivo da Consulta
        motivoConsultaColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMotivoConsulta()));

        // Botão "Ver Motivo"
        motivoConsultaColumn.setCellFactory(column -> new TableCell<>() {
            private final Button viewButton = new Button("Ver Motivo");

            {
                viewButton.setOnAction(event -> {
                    Agendamento agendamento = getTableView().getItems().get(getIndex());
                    mostrarMotivoCompleto(agendamento);
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : viewButton);
            }
        });

        // Coluna de Ações (Alterar e Excluir)
        acoesColumn.setCellFactory(getAcoesCellFactory());
    }

    private Callback<TableColumn<Agendamento, Void>, TableCell<Agendamento, Void>> getAcoesCellFactory() {
        return column -> new TableCell<>() {
            private final Button excluirButton = new Button("Excluir");
            private final Button alterarButton = new Button("Alterar");

            {
                excluirButton.setOnAction(event -> handleExcluir(getTableView().getItems().get(getIndex())));
                alterarButton.setOnAction(event -> handleAlterar(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : new HBox(5, excluirButton, alterarButton));
            }
        };
    }

    private void mostrarMotivoCompleto(Agendamento agendamento) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Motivo Completo");
        alert.setHeaderText("Motivo do Agendamento:");
        alert.setContentText(agendamento.getMotivoConsulta());
        alert.showAndWait();
    }

    public void carregarAgendamentos() {
        agendamentosList.clear();
        try {
            List<Agendamento> agendamentos = agendamentoDAO.buscarTodosAgendamentos();
            agendamentosList.addAll(agendamentos);
        } catch (SQLException e) {
            exibirErro("Erro ao carregar agendamentos", e.getMessage());
        }
    }

    private void handleAlterar(Agendamento agendamento) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tca/Views/TelaAlterarAgendamentos.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));

            AlterarAgendamentosController controller = loader.getController();
            controller.setAgendamento(agendamento);

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Alterar Agendamento");
            stage.showAndWait();

            carregarAgendamentos();
        } catch (IOException e) {
            exibirErro("Erro ao abrir tela de alteração", e.getMessage());
        }
    }

    private void handleExcluir(Agendamento agendamento) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirmação de Exclusão");
        confirmAlert.setHeaderText("Deseja realmente excluir este agendamento?");
        confirmAlert.setContentText("Essa ação é irreversível.");
    
        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                agendamentoDAO.excluirAgendamento(agendamento.getId()); // Agora lança SQLException corretamente
                agendamentosList.remove(agendamento);
    
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Sucesso");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Agendamento excluído com sucesso!");
                successAlert.showAndWait();
            } catch (SQLException e) {
                exibirErro("Erro ao excluir agendamento", e.getMessage()); // Agora o erro será tratado corretamente
            }
        }
    }
    
    @FXML
    private void handleVoltarTelaInicial() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tca/Views/TelaInicial.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));

            Stage currentStage = (Stage) agendamentosTable.getScene().getWindow();
            currentStage.close();

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Tela Inicial");
            stage.show();
        } catch (IOException e) {
            exibirErro("Erro ao carregar a tela inicial", "Houve um erro ao tentar carregar a tela inicial.");
        }
    }

    private void exibirErro(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(titulo);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}