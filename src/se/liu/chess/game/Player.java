package se.liu.chess.game;

import se.liu.chess.pieces.Piece;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

/**
 * A player object contains all the information specific to said player as well as methods to access that information.
 * A player object keeps track of its color, timeLeft, score, increment and castling ability.
 */

public class Player
{
    private TeamColor color;

    private boolean kingsideCastleAvailable = true;
    private boolean queensideCastleAvailable = true;
    private Point queensideRookHomePosition;
    private Point kingsideRookHomePosition;
    private int promotionRank;
    private int homeRank;
    private int forwardDirection;

    private Set<Move> availableMoves = new HashSet<>();
    private Set<Point> attackedSquares = new HashSet<>();

    //TODO problematiskt med cirkelreferens?
    private Piece king = null;

    private double timeLeft;
    private static final double START_TIME = 300;
    private static final int SCORE = 0; // (Inspection) false alarm
    private static final int INCREMENT = 1;


    public Player(final TeamColor color) {
	this.color = color;
	this.timeLeft = START_TIME;

	if (color == TeamColor.WHITE) {
	    this.queensideRookHomePosition = new Point(0, 7);
	    this.kingsideRookHomePosition = new Point(7, 7);
	    this.promotionRank = 0;
	    this.homeRank = 7;
	    this.forwardDirection = -1;
	} else {
	    this.queensideRookHomePosition = new Point(0, 0);
	    this.kingsideRookHomePosition = new Point(7, 0);
	    this.promotionRank = 7;
	    this.homeRank = 0;
	    this.forwardDirection = 1;
	}

    }

    // ----------------------------------------------------- Getters / Setters -------------------------------------------------------------

    // --- Getters ---
    public TeamColor getColor() {
	return color;
    }

    public boolean canCastleKingside() {
	return kingsideCastleAvailable;
    }

    public boolean canCastleQueenside() {
	return queensideCastleAvailable;
    }

    public Point getQueensideRookHomePosition() {
	return queensideRookHomePosition;
    }

    public Point getKingsideRookHomePosition() {
	return kingsideRookHomePosition;
    }

    public int getPromotionRank() {
	return promotionRank;
    }

    public int getHomeRank() {
	return homeRank;
    }

    public int getForwardDirection() {
	return forwardDirection;
    }

    public Set<Move> getAvailableMoves() {
	return availableMoves;
    }

    public Set<Point> getAttackedSquares() {
	return attackedSquares;
    }

    public double getTimeLeft() {
	return timeLeft;
    }

    public int getScore() {
    return SCORE;
}

    public Piece getKing() {
    return king;
}

    // --- Setters ---

    public void setKingsideCastleAvailable(final boolean kingsideCastleAvailable) {
	this.kingsideCastleAvailable = kingsideCastleAvailable;
    }

    public void setQueensideCastleAvailable(final boolean queensideCastleAvailable) {
	this.queensideCastleAvailable = queensideCastleAvailable;
    }

    /**
     * Sets the available moves and updates the attacked squares based on those moves.
     * @param availableMoves
     */
    public void setAvailableMoves(final Set<Move> availableMoves) {
	this.availableMoves = availableMoves;

	attackedSquares = new HashSet<>();

	for (Move move : availableMoves) {
	    if (!move.isHarmless()) {
	        attackedSquares.add(move.getTargetSquare());
	    }
	}
    }

    public void setTimeLeft(final double timeLeft) {
	this.timeLeft = timeLeft;
    }

    public void setKing(final Piece king) {
        this.king = king;
    }




    // ----------------------------------------------------- Public Methods ----------------------------------------------------------------

    public void resetTime(){
        timeLeft = START_TIME;
    }

    public void increaseTimeByIncrement(){
        this.timeLeft += INCREMENT;
    }

    @Override public String toString() {
	if (color  == TeamColor.WHITE){
	    return "White player";
	}
	return "Black player";
    }
}
