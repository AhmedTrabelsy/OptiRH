package tn.nexus.Entities;

public class offreEmploi {

    private Integer id;
    private String poste;
    private String description;
    private String status;
    private Integer utilistaeurId;
    
    public offreEmploi() {
    }

    public offreEmploi(String poste, String description, String status, Integer utilistaeurId) {
        this.poste = poste;
        this.description = description;
        this.status = status;
        this.utilistaeurId = utilistaeurId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getUtilistaeurId() {
        return utilistaeurId;
    }

    public void setUtilistaeurId(Integer utilistaeurId) {
        this.utilistaeurId = utilistaeurId;
    }

}
