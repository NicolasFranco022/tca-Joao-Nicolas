package com.tca.Controllers;

import com.tca.DAO.PetDAO;
import com.tca.Models.Pet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class VerPetsController {
    
    @FXML
    private TableView<Pet> petsTable;
    
    @FXML
    private TableColumn<Pet, String> nomePetColumn;
    
    @FXML
    private TableColumn<Pet, String> racaPetColumn;
    
    @FXML
    private TableColumn<Pet, String> tipoPetColumn;
    
    @FXML
    private TableColumn<Pet, Integer> idadePetColumn;
    
    @FXML
    private TableColumn<Pet, String> sexoPetColumn;
    
    @FXML
    private TableColumn<Pet, String> donoPetColumn;
    
    @FXML
    private TableColumn<Pet, Void> acaoColumn;
    
    private final PetDAO petDAO = new PetDAO();

    @FXML
    public void initialize() {
        configurarColunas();
        carregarPets();
    }

    private void configurarColunas() {
        nomePetColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        racaPetColumn.setCellValueFactory(new PropertyValueFactory<>("raca"));
        tipoPetColumn.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        idadePetColumn.setCellValueFactory(new PropertyValueFactory<>("idade"));
        sexoPetColumn.setCellValueFactory(new PropertyValueFactory<>("sexo"));
        donoPetColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDono().getNome()));
        
        acaoColumn.setCellFactory(param -> new TableCell<>() {
            private final Button btnAlterar = new Button("Alterar");
            private final Button btnExcluir = new Button("Excluir");
            private final HBox container = new HBox(10, btnAlterar, btnExcluir);
            
            {
                btnAlterar.setOnAction(event -> abrirTelaAlteracao(getTableView().getItems().get(getIndex())));
                btnExcluir.setOnAction(event -> excluirPet(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(container);
                }
            }
        });
    }

    private void carregarPets() {
        try {
            List<Pet> pets = petDAO.buscarTodosPets();
            ObservableList<Pet> petsObservableList = FXCollections.observableArrayList(pets);
            petsTable.setItems(petsObservableList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void excluirPet(Pet pet) {
        try {
            petDAO.excluirPet(pet.getId());
            carregarPets();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void abrirTelaAlteracao(Pet pet) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tca/Views/TelaAlterarPets.fxml"));
            Parent root = loader.load();
            
            AlterarPetController controller = loader.getController();
            controller.setPet(pet);
            
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Alterar Pet");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleVoltarTelaInicial(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}