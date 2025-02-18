package tn.nexus.services;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import tn.nexus.Entities.Demande;
import tn.nexus.Utils.DBConnection;

public class DemandeService implements CRUD<Demande>{

    private Connection cnx = DBConnection.getInstance().getCnx();
    private Statement st ;
    private PreparedStatement ps ;

    @Override
    public int insert(Demande demande) throws SQLException {
        String req = "INSERT INTO `demande`(`status`, `date`, `description`, `utilisateur_id`) VALUES (?, ?, ?,?)";


        ps = cnx.prepareStatement(req);

        ps.setString(1, demande.getStatus());
        ps.setString(2, demande.getDate());
        ps.setString(3, demande.getDescription());
        ps.setInt(4, demande.getUtilistaeurId());

        return ps.executeUpdate();    }

    @Override
    public int update(Demande demande) throws SQLException {
        String req = "UPDATE `demande` SET `status` = ?, `date` = ?, `description` = ?, `utilisateur_id` = ? WHERE `id` = ?";

        ps = cnx.prepareStatement(req);
        ps.setString(1, demande.getStatus());
        ps.setString(2, demande.getDate());
        ps.setString(3, demande.getDescription());
        ps.setInt(4, demande.getUtilistaeurId());
        ps.setInt(5, demande.getId());

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
            d.setId(rs.getInt(1));
            d.setStatus(rs.getString(2));
            d.setDate(rs.getDate(3));
            d.setDescription(rs.getString(4));
            d.setUtilistaeurId(rs.getInt(5));

            temp.add(d);
        }
        return temp;
    }



}
