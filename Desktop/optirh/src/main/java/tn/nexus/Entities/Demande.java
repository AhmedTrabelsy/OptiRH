package tn.nexus.Entities;

import java.sql.Date;
import java.sql.Timestamp;

public class Demande {

    private int id;
    private int utilisateurId;
    private int offreId;
    public enum Statut {
        ACCEPTEE,
        REFUSEE,
        EN_ATTENTE;
    }
    private Statut statut; // "En attente", "Acceptée", "Refusée"
    private Timestamp date;
    private String description;
    private String fichierPieceJointe;

    public Demande() {}

    public Demande(int id, int utilisateurId, int offreId, Statut statut, Timestamp date, String description, String fichierPieceJointe) {
        this.id = id;
        this.utilisateurId = utilisateurId;
        this.offreId = offreId;
        this.statut = statut;
        this.date = date;
        this.description = description;
        this.fichierPieceJointe = fichierPieceJointe;
    }

    public Demande(int utilisateurId, int offreId, Statut statut, Timestamp date, String description, String fichierPieceJointe) {
        this.utilisateurId = utilisateurId;
        this.offreId = offreId;
        this.statut = statut;
        this.date = date;
        this.description = description;
        this.fichierPieceJointe = fichierPieceJointe;
    }
    public Demande(Statut statut, String description, Date date, int utilisateurId) {
        this.statut = statut;
        this.description = description;
        this.date = new Timestamp(date.getTime()); // Convertir Date en Timestamp
        this.utilisateurId = utilisateurId;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUtilisateurId() {
        return utilisateurId;
    }

    public void setUtilisateurId(int utilisateurId) {
        this.utilisateurId = utilisateurId;
    }

    public int getOffreId() {
        return offreId;
    }

    public void setOffreId(int offreId) {
        this.offreId = offreId;
    }

    public Statut getStatut() {
        return statut;
    }

    public void setStatut(Statut statut) {
        this.statut = statut;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFichierPieceJointe() {
        return fichierPieceJointe;
    }

    public void setFichierPieceJointe(String fichierPieceJointe) {
        this.fichierPieceJointe = fichierPieceJointe;
    }

    @Override
    public String toString() {
        return "Demande{" +
                "id=" + id +
                ", utilisateurId=" + utilisateurId +
                ", offreId=" + offreId +
                ", statut=" + statut +  // Pas besoin de guillemets pour un ENUM                ", date=" + date +
                ", description='" + description + '\'' +
                ", fichierPieceJointe='" + fichierPieceJointe + '\'' +
                '}';
    }
}

