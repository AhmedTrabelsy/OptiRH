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
    private ImageView qrCodeImageView; // Assurez-vous que cette ligne est présente

    private int reclamationId;
    private final ReponseService reponseService = new ReponseService();

    public void setReclamationId(int reclamationId) {
        this.reclamationId = reclamationId;
        loadReponses();
    }

    @FXML
    public void initialize() {
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        // Ajoutez un listener pour générer le QR code lorsqu'une réponse est sélectionnée
        reponsesTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                generateQRCode(newSelection.getDescription());
            }
        });
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

    private void generateQRCode(String text) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        int width = 200;
        int height = 200;
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            Image qrImage = new Image(inputStream);
            qrCodeImageView.setImage(qrImage); // Définir l'image dans l'ImageView
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
    }
}