package tn.esprit.jdbc.tests;

import tn.esprit.jdbc.entities.Reclamation;
import tn.esprit.jdbc.utils.MyDatabase;
import tn.esprit.jdbc.services.ReclamationService;

import java.sql.Date; // Import pour java.sql.Date
import java.sql.SQLException;

public class MainTest {

    public static void main(String[] args) {
        MyDatabase m1 = MyDatabase.getInstance();

        // 🔹 Correction : Conversion de String en java.sql.Date
        Reclamation p1 = new Reclamation("Hbib", "Khamouma", Date.valueOf("2024-03-12"), 24);
        Reclamation p2 = new Reclamation("Amine", "Jery", Date.valueOf("2024-04-12"), 24);

        ReclamationService reclamationService = new ReclamationService();

        try {
            reclamationService.insert(p1);
            reclamationService.insert(p2);

            System.out.println(reclamationService.showAll());

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
