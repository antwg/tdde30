package se.liu.chess.pieces;

import se.liu.chess.game.Board;
import se.liu.chess.game.TeamColor;

import java.awt.*;
import java.util.Set;

/**
 *
 */
public class King extends PointMovePiece {
    private Point[] kingMoves = { new Point(1, 0),
	    			  new Point(-1, 0),
	    			  new Point(1, 1),
	    			  new Point(1, -1),
 	    			  new Point(0, 1),
	    			  new Point(0, -1),
	    			  new Point(-1, 1),
	    			  new Point(-1, -1) };

    public King(final TeamColor color) {
	super(color);
    }

    @Override public Set<Point> getMoves(final Board board, final int x, final int y) {
        allMoves = kingMoves;
	return super.getMoves(board, x, y);
    }

    @Override public PieceType getType() {
	return PieceType.KING;
    }

    @Override public String toString() {
	if (getColor() == TeamColor.WHITE) {
	    return "K";
	}
	return "k";
    }
}
