public class Piece {
    private int moveDistance;
    private int value;
    private String color;

        // Value 99 is bomb
        // Value 100 is flag

    /** --------------------------------------- PUBLIC METHODS ------------------------------------------------**/
    /**
     * Default constructor (Never really used in game, but here for testing)
     */
    public Piece() {
        this.moveDistance = 0;
    }

    /**
     * Overridden Constructor for Piece class where the piece's value can be set by the user
     * @param playerValue the value the piece will hold
     */
    public Piece(int playerValue, String color) {
        this.value = playerValue;
        this.setMoveDistance(playerValue);
        this.color = color;
    }

    public int getValue() { return this.value; }

    public int getMoveDistance() { return this.moveDistance; }

    public String getColor() { return this.color; }

    /** --------------------------------------- PRIVATE METHODS -----------------------------------------------**/
    /**
     * Method to set the move distance based on the value of the player
     * @param playerValue the player's value
     */
    private void setMoveDistance(int playerValue) {
        // Case for player of value 2 which can move up to the size of the game board
        if (playerValue == 2) {
            this.moveDistance = 10;
        }

        // Case for flag or bomb, which can't move at all
        else if (playerValue == 99 || playerValue == 100) {
            this.moveDistance = 0;
        }

        // Case for all other players, which can move one space at a time
        else {
            this.moveDistance = 1;
        }
    }


}
