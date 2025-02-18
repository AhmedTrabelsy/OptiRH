package tn.nexus.Entities;

import java.time.LocalDate;

import tn.nexus.Entities.Evenement;

public class Reservation_evenement {

    private int idParticipation;
    private int idUser;
    private int idEvenement;
    private String firstName;
    private String lastName;
    private String email;
    private String telephone;
    private LocalDate dateReservation;

    // Constructeurs
    public Reservation_evenement() {}

    public Reservation_evenement( int idUser, int idEvenement, String firstName, String lastName, String email, String telephone, LocalDate dateReservation) {

        this.idUser = idUser;
        this.idEvenement = idEvenement;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.telephone = telephone;
        this.dateReservation = dateReservation;
    }


    // Getters et Setters
    public int getIdParticipation() {
        return idParticipation;
    }

    public void setIdParticipation(int idParticipation) {
        this.idParticipation = idParticipation;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getIdEvenement() {
        return idEvenement;
    }

    public void setIdEvenement(int idEvenement) {
        this.idEvenement = idEvenement;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public LocalDate getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(LocalDate dateReservation) {
        this.dateReservation = dateReservation;
    }

    @Override
    public String toString() {
        return "ReservationEvenement{" +
                "idParticipation=" + idParticipation +
                ", idUser=" + idUser +
                ", idEvenement=" + idEvenement +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", telephone='" + telephone + '\'' +
                ", dateReservation=" + dateReservation +
                '}';
    }
}
