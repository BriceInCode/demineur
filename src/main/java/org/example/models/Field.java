package org.example.models;

/**
 * Représente un champ de la grille du jeu de démineur.
 * Chaque champ a une coordonnée, une mine potentielle, un état d'ouverture,
 * un compte de mines voisines et un état de drapeau.
 */
public class Field {

    // Coordonnée du champ dans la grille
    private Coordinate coordinate;

    // Indique si ce champ contient une mine
    private boolean hasMine;

    // Indique si ce champ est ouvert ou non
    private boolean isOpened;

    // Compte du nombre de mines voisines
    private int neighbourMineCount;

    // Indique si un drapeau est posé sur ce champ
    private boolean hasFlag;

    /**
     * Constructeur pour initialiser un champ avec une coordonnée.
     * Par défaut, le champ ne contient pas de mine, n'est pas ouvert,
     * n'a pas de drapeau et n'a pas de mines voisines.
     *
     * @param coord La coordonnée du champ.
     */
    public Field(Coordinate coord) {
        this.coordinate = coord;
        this.hasMine = false;
        this.isOpened = false;
        this.neighbourMineCount = 0;
        this.hasFlag = false;
    }

    /**
     * Récupère la coordonnée du champ.
     *
     * @return La coordonnée du champ.
     */
    public Coordinate getCoordinate() {
        return coordinate;
    }

    /**
     * Vérifie si un drapeau est posé sur ce champ.
     *
     * @return true si un drapeau est posé, false sinon.
     */
    public boolean hasFlag() {
        return hasFlag;
    }

    /**
     * Modifie l'état du drapeau sur ce champ.
     *
     * @param flag true si un drapeau doit être posé, false sinon.
     */
    public void setHasFlag(boolean flag) {
        this.hasFlag = flag;
    }

    /**
     * Vérifie si ce champ contient une mine.
     *
     * @return true si le champ contient une mine, false sinon.
     */
    public boolean hasMine() {
        return hasMine;
    }

    /**
     * Modifie l'état de la mine sur ce champ.
     *
     * @param mine true si une mine doit être posée, false sinon.
     */
    public void setHasMine(boolean mine) {
        this.hasMine = mine;
    }

    /**
     * Vérifie si ce champ est ouvert.
     *
     * @return true si le champ est ouvert, false sinon.
     */
    public boolean isOpened() {
        return isOpened;
    }

    /**
     * Modifie l'état d'ouverture du champ.
     *
     * @param opened true si le champ doit être ouvert, false sinon.
     */
    public void setOpened(boolean opened) {
        this.isOpened = opened;
    }

    /**
     * Récupère le nombre de mines voisines de ce champ.
     *
     * @return Le nombre de mines voisines.
     */
    public int getNeighbourMineCount() {
        return neighbourMineCount;
    }

    /**
     * Modifie le nombre de mines voisines de ce champ.
     * Si la valeur est inférieure à 0, une exception est lancée.
     *
     * @param count Le nouveau nombre de mines voisines.
     * @throws IllegalArgumentException Si le nombre de mines voisines est inférieur à 0.
     */
    public void setNeighbourMineCount(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("Count cannot be negative");
        }
        this.neighbourMineCount = count;
    }
}
