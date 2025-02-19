package tn.nexus.Services;
import tn.nexus.Entities.Evenement;
import tn.nexus.Utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EvenementServices implements CRUD<Evenement> {

    private Connection con = DBConnection.getInstance().getcon();
    private Statement st;
    private PreparedStatement ps;

    @Override
    public int insert(Evenement evenement) throws SQLException {

        // Requête d'insertion sans l'attribut id_evenement
        String req = "INSERT INTO evenement (titre, lieu, description, prix, date_debut, date_fin, image, heure) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        ps = con.prepareStatement(req, Statement.RETURN_GENERATED_KEYS);

        // Remplir les valeurs des placeholders avec les données de l'événement
        ps.setString(1, evenement.getTitre());
        ps.setString(2, evenement.getLieu());
        ps.setString(3, evenement.getDescription());
        ps.setDouble(4, evenement.getPrix());
        ps.setDate(5, Date.valueOf(evenement.getDateDebut()));
        ps.setDate(6, Date.valueOf(evenement.getDateFin()));
        ps.setString(7, evenement.getImage());
        ps.setTime(8, Time.valueOf(evenement.getHeure()));


        // Exécuter la requête et récupérer les clés générées
        int rowsAffected = ps.executeUpdate();

        // Si l'insertion a réussi, récupérer l'ID généré
        if (rowsAffected > 0) {
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    // Assigner l'ID généré à l'objet événement
                    evenement.setIdEvenement(generatedKeys.getInt(1)); // L'ID généré est dans la première colonne
                }
            }
        }

        return rowsAffected;

    }

    @Override
    public int update(Evenement evenement) throws SQLException {
        String req = "UPDATE evenement SET titre = ?, lieu = ?, description = ?, prix = ?, date_debut = ?, date_fin = ?, heure = ?, image = ? WHERE id_evenement = ?";

        try (PreparedStatement ps = con.prepareStatement(req)) {
            // Associer les valeurs des paramètres à l'objet Evenement
            ps.setString(1, evenement.getTitre());
            ps.setString(2, evenement.getLieu());
            ps.setString(3, evenement.getDescription());
            ps.setDouble(4, evenement.getPrix());
            ps.setDate(5, Date.valueOf(evenement.getDateDebut()));  // Convertir LocalDate en Date
            ps.setDate(6, Date.valueOf(evenement.getDateFin()));    // Convertir LocalDate en Date
            ps.setTime(7, Time.valueOf(evenement.getHeure()));      // Convertir LocalTime en Time
            ps.setString(8, evenement.getImage());
            ps.setInt(9, evenement.getIdEvenement());  // Utiliser l'ID pour identifier l'événement à mettre à jour

            // Exécuter la mise à jour
            return ps.executeUpdate();  // Retourne le nombre de lignes affectées
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    @Override
    public int delete(Evenement evenement) throws SQLException {
        String req = "DELETE FROM evenement WHERE id_evenement = ?"; // Requête pour supprimer l'événement par id
        try (PreparedStatement ps = con.prepareStatement(req)) {
            ps.setInt(1, evenement.getIdEvenement());
            return ps.executeUpdate(); // Exécute la requête de suppression et retourne le nombre de lignes affectées
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return 0; // Retourne 0 en cas d'erreur
        }
    }


    @Override
    public List<Evenement> showAll() throws SQLException {
        List<Evenement> events = new ArrayList<>();
        String req = "SELECT * FROM `evenement`";
        st = con.createStatement();
        ResultSet rs = st.executeQuery(req);

        while (rs.next()) {
            Evenement evenement = new Evenement();

            // Récupération des données à partir du ResultSet
            evenement.setIdEvenement(rs.getInt("id_evenement")); // Assurez-vous que les noms de colonne correspondent
            evenement.setTitre(rs.getString("titre"));

            // Conversion de la date en LocalDate
            Date dateDebut = rs.getDate("date_debut");
            if (dateDebut != null) {
                evenement.setDateDebut(dateDebut.toLocalDate());
            }

            Date dateFin = rs.getDate("date_fin");
            if (dateFin != null) {
                evenement.setDateFin(dateFin.toLocalDate());
            }

            // Récupération des autres attributs
            evenement.setPrix(rs.getDouble("prix"));
            evenement.setDescription(rs.getString("description"));
            evenement.setLieu(rs.getString("lieu"));

            // Conversion de l'heure en LocalTime (si applicable)
            Time heure = rs.getTime("heure");
            if (heure != null) {
                evenement.setHeure(heure.toLocalTime());
            }

            // Récupération de l'image (chemin ou URL)
            evenement.setImage(rs.getString("image"));

            // Ajout de l'événement à la liste
            events.add(evenement);
        }

        return events;
    }


    public Evenement getEvenementById(int idEvenement) throws SQLException {
        String query = "SELECT titre, date_debut FROM evenement WHERE id_evenement = ?";

        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, idEvenement);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Evenement(idEvenement, rs.getString("titre"), rs.getDate("date_debut").toLocalDate());
            }
        }

        return null;
    }
}
