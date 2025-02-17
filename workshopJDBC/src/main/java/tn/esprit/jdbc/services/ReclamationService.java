package tn.esprit.jdbc.services;

import tn.esprit.jdbc.entities.Reclamation;
import tn.esprit.jdbc.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReclamationService implements CRUD<Reclamation> {

    private Connection cnx = MyDatabase.getInstance().getCnx();

    @Override
    public int insert(Reclamation reclamation) throws SQLException {
        String req = "INSERT INTO reclamation (description, date, status, utilisateur_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = cnx.prepareStatement(req, Statement.RETURN_GENERATED_KEYS)) { // Add RETURN_GENERATED_KEYS
            ps.setString(1, reclamation.getDescription());
            ps.setDate(2, new java.sql.Date(reclamation.getDate().getTime()));
            ps.setString(3, reclamation.getStatus());
            ps.setInt(4, 1);

            int rowsAffected = ps.executeUpdate();

            // Retrieve the generated key (ID) after the insert
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    reclamation.setId(rs.getInt(1)); // Set the generated ID to the reclamation object
                }
            }

            return rowsAffected;
        }
    }


    @Override
    public int update(Reclamation reclamation) throws SQLException {
        String req = "UPDATE reclamation SET description = ?, date = ?, status = ?, utilisateur_id = ? WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setString(1, reclamation.getDescription());
            ps.setDate(2, new java.sql.Date(reclamation.getDate().getTime()));
            ps.setString(3, reclamation.getStatus());
            ps.setInt(4, 1);
            ps.setInt(5, reclamation.getId());
            return ps.executeUpdate();
        }
    }

    @Override
    public int delete(Reclamation reclamation) throws SQLException {
        String req = "DELETE FROM reclamation WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, reclamation.getId());
            return ps.executeUpdate();
        }
    }

    @Override
    public List<Reclamation> showAll() throws SQLException {
        List<Reclamation> reclamations = new ArrayList<>();
        String req = "SELECT * FROM reclamation";  // Modification pour récupérer toutes les réclamations

        try (Statement st = cnx.createStatement(); ResultSet rs = st.executeQuery(req)) {
            while (rs.next()) {
                Reclamation reclamation = new Reclamation(
                        rs.getInt("id"),
                        rs.getString("description"),
                        rs.getString("status"),
                        rs.getDate("date"),
                        rs.getInt("utilisateur_id")
                );
                reclamations.add(reclamation);
            }
        }

        // Débogage
        System.out.println("Nombre de réclamations récupérées : " + reclamations.size());

        return reclamations;
    }


}