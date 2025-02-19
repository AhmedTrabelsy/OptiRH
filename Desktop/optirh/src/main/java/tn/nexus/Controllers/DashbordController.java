package tn.nexus.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class DashbordController {

    public void homeOnClick(MouseEvent mouseEvent) {
    }

    public void tab1onclick(MouseEvent mouseEvent) {
    }

    public void tab2onclick(MouseEvent mouseEvent) {
    }

    public void ouvrirEvent(MouseEvent mouseEvent) {
    }

    @FXML
    private AnchorPane dashContent; // Zone où afficher la gestion de projet

    @FXML
    private void ouvrirGestionProjet() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Projet.fxml"));
            Parent projetView = loader.load();
            dashContent.getChildren().clear(); // Effacer le contenu actuel
            dashContent.getChildren().add(projetView); // Afficher la nouvelle vue
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
