package tn.esprit.jdbc.tests;

import tn.esprit.jdbc.entities.Reclamation;
import tn.esprit.jdbc.utils.MyDatabase;
import tn.esprit.jdbc.services.ReclamationService;
import java.sql.SQLException;

public class MainTest {

    public static void main(String[] args) {
        MyDatabase m1 = MyDatabase.getInstance();

        Reclamation p1 = new Reclamation("Hbib",
                "Khamouma", "2024-03-12", 24);
        Reclamation p2 = new Reclamation("Amine", "Jery", "2024-04-12", 24);


        ReclamationService reclamationService = new ReclamationService();

        try {
            reclamationService.insert(p1);
            reclamationService.insert1(p2);

            System.out.println(reclamationService.showAll());

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


    }
}
