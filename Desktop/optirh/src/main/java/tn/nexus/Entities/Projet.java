package tn.nexus.Entities;


public class Projet {
    private int id;
    private String type;
    private String description;
    private String status;
    private int utilisateurId;


    public Projet() {
    }

    public Projet(int id, String type, String description, String status, int utilisateurId) {
        this.id = id;
        this.type = type;
        this.description = description;
        this.status = status;
        this.utilisateurId = utilisateurId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    // Méthode toString pour affichage
    @Override
    public String toString() {
        return "Projet{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", utilisateurId=" + utilisateurId +
                '}';
    }
}
