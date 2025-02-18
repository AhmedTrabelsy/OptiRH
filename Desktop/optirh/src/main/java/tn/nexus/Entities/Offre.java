package tn.nexus.Entities;

import java.time.LocalDateTime;

public class Offre {

    private int id;
    private String poste;
    private String description;
    private String statut;
    private LocalDateTime dateCreation;

    public Offre() {}

    public Offre(int id, String poste, String description, String statut, LocalDateTime dateCreation) {
        this.id = id;
        this.poste = poste;
        this.description = description;
        this.statut = statut;
        this.dateCreation = dateCreation;
    }

    public Offre(String poste, String description, String statut, LocalDateTime dateCreation) {
        this.poste = poste;
        this.description = description;
        this.statut = statut;
        this.dateCreation = dateCreation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPoste() {
        return poste;
    }

    public void setPoste(String poste) {
        this.poste = poste;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    @Override
    public String toString() {
        return "Offre{" +
                "id=" + id +
                ", poste='" + poste + '\'' +
                ", description='" + description + '\'' +
                ", statut='" + statut + '\'' +
                ", dateCreation=" + dateCreation +
                '}';
    }
}
