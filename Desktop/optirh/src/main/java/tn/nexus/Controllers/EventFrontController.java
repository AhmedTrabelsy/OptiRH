package tn.nexus.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import tn.nexus.Entities.Evenement;
import tn.nexus.Services.EvenementServices;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class EventFrontController {

    @FXML
    private FlowPane eventContainer;

    private final EvenementServices evenementService = new EvenementServices();

    @FXML
    public void initialize() throws SQLException {
        loadEvents();
    }

    private void loadEvents() throws SQLException {
        List<Evenement> events = evenementService.showAll();

        for (Evenement event : events) {
            VBox card = new VBox(10);
            card.setStyle("-fx-padding: 10px; -fx-border-color: #ccc; -fx-border-radius: 10px; -fx-background-color: white; -fx-alignment: center;");

            ImageView imageView = new ImageView();
            imageView.setFitWidth(200);
            imageView.setFitHeight(150);

            File file = new File(event.getImage());
            if (file.exists()) {
                Image image = new Image(file.toURI().toString());
                imageView.setImage(image);
            }

            Label titleLabel = new Label(event.getTitre());
            titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

            Button detailsButton = new Button("Show Details");
            detailsButton.setOnAction(e -> openEventDetails(event));

            card.getChildren().addAll(imageView, titleLabel, detailsButton);
            eventContainer.getChildren().add(card);
        }
    }

    private void openEventDetails(Evenement event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/event_details.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Event Details");

            EventDetailsController controller = loader.getController();
            controller.setEventData(event);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleMyReservation(ActionEvent event) {  // Vérifie bien que ActionEvent event est présent
        try {
            // Charger la nouvelle scène
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/reservation_list.fxml"));
            Parent reservationView = loader.load();

            // Obtenir la fenêtre actuelle et changer la scène
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(reservationView);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();  // Afficher l'erreur dans la console
        }
    }
}
