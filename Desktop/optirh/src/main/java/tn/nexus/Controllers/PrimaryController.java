package tn.nexus.Controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import java.io.IOException;

public class PrimaryController {

    @FXML
    private Text tab1; // Tableau de bord
    @FXML
    private Text tabOffres; // Offres
    @FXML
    private Text tabDemandes; // Demandes

    // Action pour naviguer vers la page Offres.fxml
    @FXML
    private void navigateToOffres(MouseEvent event) throws IOException {
        // Utilisation du bon chemin relatif pour le fichier FXML
        AnchorPane root = FXMLLoader.load(getClass().getResource("/Offres.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    // Action pour naviguer vers la page ListeDemandes.fxml
    @FXML
    private void navigateToDemandes(MouseEvent event) throws IOException {
        // Utilisation du bon chemin relatif pour le fichier FXML
        BorderPane root = FXMLLoader.load(getClass().getResource("/ListeDemande.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    // Initialisation des événements des clics sur les éléments de texte
    @FXML
    private void initialize() {
        tabOffres.setOnMouseClicked(event -> {
            try {
                navigateToOffres(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        tabDemandes.setOnMouseClicked(event -> {
            try {
                navigateToDemandes(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
