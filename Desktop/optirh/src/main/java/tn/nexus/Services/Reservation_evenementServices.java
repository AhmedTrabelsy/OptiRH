package tn.nexus.Services;

import tn.nexus.Entities.Evenement;
import tn.nexus.Entities.Reservation_evenement;
import tn.nexus.Entities.User;
import tn.nexus.Utils.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Reservation_evenementServices implements CRUD<Reservation_evenement> {
    private Connection con = DBConnection.getInstance().getcon();
    private Statement st;
    private PreparedStatement ps;

    @Override
    public int insert(Reservation_evenement reservationEvenement) throws SQLException {
        String req = "INSERT INTO `reservation_evenement`(`id_user`, `id_evenement`, `first_name`, `last_name`, `email`, `telephone`, `date_reservation`) VALUES (?, ?, ?, ?, ?, ?, ?)";

        ps = con.prepareStatement(req);

        // Fixer idUser à 1
        ps.setInt(1, 1);

        // Associer l'ID de l'événement spécifique
        ps.setInt(2, reservationEvenement.getIdEvenement());

        // Remplir les autres champs avec les valeurs de l'objet Reservation_evenement
        ps.setString(3, reservationEvenement.getFirstName());
        ps.setString(4, reservationEvenement.getLastName());
        ps.setString(5, reservationEvenement.getEmail());
        ps.setString(6, reservationEvenement.getTelephone());
        ps.setDate(7, Date.valueOf(reservationEvenement.getDateReservation()));

        // Exécuter la requête et retourner le nombre de lignes affectées
        return ps.executeUpdate();
    }


    @Override
    public int update(Reservation_evenement reservationEvenement) throws SQLException {
        String query = "UPDATE reservation_evenement SET last_name = ?, first_name = ?, telephone = ?, email = ?, date_reservation = ? WHERE id_participation = ?";
        int rowsAffected = 0;

        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {

            // Remplir les paramètres de la requête
            preparedStatement.setString(1, reservationEvenement.getLastName());
            preparedStatement.setString(2, reservationEvenement.getFirstName());
            preparedStatement.setString(3, reservationEvenement.getTelephone());
            preparedStatement.setString(4, reservationEvenement.getEmail());
            preparedStatement.setDate(5, java.sql.Date.valueOf(reservationEvenement.getDateReservation()));
            preparedStatement.setInt(6, reservationEvenement.getIdParticipation());

            // Exécuter la mise à jour
            rowsAffected = preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e;
        }

        return rowsAffected;
    }

    @Override
    public int delete(Reservation_evenement reservationEvenement) throws SQLException {
        String query = "DELETE FROM reservation_evenement WHERE id_participation = ?";
        int rowsAffected = 0;

        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            // Utiliser l'ID de la participation de l'objet Reservation_evenement pour la suppression
            preparedStatement.setInt(1, reservationEvenement.getIdParticipation());

            // Exécuter la suppression
            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de la réservation : " + e.getMessage());
            throw e;
        }

        return rowsAffected;
    }

    @Override
    public List<Reservation_evenement> showAll() throws SQLException {
        List<Reservation_evenement> reservations = new ArrayList<>();
        // Jointure entre reservation_evenement et evenement

        String req = "SELECT r.*, e.titre " +
                "FROM reservation_evenement r " +
                "JOIN evenement e ON r.id_evenement = e.id_evenement"; // JOIN pour lier les deux tables

        try (PreparedStatement ps = con.prepareStatement(req)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String titre = rs.getString("titre");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("email");
                String telephone = rs.getString("telephone");

                LocalDate DATE=rs.getDate("date_reservation").toLocalDate();

                Reservation_evenement reservation = new Reservation_evenement(titre,firstName,lastName,email,telephone,DATE);

                reservations.add(reservation);

            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Erreur lors de la récupération des réservations", e);
        }

        return reservations;
    }




/*
    public List<Reservation_evenement> getReservatiobByuserID() throws SQLException {
        List<Reservation_evenement> reservations = new ArrayList<>();

        String query = "SELECT id_participation, id_user, id_evenement, date_reservation " +
                "FROM reservation_evenement WHERE id_user = 1"; // Filtrer par user

        try (PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Reservation_evenement reservation = new Reservation_evenement();
                reservation.setIdParticipation(rs.getInt("id_participation"));
                reservation.setIdUser(rs.getInt("id_user"));
                reservation.setIdEvenement(rs.getInt("id_evenement"));
                reservation.setDateReservation(rs.getDate("date_reservation").toLocalDate());

                reservations.add(reservation);
            }
        }

        return reservations;

    }
*/
public List<Reservation_evenement> getReservatiobByuserID(User user) throws SQLException {
    List<Reservation_evenement> reservations = new ArrayList<>();

    // Requête SQL avec paramètre pour l'ID de l'utilisateur
    String query = "SELECT r.id_participation, r.id_user, r.id_evenement, r.date_reservation, " +
            "r.last_name, r.first_name, r.telephone, r.email " +
            "FROM reservation_evenement r " +
            "WHERE r.id_user ="+user.getId(); // id_user est le paramètre

    try (PreparedStatement stmt = con.prepareStatement(query)) {
        // Définir l'ID de l'utilisateur comme paramètre de la requête
        //stmt.setInt(1, user.getId()); // On suppose que l'objet User a une méthode getId()

        // Exécuter la requête et récupérer les résultats
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Reservation_evenement reservation = new Reservation_evenement();
                reservation.setIdParticipation(rs.getInt("id_participation"));
                reservation.setIdUser(rs.getInt("id_user"));
                reservation.setIdEvenement(rs.getInt("id_evenement"));

                // Vérifier que la date n'est pas null avant de la convertir
                Date dateReservation = rs.getDate("date_reservation");
                if (dateReservation != null) {
                    reservation.setDateReservation(dateReservation.toLocalDate());
                }

                // Récupérer les informations de l'utilisateur
                reservation.setLastName(rs.getString("last_name"));
                reservation.setFirstName(rs.getString("first_name"));
                reservation.setTelephone(rs.getString("telephone"));
                reservation.setEmail(rs.getString("email"));

                reservations.add(reservation);
            }
        }
    }

    return reservations;
}









}
