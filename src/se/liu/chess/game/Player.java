package se.liu.chess.game;

import se.liu.chess.pieces.Piece;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

/**
 * A player object contains all the information specific to said player as
 * well as methods to access that information.
 * A player object keeps track of its color, timeLeft, score, increment and
 * castling ability as well as some player specific board coordinates.
 */

public class Player
{
    private TeamColor color;

    private boolean kingSideCastleAvailable = true, queenSideCastleAvailable = true;
    private Point queenSideRookHomePosition, kingSideRookHomePosition;
    private int promotionRank, homeRank, pawnRank, enPassantRow, forwardDirection;

    private Set<Move> availableMoves = new HashSet<>();
    private Set<Point> attackedSquares = new HashSet<>();

    private Piece king = null;

    private double timeLeft;
    private static final double START_TIME = 300;
    private static final int INCREMENT = 1;

    public Player(final TeamColor color) {
	this.color = color;
	this.timeLeft = START_TIME;

	if (color == TeamColor.WHITE) {
	    this.queenSideRookHomePosition = new Point(0, 7);
	    this.kingSideRookHomePosition = new Point(7, 7);
	    this.promotionRank = 0;
	    this.homeRank = 7;
	    this.pawnRank = 6;
	    this.enPassantRow = 5;
	    this.forwardDirection = -1;
	} else {
	    this.queenSideRookHomePosition = new Point(0, 0);
	    this.kingSideRookHomePosition = new Point(7, 0);
	    this.promotionRank = 7;
	    this.homeRank = 0;
	    this.pawnRank = 1;
	    this.enPassantRow = 2;
	    this.forwardDirection = 1;
	}

    }

    // ----------------------------------------------------- Getters / Setters -------------------------------------------------------------

    public TeamColor getColor() {
	return color;
    }

    public boolean isKingSideCastleAvailable() {
	return kingSideCastleAvailable;
    }

    public boolean isQueenSideCastleAvailable() {
	return queenSideCastleAvailable;
    }

    public Point getQueenSideRookHomePosition() {
	return queenSideRookHomePosition;
    }

    public Point getKingSideRookHomePosition() {
	return kingSideRookHomePosition;
    }

    public int getPromotionRank() {
	return promotionRank;
    }

    public int getHomeRank() {
	return homeRank;
    }

    public int getPawnRank() {
	return pawnRank;
    }

    public int getEnPassantRow() {
        return enPassantRow;
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

    public Piece getKing() {
    return king;
}



    public void setKingSideCastleAvailable(final boolean kingSideCastleAvailable) {
	this.kingSideCastleAvailable = kingSideCastleAvailable;
    }

    public void setQueenSideCastleAvailable(final boolean queenSideCastleAvailable) {
	this.queenSideCastleAvailable = queenSideCastleAvailable;
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

    public void countDown(){
        this.timeLeft --;
    }

    public boolean isOutOfTime(){
        return timeLeft <= 0;
    }

    @Override public String toString() {
	if (color  == TeamColor.WHITE){
	    return "White player";
	}
	return "Black player";
    }
}
