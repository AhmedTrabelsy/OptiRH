package tn.nexus.Entities.Evenement;

public enum TypeEvenement {

    RH("Ressources Humaines"),
    MARKETING("Marketing"),
    TECH_INNOVATION("Technologies et Innovation"),
    SOFT_SKILLS("Soft Skills"),
    FINANCE("Finance"),
    COMMERCE("Commerce"),
    MANAGEMENT("Management"),
    LOISIR("Loisir");

    private final String label;

    TypeEvenement(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static TypeEvenement fromString(String text) {
        for (TypeEvenement c : TypeEvenement.values()) {
            if (c.label.equalsIgnoreCase(text)) {
                return c;
            }
        }
        throw new IllegalArgumentException("Catégorie inconnue : " + text);
    }
}
