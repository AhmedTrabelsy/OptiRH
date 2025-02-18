package tn.nexus.Entities;

import java.sql.Date;
import java.time.LocalDate;

public class Demande {
    
private Integer id;
private String status;
private LocalDate date;
private String description;
private Integer utilistaeurId;

public Demande() {
}

public Demande(String status, LocalDate date, String description, Integer utilistaeurId) {
    this.status = status;
    this.date = date;
    this.description = description;
    this.utilistaeurId = utilistaeurId;
}

public Integer getId() {
    return id;
}

public void setId(Integer id) {
    this.id = id;
}

public String getStatus() {
    return status;
}

public void setStatus(String status) {
    this.status = status;
}

public String getDate() {
    return String.valueOf(date);
}

public void setDate(Date date) {
    this.date = date.toLocalDate();
}

public String getDescription() {
    return description;
}

public void setDescription(String description) {
    this.description = description;
}

public Integer getUtilistaeurId() {
    return utilistaeurId;
}

public void setUtilistaeurId(Integer utilistaeurId) {
    this.utilistaeurId = utilistaeurId;
}



}
