package tn.nexus.Services;

import tn.nexus.Entities.Reservation_evenement;
import tn.nexus.Utils.DBConnection;

import java.sql.*;
import java.util.List;

public class Reservation_evenementServices implements CRUD<Reservation_evenement> {
    private Connection con = DBConnection.getInstance().getcon();
    private Statement st;
    private PreparedStatement ps;

    @Override
    public int insert(Reservation_evenement reservationEvenement) throws SQLException {
        // Requête d'insertion sans id_participation (clé primaire auto-incrémentée)
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
        return 0;
    }

    @Override
    public int delete(Reservation_evenement reservationEvenement) throws SQLException {
        return 0;
    }

    @Override
    public List<Reservation_evenement> showAll() throws SQLException {
        return List.of();
    }
}
