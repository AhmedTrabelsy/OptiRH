package tn.nexus.services;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import tn.nexus.Entities.Demande;
import tn.nexus.Utils.DBConnection;

public class DemandeService implements CRUD<Demande> {

    private Connection cnx = DBConnection.getInstance().getCnx();
    private Statement st;
    private PreparedStatement ps;

    @Override
    public int insert(Demande demande) throws SQLException {
        String req = "INSERT INTO `demande`(`statut`, `date`, `description`, `utilisateur_id`, `offre_id`, `fichier_piece_jointe`) VALUES (?, ?, ?, ?, ?, ?)";

        ps = cnx.prepareStatement(req);
        ps.setString(1, Demande.Statut.EN_ATTENTE.name());
        ps.setTimestamp(2, demande.getDate());
        ps.setString(3, demande.getDescription());
        ps.setInt(4, demande.getUtilisateurId());
        ps.setInt(5, demande.getOffreId());
        ps.setString(6, demande.getFichierPieceJointe());




        return ps.executeUpdate();
    }

    @Override
    public int update(Demande demande) throws SQLException {
        String req = "UPDATE `demande` SET `statut` = ?, `date` = ?, `description` = ?, `utilisateur_id` = ?, `offre_id` = ?, `fichier_piece_jointe` = ? WHERE `id` = ?";

        ps = cnx.prepareStatement(req);
        ps.setString(1, demande.getStatut().name());
        ps.setTimestamp(2, demande.getDate());
        ps.setString(3, demande.getDescription());
        ps.setInt(4, demande.getUtilisateurId());
        ps.setInt(5, demande.getOffreId());
        ps.setString(6, demande.getFichierPieceJointe());
        ps.setInt(7, demande.getId());

        return ps.executeUpdate();
    }

    @Override
    public int delete(Demande demande) throws SQLException {
        String req = "DELETE FROM `demande` WHERE `id` = ?";

        ps = cnx.prepareStatement(req);
        ps.setInt(1, demande.getId());

        return ps.executeUpdate();
    }

    @Override
    public List<Demande> showAll() throws SQLException {
        List<Demande> temp = new ArrayList<>();
        String req = "SELECT * FROM `demande`";
        st = cnx.createStatement();
        ResultSet rs = st.executeQuery(req);

        while (rs.next()) {
            Demande d = new Demande();
            d.setId(rs.getInt("id"));
            d.setStatut(Demande.Statut.valueOf(rs.getString("statut")));
            d.setDate(rs.getTimestamp("date"));
            d.setDescription(rs.getString("description"));
            d.setUtilisateurId(rs.getInt("utilisateur_id"));
            d.setOffreId(rs.getInt("offre_id"));
            d.setFichierPieceJointe(rs.getString("fichier_piece_jointe"));

            temp.add(d);
        }
        return temp;
    }
}
