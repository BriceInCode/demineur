package org.example.models;

import java.util.Collection;
import java.util.Collections;

/**
 * This class represents the result of a field reveal operation on the board.
 * It contains information about the revealed fields and the state of the operation.
 */
public class RevealFieldsResult {

    /**
     * Enum representing the possible states of a field reveal operation.
     */
    public enum RevealFieldState {
        FIELD_NOT_REVEALED,  // Indicates no field was revealed.
        FIELDS_REVEALED,     // Indicates fields were revealed successfully.
        FOUND_MINE           // Indicates a mine was found during the reveal.
    }

    private Collection<Field> revealedFields;  // Collection of fields that were revealed.
    private RevealFieldState state;            // State of the reveal operation.

    /**
     * Default constructor that initializes the result with no revealed fields
     * and a state of FIELD_NOT_REVEALED.
     */
    public RevealFieldsResult() {
        this.revealedFields = Collections.emptyList();  // No fields revealed.
        this.state = RevealFieldState.FIELD_NOT_REVEALED;  // Default state.
    }

    /**
     * Constructor that initializes the result with a collection of revealed fields
     * and determines the state based on the contents of the collection.
     *
     * @param fields A collection of fields that were revealed.
     */
    public RevealFieldsResult(Collection<Field> fields) {
        this.revealedFields = Collections.unmodifiableCollection(fields);  // Make collection unmodifiable.
        this.state = determineState(fields);  // Determine the state based on the fields.
    }

    /**
     * Constructor that initializes the result with a collection of revealed fields
     * and a specific state.
     *
     * @param fields A collection of fields that were revealed.
     * @param state The state of the reveal operation.
     */
    public RevealFieldsResult(Collection<Field> fields, RevealFieldState state) {
        this.revealedFields = Collections.unmodifiableCollection(fields);  // Make collection unmodifiable.
        this.state = state;  // Set the provided state.
    }

    /**
     * Returns the collection of revealed fields.
     *
     * @return The collection of revealed fields.
     */
    public Collection<Field> getRevealedFields() {
        return revealedFields;
    }

    /**
     * Returns the state of the reveal operation.
     *
     * @return The state of the reveal operation.
     */
    public RevealFieldState getState() {
        return state;
    }

    /**
     * Determines the state of the reveal operation based on the provided collection
     * of fields. If any of the fields contains a mine, the state will be FOUND_MINE.
     * If the collection is empty, the state will be FIELD_NOT_REVEALED.
     * Otherwise, the state will be FIELDS_REVEALED.
     *
     * @param fields The collection of fields to check.
     * @return The determined state of the reveal operation.
     */
    private RevealFieldState determineState(Collection<Field> fields) {
        if (fields.isEmpty()) {
            return RevealFieldState.FIELD_NOT_REVEALED;  // No fields revealed.
        }
        for (Field field : fields) {
            if (field.hasMine()) {
                return RevealFieldState.FOUND_MINE;  // Mine was found in the revealed fields.
            }
        }
        return RevealFieldState.FIELDS_REVEALED;  // All fields revealed without mines.
    }
}
