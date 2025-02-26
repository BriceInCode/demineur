package org.example.models;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Représente le plateau de jeu pour un jeu de type "Démineur".
 * Le plateau contient des champs, certains avec des mines, et permet de gérer les actions de dévoilement et de marquage des champs.
 */
public class Board {

    // Largeur du plateau
    private final int width;

    // Hauteur du plateau
    private final int height;

    // Carte des champs du plateau avec leurs coordonnées comme clés
    private final Map<Coordinate, Field> fields;

    // Ensemble des champs contenant des mines
    private final Set<Field> mines;

    /**
     * Constructeur pour initialiser le plateau avec la largeur, la hauteur et les coordonnées des mines.
     *
     * @param width La largeur du plateau.
     * @param height La hauteur du plateau.
     * @param mines Collection de coordonnées représentant les mines.
     */
    public Board(int width, int height, Collection<Coordinate> mines) {
        this.width = width;
        this.height = height;
        this.fields = new HashMap<>();
        this.mines = new HashSet<>();

        // Initialisation des champs
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Coordinate coord = new Coordinate(x, y);
                fields.put(coord, new Field(coord));
            }
        }

        // Placement des mines sur le plateau
        for (Coordinate mineCoord : mines) {
            Field field = fields.get(mineCoord);
            if (field != null) {
                field.setHasMine(true);
                this.mines.add(field);
            }
        }

        // Calcul du nombre de mines voisines pour chaque champ
        calculateNeighbourMineCounts();
    }

    /**
     * Calcule le nombre de mines voisines pour chaque champ du plateau.
     */
    private void calculateNeighbourMineCounts() {
        for (Field field : fields.values()) {
            if (!field.hasMine()) {
                int count = 0;
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        if (dx == 0 && dy == 0) continue; // Ignorer la case elle-même
                        Coordinate neighbourCoord = new Coordinate(field.getCoordinate(), dx, dy);
                        Field neighbour = fields.get(neighbourCoord);
                        if (neighbour != null && neighbour.hasMine()) {
                            count++;
                        }
                    }
                }
                field.setNeighbourMineCount(count);
            }
        }
    }

    /**
     * Vérifie si le joueur a gagné la partie.
     * Le joueur a gagné si tous les champs sans mines ont été ouverts.
     *
     * @return true si le joueur a gagné, false sinon.
     */
    public boolean hasWon() {
        for (Field field : fields.values()) {
            if (!field.hasMine() && !field.isOpened()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Marque ou démarque un champ avec un drapeau (flag) à la position spécifiée.
     * Le champ ne peut être marqué que s'il n'est pas déjà ouvert.
     *
     * @param coord La coordonnée du champ à marquer/démarquer.
     */
    public void flagField(Coordinate coord) {
        Field field = fields.get(coord);
        if (field != null && !field.isOpened()) {
            field.setHasFlag(!field.hasFlag());
        }
    }

    /**
     * Récupère le nombre de mines restantes à découvrir en fonction des drapeaux placés.
     *
     * @return Le nombre de mines restantes.
     */
    public int getRemainingMines() {
        int flaggedCount = 0;
        for (Field field : fields.values()) {
            if (field.hasFlag()) {
                flaggedCount++;
            }
        }
        return mines.size() - flaggedCount;
    }

    /**
     * Récupère toutes les mines présentes sur le plateau.
     *
     * @return Une collection de champs contenant des mines.
     */
    public Collection<Field> getMines() {
        return mines;
    }

    /**
     * Récupère tous les champs du plateau.
     *
     * @return Une collection de tous les champs du plateau.
     */
    public Collection<Field> getFields() {
        return fields.values();
    }

    /**
     * Révèle un champ à la coordonnée spécifiée. Si une mine est trouvée, la partie est terminée.
     *
     * @param coord La coordonnée du champ à révéler.
     * @return Un objet contenant le résultat de l'action de révélation.
     */
    public RevealFieldsResult revealFields(Coordinate coord) {
        Field field = fields.get(coord);
        if (field == null || field.hasFlag() || field.isOpened()) {
            return new RevealFieldsResult();
        }

        Set<Field> revealedFields = new HashSet<>();
        if (field.hasMine()) {
            field.setOpened(true);
            revealedFields.add(field);
            return new RevealFieldsResult(revealedFields, RevealFieldsResult.RevealFieldState.FOUND_MINE);
        }

        revealField(field, revealedFields);
        return new RevealFieldsResult(revealedFields);
    }

    /**
     * Révèle récursivement un champ et ses voisins si le champ n'a pas de mines voisines.
     *
     * @param field Le champ à révéler.
     * @param revealedFields L'ensemble des champs déjà révélés.
     */
    private void revealField(Field field, Set<Field> revealedFields) {
        if (field.isOpened() || field.hasFlag()) {
            return;
        }
        field.setOpened(true);
        revealedFields.add(field);

        if (field.getNeighbourMineCount() == 0) {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    if (dx == 0 && dy == 0) continue;
                    Coordinate neighbourCoord = new Coordinate(field.getCoordinate(), dx, dy);
                    Field neighbour = fields.get(neighbourCoord);
                    if (neighbour != null) {
                        revealField(neighbour, revealedFields);
                    }
                }
            }
        }
    }

    /**
     * Révèle les champs autour d'un champ déjà ouvert si le nombre de drapeaux autour de ce champ
     * correspond au nombre de mines voisines.
     *
     * @param coord La coordonnée du champ à révéler.
     * @return Un objet contenant le résultat de l'action de révélation.
     */
    public RevealFieldsResult revealMultiClickFields(Coordinate coord) {
        Field field = fields.get(coord);
        if (field == null || !field.isOpened()) {
            return new RevealFieldsResult();
        }

        int flagCount = 0;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;
                Coordinate neighbourCoord = new Coordinate(field.getCoordinate(), dx, dy);
                Field neighbour = fields.get(neighbourCoord);
                if (neighbour != null && neighbour.hasFlag()) {
                    flagCount++;
                }
            }
        }

        if (flagCount == field.getNeighbourMineCount()) {
            Set<Field> revealedFields = new HashSet<>();
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    if (dx == 0 && dy == 0) continue;
                    Coordinate neighbourCoord = new Coordinate(field.getCoordinate(), dx, dy);
                    Field neighbour = fields.get(neighbourCoord);
                    if (neighbour != null && !neighbour.hasFlag() && !neighbour.isOpened()) {
                        if (neighbour.hasMine()) {
                            neighbour.setOpened(true);
                            revealedFields.add(neighbour);
                            return new RevealFieldsResult(revealedFields, RevealFieldsResult.RevealFieldState.FOUND_MINE);
                        }
                        revealField(neighbour, revealedFields);
                    }
                }
            }
            return new RevealFieldsResult(revealedFields);
        }

        return new RevealFieldsResult();
    }

    /**
     * Récupère la largeur du plateau.
     *
     * @return La largeur du plateau.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Récupère la hauteur du plateau.
     *
     * @return La hauteur du plateau.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Récupère le nombre de mines présentes sur le plateau.
     *
     * @return Le nombre de mines sur le plateau.
     */
    public int getMineCount() {
        return mines.size();
    }
}
