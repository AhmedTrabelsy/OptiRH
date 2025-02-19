
package tn.nexus.services;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import tn.nexus.Entities.Offre;
import tn.nexus.Utils.DBConnection;

    public class OffreService implements CRUD<Offre> {

        private Connection cnx = DBConnection.getInstance().getCnx();
        private PreparedStatement ps;
        private Statement st;

        @Override
        public int insert(Offre offre) throws SQLException {
            String req = "INSERT INTO `offre`(`poste`, `description`, `statut`, `date_creation`) VALUES (?, ?, ?, ?)";
            ps = cnx.prepareStatement(req);
            ps.setString(1, offre.getPoste());
            ps.setString(2, offre.getDescription());
            ps.setString(3, offre.getStatut());
            ps.setTimestamp(4, new java.sql.Timestamp(System.currentTimeMillis()));

            return ps.executeUpdate();
        }

        @Override
        public int update(Offre offre) throws SQLException {
            String req = "UPDATE `offre` SET `poste` = ?, `description` = ?, `statut` = ?, `date_creation` = ? WHERE `id` = ?";
            ps = cnx.prepareStatement(req);

            // Remplir les valeurs des colonnes
            ps.setString(1, offre.getPoste());
            ps.setString(2, offre.getDescription());
            ps.setString(3, offre.getStatut());

            // Convertir LocalDateTime en java.sql.Timestamp
            ps.setTimestamp(4, java.sql.Timestamp.valueOf(offre.getDateCreation()));  // LocalDateTime à Timestamp

            // Définir l'ID de l'offre à mettre à jour
            ps.setInt(5, offre.getId());

            return ps.executeUpdate();
        }

        @Override
        public int delete(Offre offre) throws SQLException {
            String req = "DELETE FROM `offre` WHERE `id` = ?";
            ps = cnx.prepareStatement(req);
            ps.setInt(1, offre.getId());

            return ps.executeUpdate();
        }

        @Override
        public List<Offre> showAll() throws SQLException {
            List<Offre> offres = new ArrayList<>();
            String req = "SELECT * FROM offre";
            st = cnx.createStatement();
            ResultSet rs = st.executeQuery(req);

            while (rs.next()) {
                Offre offre = new Offre();
                offre.setId(rs.getInt("id"));
                offre.setPoste(rs.getString("poste"));
                offre.setDescription(rs.getString("description"));
                offre.setStatut(rs.getString("statut"));

                // Vérifier que la requête retourne des données
                System.out.println("Offre récupérée : " + offre);

                // Utiliser rs.getTimestamp() pour les champs de type TIMESTAMP
                Timestamp timestamp = rs.getTimestamp("date_creation");

                // Convertir Timestamp en LocalDateTime
                if (timestamp != null) {
                    offre.setDateCreation(timestamp.toLocalDateTime());
                }
                offres.add(offre);
            }
            return offres;
        }

        public List<Offre> getAllOffres() {
            List<Offre> offres = new ArrayList<>();
            String req = "SELECT id, poste, description FROM offre WHERE statut = 'Disponible'"; // Filtrer les offres actives

            try (Statement st = cnx.createStatement();
                 ResultSet rs = st.executeQuery(req)) {

                while (rs.next()) {
                    Offre offre = new Offre();
                    offre.setId(rs.getInt("id"));
                    offre.setPoste(rs.getString("poste"));
                    offre.setDescription(rs.getString("description"));
                    offres.add(offre);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
            return offres;
        }

    }


