package tn.nexus.Service;

import tn.nexus.Entities.Mission;
import tn.nexus.Utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MissionService implements CRUD <Mission> {
    private Connection cnx = DBConnection.getInstance().getConnection();
    private Statement st ;
    private PreparedStatement ps ;

    @Override

    public int insert(Mission mission) throws SQLException {

        String req = "INSERT INTO `mission`(`titre`, `description`, `statut`,`isPresent` , `projet_id`)  VALUES (?,?,?,?,?)" ;

        ps = cnx.prepareStatement(req);

        ps.setString(1, mission.getTitre());
        ps.setString(2,mission.getDescription());
        ps.setString(3, mission.getStatut());
        ps.setBoolean(4,mission.isPresent());
        ps.setInt(5, mission.getProjetId());

        return ps.executeUpdate();
    }
    @Override
    public int update(Mission mission) throws SQLException {
        String query = "UPDATE mission SET titre = ?, description = ?, statut = ?, isPresent = ? WHERE id = ?";

        int rowsUpdated;
        try (
                PreparedStatement statement = cnx.prepareStatement(query)) {

            statement.setString(1, mission.getTitre());
            statement.setString(2, mission.getDescription());
            statement.setString(3, mission.getStatut());
            statement.setBoolean(4, mission.isPresent());
            statement.setInt(5, mission.getId());

            rowsUpdated = statement.executeUpdate();
            System.out.println(rowsUpdated + " ligne(s) mise(s) à jour.");

            if (rowsUpdated == 0) {
                System.out.println(" Aucune ligne mise à jour. Vérifie si l'ID est correct.");
            }
        }
        return rowsUpdated;
    }


    @Override
    public int delete(Mission mission) throws SQLException {
        String query = "DELETE FROM mission WHERE id = ?";
        PreparedStatement ps = cnx.prepareStatement(query);
        ps.setInt(1, mission.getId()); // Utiliser l'ID de la mission pour la suppression

        return ps.executeUpdate();
    }

    @Override
    public List<Mission> showAll() throws SQLException {
            List<Mission> missions = new ArrayList<Mission>();
            st= cnx.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM `mission`");
            while (rs.next()) {
                Mission mission = new Mission();
                mission.setTitre(rs.getString("titre"));
                mission.setDescription(rs.getString("description"));
                mission.setStatut(rs.getString("statut"));
                mission.setPresent(rs.getBoolean("isPresent"));
                mission.setProjetId(rs.getInt("projet_id"));
                missions.add(mission);
            }
                     return missions;
    }
    public Mission getById(int id) {
        Mission mission = null;
        String sql = "SELECT * FROM mission WHERE id = ?";

        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                mission = new Mission();
                mission.setId(rs.getInt("id"));
                mission.setTitre(rs.getString("titre"));
                mission.setDescription(rs.getString("description"));
                mission.setStatut(rs.getString("statut"));
                mission.setPresent(rs.getBoolean("is_present"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return mission;
    }

    public List<Mission> getByProjetId(int projetId) throws SQLException {
        List<Mission> missions = new ArrayList<>();
        String query = "SELECT * FROM mission WHERE projet_id = ?";

        PreparedStatement ps = cnx.prepareStatement(query);
        ps.setInt(1, projetId);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Mission mission = new Mission();
            mission.setId(rs.getInt("id"));
            mission.setTitre(rs.getString("titre"));
            mission.setDescription(rs.getString("description"));
            mission.setStatut(rs.getString("statut"));
            mission.setPresent(rs.getBoolean("isPresent"));
            mission.setProjetId(rs.getInt("projet_id"));

            missions.add(mission);
        }

        return missions;
    }



}
