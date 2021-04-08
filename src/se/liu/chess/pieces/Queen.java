package se.liu.chess.pieces;

import se.liu.chess.game.Board;
import se.liu.chess.game.Player;
import se.liu.chess.game.TeamColor;

import java.awt.*;
import java.util.Set;

/**
 *
 */
public class Queen extends VectorMovePiece
{
    private Point[] queenMoveDirections = {new Point(1, 1),
	    				   new Point(1, -1),
	    				   new Point(-1, 1),
	    				   new Point(-1, -1),
	    				   new Point(1, 0),
	    				   new Point(0, 1),
	    				   new Point(-1, 0),
	    				   new Point(0, -1)};

    public Queen(final Player owner) {
	super(owner);
    }

    @Override public PieceType getType() {
	return PieceType.QUEEN;
    }

    @Override public Set<Point> getMoves(Board board, int x, int y) {
	allMoveDirections = queenMoveDirections;
        return super.getMoves(board, x, y);
    }

    @Override public String toString() {
	if (getColor() == TeamColor.WHITE) {
	    return "Q";
	}
	return "q";
    }
}
