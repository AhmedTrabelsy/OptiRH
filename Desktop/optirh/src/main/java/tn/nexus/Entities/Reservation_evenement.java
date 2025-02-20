package tn.nexus.Entities;

import java.time.LocalDate;
import java.util.Date;

public class Reservation_evenement {

    private int idParticipation;
    private int idUser;
    private int idEvenement;
    private String firstName;
    private String lastName;
    private String email;
    private String telephone;
    private LocalDate dateReservation;
    private  String TitreEvenement;
    private LocalDate Date_debut;

    // Constructeurs
    public Reservation_evenement() {}

//    public Reservation_evenement( int idParticipation ,int idUser, int idEvenement, String firstName, String lastName, String email, String telephone, LocalDate dateReservation) {
//
//        this.idParticipation = idParticipation;
//        this.idUser = idUser;
//        this.idEvenement = idEvenement;
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.email = email;
//        this.telephone = telephone;
//        this.dateReservation = dateReservation;
//
//    }

   public Reservation_evenement( String titre, String firstName, String lastName, String email, String telephone, LocalDate dateReservation) {
        this.TitreEvenement = titre;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.telephone = telephone;
        this.dateReservation = dateReservation;
    }

    public Reservation_evenement(int idParticipation,int idEvent,int telephone,String email,String firstName,String lastName,LocalDate dateReservation,String titre,LocalDate dateDebut) {
        this.idParticipation = idParticipation;
        this.idUser = idEvent;
        this.idEvenement = idEvent;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateReservation = dateReservation;
        this.TitreEvenement = titre;
        this.Date_debut = dateDebut;

    }



    public String getTitre() {
        return TitreEvenement;
    }

    public void setTitre(String titre) {
        this.TitreEvenement = titre;
    }

    public LocalDate getDateDebut() {
        return Date_debut;
    }
    public void setDateDebut(LocalDate dateDebut) {
        this.Date_debut = dateDebut;
    }


//    public Reservation_evenement(int idUser, int idEvenement, String firstName, String lastName, String email, String telephone, LocalDate dateReservation) {
//
//        this.idUser = idUser;
//        this.idEvenement = idEvenement;
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.email = email;
//        this.telephone = telephone;
//        this.dateReservation = dateReservation;
//    }
    public Reservation_evenement (String lastName, String firstName, String telephone, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.telephone = telephone;
        this.email = email;
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
                " TitreEvenement='" + TitreEvenement + '\'' +

                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", telephone='" + telephone + '\'' +
                ", dateReservation=" + dateReservation +
                '}';
    }
}
