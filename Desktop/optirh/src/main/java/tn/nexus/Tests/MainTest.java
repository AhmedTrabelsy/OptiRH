package tn.nexus.Tests;

import tn.nexus.Entities.Evenement;
import tn.nexus.Services.EvenementServices;
import tn.nexus.Utils.DBConnection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

public class MainTest {
    public static void main(String[] args) {
        /*DBConnection db = DBConnection.getInstance();
        Evenement e1 = new Evenement("hadhra", "carthage","blablabla", 30.0, LocalDate.of(2003, 2, 1), LocalDate.of(2003, 2, 1), "llll", LocalTime.of(10, 10));
        Evenement e2 = new Evenement("hadhra", "carthage","blablabla", 30.0, LocalDate.of(2003, 2, 1), LocalDate.of(2003, 2, 1), "llll", LocalTime.of(10, 10));
        EvenementServices es = new EvenementServices();

        try {
            es.delete(e1);
            //System.out.println(es.showAll());

            //es.delete(e2);

            /*if (result > 0) {
                System.out.println("L'événement " + e2.getTitre() + " a été supprimé avec succès.");
            } else {
                System.out.println("Aucun événement trouvé avec cet ID.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }*/


    }
    
}
