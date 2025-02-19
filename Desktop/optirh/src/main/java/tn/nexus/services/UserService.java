package tn.nexus.services;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import tn.nexus.Entities.User;
import tn.nexus.Utils.DBConnection;
import tn.nexus.services.CRUD;

public class UserService implements CRUD<User> {

    private Connection cnx = DBConnection.getInstance().getcon();
    private Statement st;
    private PreparedStatement ps;

    @Override
    public int insert(User user) throws SQLException {

        String req = "INSERT INTO Users(nom, email, motDePasse, role, address) VALUES (?, ?, ?, ?, ?)";

        ps = cnx.prepareStatement(req);

        ps.setString(1, user.getNom());
        ps.setString(2, user.getEmail());
        ps.setString(3, user.getMotDePasse());
        ps.setString(4, user.getRole());
        ps.setString(5, user.getAddress());

        return ps.executeUpdate();
    }

    @Override
    public int update(User person) throws SQLException {
        String req = "UPDATE Users SET nom = ?, email = ?, motDePasse = ?, role = ?, address = ? WHERE id = ?";

        ps = cnx.prepareStatement(req);

        ps.setString(1, person.getNom());
        ps.setString(2, person.getEmail());
        ps.setString(3, person.getMotDePasse());
        ps.setString(4, person.getRole());
        ps.setString(5, person.getAddress());
        ps.setInt(6, person.getId());

        return ps.executeUpdate();
    }

    @Override
    public int delete(User person) throws SQLException {
        String req = "DELETE FROM Users WHERE id = ?";

        ps = cnx.prepareStatement(req);

        ps.setInt(1, person.getId());

        return ps.executeUpdate();
    }

    @Override
    public List<User> showAll() throws SQLException {
        List<User> temp = new ArrayList<>();

        String req = "SELECT * FROM `users`";

        st = cnx.createStatement();

        ResultSet rs = st.executeQuery(req);

        while (rs.next()) {
            User p = new User();

            p.setId(rs.getInt("id"));
            p.setNom(rs.getString("nom"));
            p.setEmail(rs.getString("email"));
            p.setMotDePasse(rs.getString("motDePasse"));
            p.setRole(rs.getString("role"));
            p.setAddress(rs.getString("address"));

            temp.add(p);
        }

        return temp;
    }
    public String getUserNameById(int userId) throws SQLException {
        String query = "SELECT nom FROM users WHERE id = ?";

        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("nom"); // Récupère le nom de l'utilisateur
                }
            }
        }
        return null; // Retourne null si aucun utilisateur n'est trouvé
    }

}