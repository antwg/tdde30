package se.liu.chess.pieces;

import se.liu.chess.game.Board;
import se.liu.chess.game.TeamColor;

import java.awt.*;
import java.util.Set;


/**
 *
 */
public class Knight extends PointMovePiece
{
    private Point[] knightMoves = { new Point(1, 2),
	    			    new Point(2, 1),
	    			    new Point(1, -2),
	    			    new Point(2, -1),
	    			    new Point(-1, 2),
	    			    new Point(-2, 1),
	    			    new Point(-1, -2),
	    			    new Point(-2, -1) };

    public Knight(final TeamColor color) {
	super(color);
    }

    @Override public Set<Point> getMoves(final Board board, final int x, final int y) {
	allMoves = knightMoves;
	return super.getMoves(board, x, y);
    }

    @Override public PieceType getType() {
	return PieceType.KNIGHT;
    }

    @Override public String toString() {
	if (getColor() == TeamColor.WHITE) {
	    return "N";
	}
	return "n";
    }
}
