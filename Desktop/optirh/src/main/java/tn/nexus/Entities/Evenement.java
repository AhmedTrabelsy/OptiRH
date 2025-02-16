package tn.nexus.Entities;

import java.time.LocalDate;
import java.time.LocalTime;

public class Evenement {

    private int idEvenement;
    private String titre;
    private String lieu;
    private String description;
    private double prix;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String image;
    private LocalTime heure;

    // Constructeurs
    public Evenement() {}

    public Evenement( String titre, String lieu,String description, double prix, LocalDate dateDebut, LocalDate dateFin, String image, LocalTime heure) {

        this.titre = titre;
        this.lieu = lieu;
        this.description = description;
        this.prix = prix;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.image = image;
        this.heure = heure;
    }

    public Evenement(int idEvenement, String titre, String description, String lieu, double prix, LocalDate dateDebut, LocalDate dateFin, LocalTime heure, String image) {
        this.idEvenement = idEvenement;
        this.titre = titre;
        this.description = description;
        this.lieu = lieu;
        this.prix = prix;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.heure = heure;
        this.image = image;
    }


    // Getters et Setters
    public int getIdEvenement() {
        return idEvenement;
    }

    public void setIdEvenement(int idEvenement) {
        this.idEvenement = idEvenement;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public LocalTime getHeure() {
        return heure;
    }

    public void setHeure(LocalTime heure) {
        this.heure = heure;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Evenement{" +
                "idEvenement=" + idEvenement +
                ", titre='" + titre + '\'' +
                ", lieu='" + lieu + '\'' +
                ", description='" + description + '\'' +
                ", prix=" + prix +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", image='" + image + '\'' +
                ", heure=" + heure +
                '}';
    }
}
