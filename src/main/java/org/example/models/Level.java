package org.example.models;

/**
 * Classe representant un niveau de jeu avec une largeur, une hauteur,
 * un nombre de mines et un label pour identifier le niveau.
 */
public class Level {
    private int width;
    private int height;
    private int mineCount;
    private String label;

    /**
     * Constructeur principal permettant de definir un niveau avec un label specifique.
     *
     * @param width Largeur du niveau
     * @param height Hauteur du niveau
     * @param mineCount Nombre de mines presentes dans le niveau
     * @param label Nom du niveau
     */
    public Level(int width, int height, int mineCount, String label) {
        this.width = width;
        this.height = height;
        this.mineCount = mineCount;
        this.label = label;
    }

    /**
     * Constructeur alternatif qui definit un niveau personnalise avec le label "Custom".
     *
     * @param width Largeur du niveau
     * @param height Hauteur du niveau
     * @param mineCount Nombre de mines presentes dans le niveau
     */
    public Level(int width, int height, int mineCount) {
        this(width, height, mineCount, "Custom");
    }

    /**
     * Obtient la hauteur du niveau.
     *
     * @return Hauteur du niveau
     */
    public int getHeight() {
        return height;
    }

    /**
     * Obtient la largeur du niveau.
     *
     * @return Largeur du niveau
     */
    public int getWidth() {
        return width;
    }

    /**
     * Obtient le nombre de mines du niveau.
     *
     * @return Nombre de mines
     */
    public int getMineCount() {
        return mineCount;
    }

    /**
     * Obtient le label du niveau.
     *
     * @return Nom du niveau
     */
    public String getLabel() {
        return label;
    }

    /**
     * Retourne une representation textuelle du niveau.
     *
     * @return Chaine de caracteres representant le niveau
     */
    @Override
    public String toString() {
        return String.format("%s :(%dx%d) %d mines", label, width, height, mineCount);
    }

    /**
     * Cree et retourne un niveau "Beginner" (Debutant).
     *
     * @return Niveau Beginner
     */
    public static Level getBeginner() {
        return new Level(9, 9, 10, "Beginner");
    }

    /**
     * Cree et retourne un niveau "Intermediate" (Intermediaire).
     *
     * @return Niveau Intermediate
     */
    public static Level getIntermediate() {
        return new Level(16, 16, 40, "Intermediate");
    }

    /**
     * Cree et retourne un niveau "Expert".
     *
     * @return Niveau Expert
     */
    public static Level getExpert() {
        return new Level(30, 16, 99, "Expert");
    }
}
