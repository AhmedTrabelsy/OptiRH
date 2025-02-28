package tn.nexus.Controllers.reclamation;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import tn.nexus.Entities.reclamation.Reponse;
import tn.nexus.Services.reclamation.ReponseService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

public class ReponseViewController {
    @FXML
    private TableView<Reponse> reponsesTable;

    @FXML
    private TableColumn<Reponse, String> descriptionColumn;

    @FXML
    private TableColumn<Reponse, Date> dateColumn;

    @FXML
    private ImageView qrCodeImageView; // ImageView pour afficher le QR code

    private int reclamationId;
    private final ReponseService reponseService = new ReponseService();

    /**
     * Définit l'ID de la réclamation et charge les réponses associées.
     *
     * @param reclamationId L'ID de la réclamation.
     */
    public void setReclamationId(int reclamationId) {
        this.reclamationId = reclamationId;
        loadReponses();
    }

    /**
     * Initialise le contrôleur.
     */
    @FXML
    public void initialize() {
        // Liaison des colonnes du tableau avec les propriétés de l'objet Reponse
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        // Ajout d'un listener pour générer un QR code lorsqu'une réponse est sélectionnée
        reponsesTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                generateQRCode(newSelection.getDescription());
            }
        });
    }

    /**
     * Charge les réponses associées à la réclamation.
     */
    private void loadReponses() {
        try {
            List<Reponse> reponses = reponseService.getReponsesByReclamationId(reclamationId);
            ObservableList<Reponse> observableReponses = FXCollections.observableArrayList(reponses);
            reponsesTable.setItems(observableReponses);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Génère un QR code à partir du texte donné et l'affiche dans l'ImageView.
     *
     * @param text Le texte à encoder dans le QR code.
     */
    private void generateQRCode(String text) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        int width = 200;
        int height = 200;
        try {
            // Génération du QR code
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);

            // Conversion du QR code en image JavaFX
            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            Image qrImage = new Image(inputStream);

            // Affichage du QR code dans l'ImageView
            qrCodeImageView.setImage(qrImage);
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
    }
}