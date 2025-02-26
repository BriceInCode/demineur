package org.example.models;

/**
 * Représente une coordonnée dans un espace bidimensionnel.
 * Chaque coordonnée est composée de deux valeurs : x et y.
 */
public class Coordinate {

    // Coordonnée x
    private int x;

    // Coordonnée y
    private int y;

    /**
     * Constructeur pour initialiser une coordonnée avec des valeurs x et y.
     *
     * @param x La coordonnée x.
     * @param y La coordonnée y.
     */
    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Constructeur pour initialiser une coordonnée en ajustant les valeurs
     * x et y par rapport à une coordonnée existante.
     *
     * @param old La coordonnée de base à partir de laquelle les nouvelles valeurs sont calculées.
     * @param x Le décalage à appliquer sur la coordonnée x.
     * @param y Le décalage à appliquer sur la coordonnée y.
     */
    public Coordinate(Coordinate old, int x, int y) {
        this(old.x + x, old.y + y);
    }

    /**
     * Récupère la valeur de la coordonnée x.
     *
     * @return La coordonnée x.
     */
    public int getX() {
        return x;
    }

    /**
     * Récupère la valeur de la coordonnée y.
     *
     * @return La coordonnée y.
     */
    public int getY() {
        return y;
    }

    /**
     * Vérifie si cette coordonnée est égale à une autre.
     * Deux coordonnées sont considérées égales si leurs valeurs x et y sont identiques.
     *
     * @param other L'autre objet à comparer.
     * @return true si les coordonnées sont égales, false sinon.
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) return true; // Si c'est le même objet
        if (other == null || getClass() != other.getClass()) return false; // Vérifie la classe et nullité
        Coordinate that = (Coordinate) other; // Conversion de l'objet en Coordinate
        return x == that.x && y == that.y; // Comparaison des coordonnées x et y
    }

    /**
     * Calcule un code de hachage pour cette coordonnée.
     * Le code de hachage est utilisé dans des structures de données comme les HashMap.
     *
     * @return Le code de hachage pour cette coordonnée.
     */
    @Override
    public int hashCode() {
        return 31 * x + y; // Calcul du code de hachage basé sur x et y
    }

    /**
     * Retourne une représentation sous forme de chaîne de caractères de la coordonnée.
     *
     * @return Une chaîne représentant la coordonnée sous la forme "(x/y)".
     */
    @Override
    public String toString() {
        return String.format("(%d/%d)", x, y); // Format "(x/y)"
    }
}
