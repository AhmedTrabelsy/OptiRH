package tn.nexus.Entities;

public class Mission {
    private int id;
    private String titre;
    private String description;
    private String statut;
    private boolean isPresent;
    private int projetId;

    public Mission() {
    }

    public Mission(int id, String titre, String description, String statut, boolean isPresent, int projetId) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.statut = statut;
        this.isPresent = isPresent;
        this.projetId = projetId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
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

    public boolean isPresent() {
        return isPresent;
    }

    public void setPresent(boolean isPresent) {
        this.isPresent = isPresent;
    }

    public int getProjetId() {
        return projetId;
    }

    public void setProjetId(int projetId) {
        this.projetId = projetId;
    }

    @Override
    public String toString() {
        return "Mission{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", statut='" + statut + '\'' +
                ", isPresent=" + isPresent +
                ", projetId=" + projetId +
                '}';
    }
}
