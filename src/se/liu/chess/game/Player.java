package se.liu.chess.game;

/**
 *
 */

public class Player
{
    private TeamColor color;
    private double timeLeft;
    private int score = 0;

    private boolean kingsideCastleAvailable;
    private boolean queensideCastleAvailable;

    // Constructor
    public Player(final TeamColor color, final double timeLeft) {
	this.color = color;
	this.timeLeft = timeLeft;

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

    public int getScore() {
	return score;
    }
}
