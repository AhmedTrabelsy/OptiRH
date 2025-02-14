package tn.nexus.Service;

import tn.nexus.Entities.Projet;
import tn.nexus.Utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjetService implements CRUD<Projet> {
    private Connection connection;

    public ProjetService() {
        connection = DBConnection.getInstance().getConnection();
    }

    @Override
    public int insert(Projet projet) throws SQLException {
        String query = "INSERT INTO projet (type, description, status, utilisateurId) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, projet.getType());
        ps.setString(2, projet.getDescription());
        ps.setString(3, projet.getStatus());
        ps.setInt(4, projet.getUtilisateurId());

        int rowsInserted = ps.executeUpdate();
        if (rowsInserted > 0) {
          return rowsInserted ;
        }
        return -1;
    }

    @Override
    public int update(Projet projet) throws SQLException {
        String query = "UPDATE projet SET type = ?, description = ?, status = ?, utilisateurId = ? WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, projet.getType());
        ps.setString(2, projet.getDescription());
        ps.setString(3, projet.getStatus());
        ps.setInt(4, projet.getUtilisateurId());
        ps.setInt(5, projet.getId());

        return ps.executeUpdate();
    }

    @Override
    public int delete(Projet projet) throws SQLException {
        String query = "DELETE FROM projet WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, projet.getId());

        return ps.executeUpdate();
    }

    @Override
    public List<Projet> showAll() throws SQLException {
        List<Projet> projets = new ArrayList<>();
        String query = "SELECT * FROM projet";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(query);

        while (rs.next()) {
            Projet projet = new Projet(
                    rs.getInt("id"),
                    rs.getString("type"),
                    rs.getString("description"),
                    rs.getString("status"),
                    rs.getInt("utilisateurId")
            );
            projets.add(projet);
        }
        return projets;
    }
}