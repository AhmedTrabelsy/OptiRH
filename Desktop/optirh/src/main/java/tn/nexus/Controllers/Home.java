


package tn.nexus.Controllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tn.nexus.Services.EvenementServices;

import java.io.IOException;

public class Home extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override

    public void start(Stage stage) {
        try {
            EvenementServices evenementService = new EvenementServices();
            evenementService.mettreAJourStatutEvenements(); // Mise à jour des statuts au démarrage
            String fxmlPath = "/listeEvenemnt.fxml";
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Liste des événements");
            stage.show();
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement du fichier FXML: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

