package tn.esprit.jdbc.services;

import tn.esprit.jdbc.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import tn.esprit.jdbc.entities.Reclamation;

public class ReclamationService implements CRUD<Reclamation> {

    private Connection cnx = MyDatabase.getInstance().getCnx();
    private Statement st ;
    private PreparedStatement ps ;

    @Override
    public int insert(Reclamation reclamation) throws SQLException {

        String req = "INSERT INTO `reclamation`(`Description`, `Date`, `Status`, `utilisateur_id`) VALUES ('"
                + reclamation.getDescription() + "', '"
                + new java.sql.Date(reclamation.getDate().getTime()) + "', '"
                + reclamation.getStatus() + "', "
                + reclamation.getUtilisateurId() + ")";

        st = cnx.createStatement();

        return st.executeUpdate(req);
    }

    public int insert1(Reclamation reclamation) throws SQLException {

        String req = "INSERT INTO `reclamation`(`Description`, `Date`, `Status`, `utilisateur_id`) VALUES (?, ?, ?, ?)";

        ps = cnx.prepareStatement(req);
        ps.setString(1, reclamation.getDescription());
        ps.setDate(2, reclamation.getDate());
        ps.setString(3, reclamation.getStatus());
        ps.setInt(4, reclamation.getUtilisateurId());

        return ps.executeUpdate();
    }

    @Override
    public int update(Reclamation reclamation) throws SQLException{
        return 0;
    }

    @Override
    public int delete(Reclamation reclamation) throws SQLException{
        return 0;
    }

    @Override
    public List<Reclamation> showAll() throws SQLException{
        List<Reclamation> temp = new ArrayList<>();

        String req = "SELECT * FROM `reclamation`";

        st = cnx.createStatement();

        ResultSet rs = st.executeQuery(req);

        while (rs.next()){
            Reclamation p = new Reclamation();
            p.setId(rs.getInt(1));
            p.setDescription(rs.getString("Description"));
            p.setDate(rs.getDate("Date"));
            p.setStatus(rs.getString("Status"));
            p.setUtilisateurId(rs.getInt("utilisateur_id"));

            temp.add(p);
        }

        return temp;
    }
}
