package se.liu.chess.game;

import se.liu.chess.pieces.Piece;

/**
 * A player object contains all the information specific to said player as well as methods to access that information.
 * A player object keeps track of its color, timeLeft, score, increment and castling ability.
 */

public class Player
{
    private TeamColor color;
    private double timeLeft;
    private static final double START_TIME = 300;
    private static final int SCORE = 0;
    private static final int INCREMENT = 1;

    private boolean kingsideCastleAvailable;
    private boolean queensideCastleAvailable;

    // problematiskt med cirkelreferens?
    private Piece king = null;

    // Constructor
    public Player(final TeamColor color) {
	this.color = color;

	this.kingsideCastleAvailable = true;
	this.queensideCastleAvailable = true;
    }

    // ----------------------------------------------------- Getters / Setters -------------------------------------------------------------


    public TeamColor getColor() {
	return color;
    }

    public boolean canCastleKingside() {
	return kingsideCastleAvailable;
    }

    public boolean canCastleQueenside() {
	return queensideCastleAvailable;
    }

    public void setKingsideCastleAvailable(final boolean kingsideCastleAvailable) {
	this.kingsideCastleAvailable = kingsideCastleAvailable;
    }

    public void setQueensideCastleAvailable(final boolean queensideCastleAvailable) {
	this.queensideCastleAvailable = queensideCastleAvailable;
    }

    public double getTimeLeft() {
	return timeLeft;
    }

    public void setTimeLeft(final double timeLeft) {
	this.timeLeft = timeLeft;
    }

    public int getScore() {
	return SCORE;
    }

    public void setKing(final Piece king) {
        this.king = king;
    }

    public Piece getKing() {
        return king;
    }


    // ----------------------------------------------------- Public Methods ----------------------------------------------------------------

    public void resetTime(){
        timeLeft = START_TIME;
    }

    public void increaseTimeByIncrement(){
        this.timeLeft += INCREMENT;
    }
}
