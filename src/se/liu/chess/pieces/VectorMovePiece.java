package se.liu.chess.pieces;

import se.liu.chess.game.Board;
import se.liu.chess.game.Player;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

/**
 * VectorMovePiece is an abstract class that extends AbstractPiece. It's used by PieceTypes that can move
 * to Points in a given direction (by a vector).
 */
public abstract class VectorMovePiece extends AbstractPiece{

    protected Point[] allMoveDirections = null;

    protected VectorMovePiece(final Player owner) {
	super(owner);
    }

    @Override public Set<Point> getMoves(final Board board, final int x, final int y) {
	Set<Point> legalMoves = new HashSet<>();

	for (Point direction : allMoveDirections) {
	    int combinedX = x + direction.x;
	    int combinedY = y + direction.y;

	    while (true) {
		if (!board.isValidTile(combinedX, combinedY)) {
		    break;
		} else if (board.getPiece(combinedX, combinedY) == null) {
		    legalMoves.add(new Point(combinedX, combinedY));
		} else if (board.getPiece(combinedX, combinedY).getColor() == this.getColor()) {
		    break;
		} else if (board.getPiece(combinedX, combinedY).getColor() != this.getColor()) {
		    legalMoves.add(new Point(combinedX, combinedY));
		    break;
		} else {
		    System.out.println("Code should not get here (in VectorMovePiece)");
		}
		combinedX += direction.x;
		combinedY += direction.y;
	    }
	}

	return legalMoves;
    }
}
