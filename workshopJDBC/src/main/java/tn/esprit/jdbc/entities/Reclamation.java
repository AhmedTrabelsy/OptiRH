package tn.esprit.jdbc.entities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.SimpleDateFormat;
public class Reclamation {

    private int id;
    private String description;
    private Date date;
    private String status;
    private int utilisateurId;

    // Constructeur par défaut
    public Reclamation() {
    }

    // Constructeur avec tous les paramètres
    public Reclamation(int id, String description, Date date, String status, int utilisateurId) {
        this.id = id;
        this.description = description;
        this.date = date;
        this.status = status;
        this.utilisateurId = utilisateurId;
    }

    // New Constructor to accept a String date and convert it to Date
    public Reclamation(String description, String status, String dateString, int utilisateurId) {
        this.description = description;
        this.status = status;
        this.utilisateurId = utilisateurId;

        // Convert dateString to Date with correct format
        try {
            // Utiliser le bon format pour MySQL : "yyyy-MM-dd"
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); // Format correct
            this.date = formatter.parse(dateString); // Parsing la date
        } catch (ParseException e) {
            e.printStackTrace();
            this.date = new Date(); // Default to current date in case of error
        }
    }



    // Getters et Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public java.sql.Date getDate() {
        return new java.sql.Date(date.getTime()); // Convertir java.util.Date en java.sql.Date
    }


    public void setDate(Date date) {
        this.date = new java.sql.Date(date.getTime()); // Ensure it's stored as java.sql.Date
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getUtilisateurId() {
        return utilisateurId;
    }

    public void setUtilisateurId(int utilisateurId) {
        this.utilisateurId = utilisateurId;
    }

    // Méthode toString pour afficher les informations de l'objet
    @Override



    public String toString() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = formatter.format(date); // Format la date en "yyyy-MM-dd"

        return "Reclamation{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", date=" + formattedDate +  // Utiliser la date formatée
                ", status='" + status + '\'' +
                ", utilisateurId=" + utilisateurId +
                '}';
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + id;
        return result;
    }
}
