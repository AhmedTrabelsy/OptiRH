package tn.nexus.services;

import tn.nexus.Entities.Trajet;
import tn.nexus.Utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrajetService implements CRUD<Trajet> {
    private Connection con = DBConnection.getInstance().getcon();

    @Override
    public int insert(Trajet trajet) throws SQLException {
        String req = "INSERT INTO trajet (disponibilite, type, station) VALUES (?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(req, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, trajet.getDisponibilite());
            ps.setString(2, trajet.getType());
            ps.setString(3, trajet.getStation());

            int result = ps.executeUpdate();

            // Récupérer l'ID généré
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    trajet.setId(rs.getInt(1));
                }
            }

            return result;
        }
    }

    @Override
    public int update(Trajet trajet) throws SQLException {
        String req = "UPDATE trajet SET disponibilite = ?, type = ?, station = ? WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(req)) {
            ps.setString(1, trajet.getDisponibilite());
            ps.setString(2, trajet.getType());
            ps.setString(3, trajet.getStation());
            ps.setInt(4, trajet.getId());

            return ps.executeUpdate();
        }
    }

    @Override
    public int delete(Trajet trajet) throws SQLException {
        String req = "DELETE FROM trajet WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(req)) {
            ps.setInt(1, trajet.getId());
            return ps.executeUpdate();
        }
    }

    @Override
    public List<Trajet> showAll() throws SQLException {
        List<Trajet> trajets = new ArrayList<>();
        String req = "SELECT * FROM trajet";

        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(req)) {
            while (rs.next()) {
                Trajet trajet = new Trajet(
                        rs.getInt("id"),
                        rs.getString("disponibilite"),
                        rs.getString("type"),
                        rs.getString("station")
                );
                trajets.add(trajet);
            }
        }
        return trajets;
    }
}