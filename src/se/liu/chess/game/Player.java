package se.liu.chess.game;

import se.liu.chess.pieces.Piece;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

/**
 *
 */

public class Player
{
    private TeamColor color;
    private double timeLeft;
    private int score = 0;
    private int increment;

    private boolean kingsideCastleAvailable;
    private boolean queensideCastleAvailable;

    // problematiskt med cirkelreferens?
    private Piece king = null;

    // Constructor
    public Player(final TeamColor color, final double timeLeft, final int increment) {
	this.color = color;
	this.timeLeft = timeLeft;
	this.increment = increment;

	this.kingsideCastleAvailable = true;
	this.queensideCastleAvailable = true;
    }

    public TeamColor getColor() {
	return color;
    }

    public boolean canCastleKingside() {
	return kingsideCastleAvailable;
    }

    public boolean canCastleQueenside() {
	return queensideCastleAvailable;
    }

    public double getTimeLeft() {
	return timeLeft;
    }

    public void setTimeLeft(final double timeLeft) {
	this.timeLeft = timeLeft;
    }

    public int getScore() {
	return score;
    }

    public void setKing(final Piece king) {
        this.king = king;
    }

    public Piece getKing() {
        return king;
    }

    public int getIncrement() {
	return increment;
    }

    public void increaseTimeByIncrement(){
        this.timeLeft += increment;
    }
}
