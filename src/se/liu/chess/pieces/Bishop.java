package se.liu.chess.pieces;

import se.liu.chess.game.Board;
import se.liu.chess.game.TeamColor;
import java.awt.*;
import java.util.Set;

/**
 *
 */
public class Bishop extends VectorMovePiece
{
    private Point[] bishopMoveDirections = {new Point(1, 1),
	    				    new Point(1, -1),
	    				    new Point(-1, 1),
	    				    new Point(-1, -1)};

    public Bishop(final TeamColor color) {
	super(color);
    }

    @Override public Set<Point> getMoves(Board board, int x, int y) {
	allMoveDirections = bishopMoveDirections;
	return super.getMoves(board, x, y);
    }

    @Override public PieceType getType() {
	return PieceType.BISHOP;
    }

    @Override public String toString() {
	if (getColor() == TeamColor.WHITE) {
	    return "B";
	}
	return "b";
    }
}
