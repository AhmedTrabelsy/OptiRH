package tn.nexus.Entities;

public class Trajet {

        private int id;
        private String disponibilite;
        private String type;
        private String station;

        public Trajet() {
        }

        public Trajet(int id, String disponibilite, String type, String station) {
            this.id = id;
            this.disponibilite = disponibilite;
            this.type = type;
            this.station = station;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getDisponibilite() {
            return disponibilite;
        }

        public void setDisponibilite(String disponibilite) {
            this.disponibilite = disponibilite;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getStation() {
            return station;
        }

        public void setStation(String station) {
            this.station = station;
        }

        @Override
        public String toString() {
            return "Reservation_Evenement{" +
                    "id=" + id +
                    ", disponibilite=" + disponibilite +
                    ", type=" + type +
                    ", station=" + station +
                    '}';
        }
}
