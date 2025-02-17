package tn.esprit.jdbc.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import tn.esprit.jdbc.entities.Reponse;
import tn.esprit.jdbc.services.ReponseService;

import java.sql.Date;
import java.util.List;

public class ReponseController {
    @FXML
    private TextArea descriptionField;
    @FXML
    private DatePicker dateField;
    @FXML
    private TableView<Reponse> reponsesTable;
    @FXML
    private TableColumn<Reponse, Integer> idColumn;
    @FXML
    private TableColumn<Reponse, String> descriptionColumn;
    @FXML
    private TableColumn<Reponse, Date> dateColumn;

    private int reclamationId;
    private final ReponseService reponseService = new ReponseService();

    public void setReclamationId(int reclamationId) {
        this.reclamationId = reclamationId;
        loadReponses();
    }

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
    }

    private void loadReponses() {
        try {
            List<Reponse> reponses = reponseService.getReponsesByReclamationId(reclamationId);
            ObservableList<Reponse> observableReponses = FXCollections.observableArrayList(reponses);
            reponsesTable.setItems(observableReponses);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void ajouterReponse() {
        try {
            Reponse reponse = new Reponse(
                    0,
                    descriptionField.getText(),
                    Date.valueOf(dateField.getValue()),
                    reclamationId
            );
            reponseService.insert(reponse);
            loadReponses();
            clearFields();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void modifierReponse() {
        Reponse selectedReponse = reponsesTable.getSelectionModel().getSelectedItem();
        if (selectedReponse != null) {
            try {
                selectedReponse.setDescription(descriptionField.getText());
                selectedReponse.setDate(Date.valueOf(dateField.getValue()));
                reponseService.update(selectedReponse);
                loadReponses();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void supprimerReponse() {
        Reponse selectedReponse = reponsesTable.getSelectionModel().getSelectedItem();
        if (selectedReponse != null) {
            try {
                reponseService.delete(selectedReponse);
                loadReponses();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void clearFields() {
        descriptionField.clear();
        dateField.setValue(null);
    }
}