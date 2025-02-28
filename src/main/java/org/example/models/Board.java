package org.example.models;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Board {

    private final int width;
    private final int height;
    private final Map<Coordinate, Field> fields;
    private final Set<Field> mines;

    public Board(int width, int height, Collection<Coordinate> mines) {
        this.width = width;
        this.height = height;
        this.fields = new HashMap<>();
        this.mines = new HashSet<>();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Coordinate coord = new Coordinate(x, y);
                fields.put(coord, new Field(coord));
            }
        }

        for (Coordinate mineCoord : mines) {
            Field field = fields.get(mineCoord);
            if (field != null) {
                field.setHasMine(true);
                this.mines.add(field);
            }
        }

        calculateNeighbourMineCounts();
    }

    private void calculateNeighbourMineCounts() {
        for (Field field : fields.values()) {
            if (!field.hasMine()) {
                int count = 0;
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        if (dx == 0 && dy == 0) continue;
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

    public boolean hasWon() {
        for (Field field : fields.values()) {
            if (!field.hasMine() && !field.isOpened()) {
                return false;
            }
        }
        return true;
    }

    public void flagField(Coordinate coord) {
        Field field = fields.get(coord);
        if (field != null && !field.isOpened()) {
            field.setHasFlag(!field.hasFlag());
        }
    }

    public Collection<Field> getMines() {
        return mines;
    }

    public Collection<Field> getFields() {
        return fields.values();
    }

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

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getMineCount() {
        return mines.size();
    }
}
