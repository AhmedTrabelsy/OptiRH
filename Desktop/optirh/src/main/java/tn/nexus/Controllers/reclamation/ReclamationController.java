package tn.nexus.Controllers.reclamation;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import tn.nexus.Entities.reclamation.Reclamation;
import tn.nexus.Services.reclamation.ReclamationService;
import tn.nexus.Utils.WrapWithSideBar;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class ReclamationController implements Initializable, WrapWithSideBar {
    @FXML
    private AnchorPane sideBar;
    @FXML
    private TableView<Reclamation> reclamationsTable;
    @FXML
    private TableColumn<Reclamation, String> descriptionColumn;
    @FXML
    private TableColumn<Reclamation, String> statusColumn;
    @FXML
    private TableColumn<Reclamation, LocalDate> dateColumn;

    private final ReclamationService reclamationService = new ReclamationService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeSideBar(sideBar);
        try {
            // Configuration des colonnes
            descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
            statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
            dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

            // Chargement des données
            List<Reclamation> reclamationList = reclamationService.showAll();
            ObservableList<Reclamation> observableReclamationList = FXCollections.observableArrayList(reclamationList);
            reclamationsTable.setItems(observableReclamationList);

            // Ajout d'une colonne "Action" avec un bouton "Réponse"
            TableColumn<Reclamation, Void> actionColumn = new TableColumn<>("Action");
            actionColumn.setCellFactory(param -> new TableCell<>() {
                private final Button btn = new Button("Réponse");
                {
                    btn.setStyle("-fx-background-color: #007BFF; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");
                    btn.setOnAction(event -> {
                        Reclamation reclamation = getTableView().getItems().get(getIndex());
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/reclamation/ReponseForm.fxml"));
                            Parent root = loader.load();
                            ReponseController controller = loader.getController();
                            controller.setReclamationId(reclamation.getId());
                            Stage stage = new Stage();
                            stage.setScene(new Scene(root));
                            stage.setTitle("Gérer les réponses");
                            stage.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    setGraphic(empty ? null : btn);
                }
            });

            // Ajouter la colonne "Action" à la table
            reclamationsTable.getColumns().add(actionColumn);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}