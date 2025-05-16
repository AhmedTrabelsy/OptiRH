package tn.nexus.Services;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

import tn.nexus.Entities.User;
import tn.nexus.Utils.DBConnection;
import tn.nexus.Utils.Enums.Role;

public class UserService implements CRUD<User> {

    private Connection cnx = DBConnection.getInstance().getConnection();
    private Statement st;
    private PreparedStatement ps;

    @Override
    public int insert(User user) throws SQLException {

        String req = "INSERT INTO User(nom, email, mot_de_passe, role, address, roles, created_at, is_verified) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        ps = cnx.prepareStatement(req);

        ps.setString(1, user.getNom());
        ps.setString(2, user.getEmail());
        ps.setString(3, BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        ps.setString(4, user.getRole().name());
        ps.setString(5, user.getAddress());
        ps.setString(6, "{\"roles\": [\"ROLE_USER\"]}");
        ps.setTimestamp(7, Timestamp.valueOf(java.time.LocalDateTime.now()));
        ps.setInt(8, 1);

        return ps.executeUpdate();
    }

    @Override
    public int update(User person) throws SQLException {
        String req = "UPDATE User SET nom = ?, email = ?, mot_de_passe = ?, role = ?, address = ? WHERE id = ?";

        ps = cnx.prepareStatement(req);

        ps.setString(1, person.getNom());
        ps.setString(2, person.getEmail());
        ps.setString(3, BCrypt.hashpw(person.getPassword(), BCrypt.gensalt()));
        ps.setString(4, person.getRole().name());
        ps.setString(5, person.getAddress());
        ps.setInt(6, person.getId());

        System.out.println(ps);

        return ps.executeUpdate();
    }

    @Override
    public int delete(User person) throws SQLException {
        String req = "DELETE FROM User WHERE id = ?";

        ps = cnx.prepareStatement(req);

        ps.setInt(1, person.getId());

        return ps.executeUpdate();
    }

    @Override
    public List<User> showAll() throws SQLException {
        List<User> temp = new ArrayList<>();

        String req = "SELECT * FROM `user`";

        st = cnx.createStatement();

        ResultSet rs = st.executeQuery(req);

        while (rs.next()) {
            User p = new User();

            p.setId(rs.getInt("id"));
            p.setNom(rs.getString("nom"));
            p.setEmail(rs.getString("email"));
            p.setRole(rs.getString("role") == null ? Role.Candidat : Role.valueOf(rs.getString("role")));
            String address = rs.getString("address");
            if (address == null) {
                address = "Tunis, 1234 Ariana St.";
            }
            p.setAddress(address);

            temp.add(p);
        }

        return temp;
    }
    public User getUserByEmailAndRole(String email, Role role) throws SQLException {
        String req = "SELECT * FROM User WHERE email = ? AND role = ?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setString(1, email);
            ps.setString(2, role.name()); // Convertir le rôle en chaîne de caractères
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setNom(rs.getString("nom"));
                    user.setEmail(rs.getString("email"));
                    user.setRole(Role.valueOf(rs.getString("role"))); // Convertir la chaîne en enum Role
                    user.setAddress(rs.getString("address"));
                    return user;
                }
            }
        }
        return null; // Aucun utilisateur trouvé avec cet email et ce rôle
    }
    public User getUserByEmail(String email) throws SQLException {
        String req = "SELECT * FROM user WHERE email = ?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setNom(rs.getString("nom"));
                    user.setEmail(rs.getString("email"));
                    user.setPassword(rs.getString("mot_de_passe"));
                    user.setRole(Role.valueOf(rs.getString("role")));
                    user.setAddress(rs.getString("address"));
                    return user;
                }
            }
        }
        return null; // Aucun utilisateur trouvé avec cet email
    }
    public String getUserNameById(int userId) throws SQLException {
        String query = "SELECT nom FROM user WHERE id = ?";

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

    public User getUserById(int userId) throws SQLException {
        System.out.println("Recherche de l'utilisateur avec l'ID : " + userId); // Log pour débogage
        String query = "SELECT * FROM user WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setNom(rs.getString("nom"));
                    user.setEmail(rs.getString("email"));
                    user.setRole(Role.valueOf(rs.getString("role")));
                    user.setAddress(rs.getString("address"));
                    System.out.println("Utilisateur trouvé : " + user.getNom()); // Log pour débogage
                    return user;
                } else {
                    System.out.println("Aucun utilisateur trouvé avec l'ID : " + userId); // Log pour débogage
                }
            }
        }
        return null; // Retourne null si l'utilisateur n'est pas trouvé
    }


    public User getUserById2(int userId) throws SQLException {
        String query = "SELECT * FROM user WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("id"),
                            rs.getString("nom"),
                            rs.getString("email"),
                            rs.getString("mot_de_passe"),
                            Role.valueOf(rs.getString("role")),
                            rs.getString("address")
                    );
                }
            }
        }
        return null; // Retourne null si l'utilisateur n'est pas trouvé
    }
}
