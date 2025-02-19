package tn.nexus.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.nexus.Entities.Demande;
import tn.nexus.services.DemandeService;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;

public class ModifierDemandeController {

    @FXML private TextField txtDescription;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<String> comboStatut;

    private DemandeService demandeService = new DemandeService();
    private Demande demande;

    @FXML
    public void initialize() {
        // Remplir la comboBox avec les statuts disponibles
        comboStatut.getItems().setAll(
                Demande.Statut.EN_ATTENTE.toString(),
                Demande.Statut.ACCEPTEE.toString(),
                Demande.Statut.REFUSEE.toString()
        );
    }


    public void setDemande(Demande demande) {
        this.demande = demande;
        txtDescription.setText(demande.getDescription());
        datePicker.setValue(demande.getDate().toLocalDateTime().toLocalDate());
        // Vérifier que les valeurs sont bien chargées avant de définir la sélection
        if (comboStatut.getItems().isEmpty()) {
            initialize(); // Remplir la ComboBox si ce n'est pas déjà fait
        }

        comboStatut.setValue(demande.getStatut().toString());
    }

    @FXML
    private void modifierDemande() {
        demande.setDescription(txtDescription.getText());
        demande.setDate(Timestamp.valueOf(datePicker.getValue().atStartOfDay()));
        demande.setStatut(Demande.Statut.valueOf(comboStatut.getValue()));

        try {
            demandeService.update(demande);
            fermerFenetre();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void annuler() {
        fermerFenetre();
    }

    private void fermerFenetre() {
        Stage stage = (Stage) txtDescription.getScene().getWindow();
        stage.close();
    }
}
