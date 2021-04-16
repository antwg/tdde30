package se.liu.chess.pieces;

import se.liu.chess.game.Board;
import se.liu.chess.game.Player;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

/**
 * PointMovePiece is an abstract class that extends AbstractPiece. It's used by PieceTypes that can move to specfic
 * points on the board in relation to itself rather than all moves in a direction. (see VectorMovePiece)
 */
public abstract class PointMovePiece extends AbstractPiece{

    protected Point[] allMoves = null;

    protected PointMovePiece(final Player owner) {
	super(owner);
    }

    @Override public Set<Point> getMoves(final Board board, final int x, final int y) {
	Set<Point> legalMoves = new HashSet<>();

	for (Point move: allMoves) {
	    int combinedX = x + move.x;
	    int combinedY = y + move.y;

	    if (board.isValidTile(combinedX, combinedY)) {
		Piece piece = board.getPiece(combinedX, combinedY);
		if (piece == null || piece.getColor() != this.getColor()) {
		    legalMoves.add(new Point(combinedX, combinedY));
		}
	    }
	}
	return legalMoves;
    }
}
