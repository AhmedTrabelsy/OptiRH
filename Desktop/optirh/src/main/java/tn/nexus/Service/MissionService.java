package tn.nexus.Service;

import tn.nexus.Entities.Mission;
import tn.nexus.Utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MissionService  implements CRUD<Mission> {
        private Connection connection;

        public MissionService() {
            connection = DBConnection.getInstance().getConnection() ;
        }

        @Override
        public int insert(Mission mission) throws SQLException {
            String query = "INSERT INTO mission (titre, description, statut, isPresent, projetId) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, mission.getTitre());
            ps.setString(2, mission.getDescription());
            ps.setString(3, mission.getStatut());
            ps.setBoolean(4, mission.isPresent());
            ps.setInt(5, mission.getProjetId());

            int rowsInserted = ps.executeUpdate();
            if (rowsInserted > 0) {
                return rowsInserted;
            }
            return -1;
        }

        @Override
        public int update(Mission mission) throws SQLException {
            String query = "UPDATE mission SET titre = ?, description = ?, statut = ?, isPresent = ?, projetId = ? WHERE id = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, mission.getTitre());
            ps.setString(2, mission.getDescription());
            ps.setString(3, mission.getStatut());
            ps.setBoolean(4, mission.isPresent());
            ps.setInt(5, mission.getProjetId());
            ps.setInt(6, mission.getId());

            return ps.executeUpdate();
        }

        @Override
        public int delete(Mission mission) throws SQLException {
            String query = "DELETE FROM mission WHERE id = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, mission.getId());

            return ps.executeUpdate();
        }

        @Override
        public List<Mission> showAll() throws SQLException {
            List<Mission> missions = new ArrayList<>();
            String query = "SELECT * FROM mission";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                Mission mission = new Mission(
                        rs.getInt("id"),
                        rs.getString("titre"),
                        rs.getString("description"),
                        rs.getString("statut"),
                        rs.getBoolean("isPresent"),
                        rs.getInt("projetId")
                );
                missions.add(mission);
            }
            return missions;
        }
    }


