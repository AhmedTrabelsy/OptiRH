package tn.nexus.services;

import tn.nexus.Entities.ReservationTrajet;
import tn.nexus.Utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationTrajetService implements CRUD<ReservationTrajet> {
    private Connection connection;

    public ReservationTrajetService() {
        this.connection = DBConnection.getInstance().getcon(); // Utilisation du Singleton
    }

    @Override
    public int insert(ReservationTrajet reservation) throws SQLException {
        // Contrôle de saisie
        if (reservation.getDisponibilite() == null || reservation.getDisponibilite().isEmpty()) {
            throw new IllegalArgumentException("La disponibilité ne peut pas être vide.");
        }
        if (reservation.getVehiculeId() <= 0) {
            throw new IllegalArgumentException("L'ID du véhicule doit être positif.");
        }
        if (reservation.getTrajetId() <= 0) {
            throw new IllegalArgumentException("L'ID du trajet doit être positif.");
        }
        if (reservation.getUserId() <= 0) {
            throw new IllegalArgumentException("L'ID de l'utilisateur doit être positif.");
        }

        // Vérifier si le véhicule, le trajet et l'utilisateur existent
        if (!vehiculeExists(reservation.getVehiculeId())) {
            throw new IllegalArgumentException("Le véhicule spécifié n'existe pas.");
        }
        if (!trajetExists(reservation.getTrajetId())) {
            throw new IllegalArgumentException("Le trajet spécifié n'existe pas.");
        }
        if (!userExists(reservation.getUserId())) {
            throw new IllegalArgumentException("L'utilisateur spécifié n'existe pas.");
        }

        String query = "INSERT INTO reservationtrajet (disponibilite, user_id, trajet_id, vehicule_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, reservation.getDisponibilite());
            statement.setInt(2, reservation.getUserId());
            statement.setInt(3, reservation.getTrajetId());
            statement.setInt(4, reservation.getVehiculeId());

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1); // Retourne l'ID généré
                    }
                }
            }
        }
        return -1; // Échec de l'insertion
    }

    @Override
    public int update(ReservationTrajet reservation) throws SQLException {
        // Contrôle de saisie
        if (reservation.getDisponibilite() == null || reservation.getDisponibilite().isEmpty()) {
            throw new IllegalArgumentException("La disponibilité ne peut pas être vide.");
        }
        if (reservation.getVehiculeId() <= 0) {
            throw new IllegalArgumentException("L'ID du véhicule doit être positif.");
        }
        if (reservation.getTrajetId() <= 0) {
            throw new IllegalArgumentException("L'ID du trajet doit être positif.");
        }
        if (reservation.getUserId() <= 0) {
            throw new IllegalArgumentException("L'ID de l'utilisateur doit être positif.");
        }

        // Vérifier si le véhicule, le trajet et l'utilisateur existent
        if (!vehiculeExists(reservation.getVehiculeId())) {
            throw new IllegalArgumentException("Le véhicule spécifié n'existe pas.");
        }
        if (!trajetExists(reservation.getTrajetId())) {
            throw new IllegalArgumentException("Le trajet spécifié n'existe pas.");
        }
        if (!userExists(reservation.getUserId())) {
            throw new IllegalArgumentException("L'utilisateur spécifié n'existe pas.");
        }

        String query = "UPDATE reservationtrajet SET disponibilite = ?, user_id = ?, vehicule_id = ?, trajet_id = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, reservation.getDisponibilite());
            statement.setInt(2, reservation.getUserId());
            statement.setInt(3, reservation.getVehiculeId());
            statement.setInt(4, reservation.getTrajetId());
            statement.setInt(5, reservation.getId());

            return statement.executeUpdate();
        }
    }

    @Override
    public int delete(ReservationTrajet reservation) throws SQLException {
        String query = "DELETE FROM reservationtrajet WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, reservation.getId());
            return statement.executeUpdate();
        }
    }

    @Override
    public List<ReservationTrajet> showAll() throws SQLException {
        List<ReservationTrajet> reservations = new ArrayList<>();
        String query = "SELECT * FROM reservationtrajet";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                ReservationTrajet reservation = new ReservationTrajet(
                        resultSet.getInt("id"),
                        resultSet.getString("disponibilite"),
                        resultSet.getInt("vehicule_id"),
                        resultSet.getInt("trajet_id"),
                        resultSet.getInt("user_id")
                );
                reservations.add(reservation);
            }
        }
        return reservations;
    }

    // Récupérer les réservations par ID de véhicule et ID de trajet
    public List<ReservationTrajet> getReservationsByVehiculeAndTrajet(int vehiculeId, int trajetId) throws SQLException {
        List<ReservationTrajet> reservations = new ArrayList<>();
        String query = "SELECT * FROM reservationtrajet WHERE vehicule_id = ? AND trajet_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, vehiculeId);
            statement.setInt(2, trajetId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    ReservationTrajet reservation = new ReservationTrajet(
                            resultSet.getInt("id"),
                            resultSet.getString("disponibilite"),
                            resultSet.getInt("vehicule_id"),
                            resultSet.getInt("trajet_id"),
                            resultSet.getInt("user_id")
                    );
                    reservations.add(reservation);
                }
            }
        }
        return reservations;
    }

    // Vérifier si un véhicule existe
    private boolean vehiculeExists(int vehiculeId) throws SQLException {
        String query = "SELECT id FROM vehicule WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, vehiculeId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next(); // Retourne true si le véhicule existe
            }
        }
    }

    // Vérifier si un trajet existe
    private boolean trajetExists(int trajetId) throws SQLException {
        String query = "SELECT id FROM trajet WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, trajetId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next(); // Retourne true si le trajet existe
            }
        }
    }

    // Vérifier si un utilisateur existe
    private boolean userExists(int userId) throws SQLException {
        String query = "SELECT id FROM utilisateur WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next(); // Retourne true si l'utilisateur existe
            }
        }
    }
}
