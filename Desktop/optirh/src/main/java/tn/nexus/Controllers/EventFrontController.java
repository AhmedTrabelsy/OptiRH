package tn.nexus.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tn.nexus.Entities.Evenement;
import tn.nexus.Services.EvenementServices;

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

    public void handleMyReservation(ActionEvent actionEvent) {
    }
}
