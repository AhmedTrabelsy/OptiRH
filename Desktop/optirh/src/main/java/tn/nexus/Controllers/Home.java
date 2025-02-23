package tn.nexus.Controllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class Home extends Application {
    public static void main(String[] args){
        launch(args);
    }
    @Override
    public void start(Stage stage) throws Exception {
        String fxmlPath = "/Front_OffresList.fxml"; // Mets le bon chemin si le fichier est dans un sous-dossier
        System.out.println("Chargement du fichier FXML : " + fxmlPath);

        URL fxmlLocation = getClass().getResource(fxmlPath);
        if (fxmlLocation == null) {
            System.out.println("Erreur : Fichier FXML introuvable !");
            return;
        } else {
            System.out.println("Fichier FXML trouvé : " + fxmlLocation);
        }
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("OptiRH");
        stage.show();
    }
}
