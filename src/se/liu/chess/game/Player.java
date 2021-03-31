package se.liu.chess.game;

/**
 *
 */

public class Player
{
    private TeamColor color;
    private double timeLeft;
    private int score = 0;

    public Player(final TeamColor color, final double timeLeft) {
	this.color = color;
	this.timeLeft = timeLeft;
    }

    public TeamColor getColor() {
	return color;
    }

    public double getTimeLeft() {
	return timeLeft;
    }

    public int getScore() {
	return score;
    }
}
