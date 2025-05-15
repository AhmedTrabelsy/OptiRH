package tn.nexus.Entities.Evenement;

public enum ModaliteEvenement {

    EN_LIGNE("En ligne"),
    PRESENTIEL("Présentiel");

    private final String label;

    ModaliteEvenement(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static ModaliteEvenement fromString(String text) {
        for (ModaliteEvenement m : ModaliteEvenement.values()) {
            if (m.label.equalsIgnoreCase(text)) {
                return m;
            }
        }
        throw new IllegalArgumentException("Modalité inconnue : " + text);
    }
}
