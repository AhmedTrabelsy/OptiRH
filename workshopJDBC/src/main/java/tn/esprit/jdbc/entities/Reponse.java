package tn.esprit.jdbc.entities;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
public class Reponse {

    private int id;
    private String description;
    private Date date;
    private String status;
    private int reclamationId;

    // Constructeur par défaut
    public Reponse() {
    }

    // Constructeur avec tous les paramètres
    public Reponse(int id, String description, Date date, String status, int reclamationId) {
        this.id = id;
        this.description = description;
        this.date = date;
        this.status = status;
        this.reclamationId = reclamationId;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getReclamationId() {
        return reclamationId;
    }

    public void setReclamationId(int reclamationId) {
        this.reclamationId = reclamationId;
    }

    // Méthode toString pour afficher les informations de l'objet
    @Override
    public String toString() {
        return "Reponse{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", status='" + status + '\'' +
                ", reclamationId=" + reclamationId +
                '}';
    }
}