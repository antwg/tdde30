package se.liu.chess.pieces;

import se.liu.chess.game.Board;
import se.liu.chess.game.Move;
import se.liu.chess.game.MoveCharacteristics;
import se.liu.chess.game.Player;

import java.awt.*;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

/**
 * VectorMovePiece is an abstract class that extends AbstractPiece. It's used by PieceTypes that can move
 * to Points in a given direction (by a vector).
 */
public abstract class VectorMovePiece extends AbstractPiece{

    protected Point[] allMoveDirections = null;

    protected VectorMovePiece(final Player owner, final Point position) {
	super(owner, position);
    }
    /*
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
    */

    public Set<Move> getVectorMoves(final Board board, final int x, final int y, Point[] moveVectors) {
	Set<Move> legalMoves = new HashSet<>();

	Set<MoveCharacteristics> moveCharacteristics = EnumSet.noneOf(MoveCharacteristics.class);

	for (Point moveVector : moveVectors) {
	    int combinedX = x + moveVector.x;
	    int combinedY = y + moveVector.y;

	    while (true) {
		if (!board.isValidTile(combinedX, combinedY)) {
		    break;
		} else if (board.getPiece(combinedX, combinedY) == null) {
		    Move moveToAdd = new Move(new Point(x, y), new Point(combinedX, combinedY),
					    this, moveCharacteristics);
		    legalMoves.add(moveToAdd);
		} else if (board.getPiece(combinedX, combinedY).getColor() == this.getColor()) {
		    break;
		} else if (board.getPiece(combinedX, combinedY).getColor() != this.getColor()) {
		    Move moveToAdd = new Move(new Point(x, y), new Point(combinedX, combinedY),
					      this, moveCharacteristics);
		    legalMoves.add(moveToAdd);
		    break;
		} else {
		    System.out.println("Code should not get here! (method getVectorMoves() in VectorMovePiece)");
		}
		combinedX += moveVector.x;
		combinedY += moveVector.y;
	    }
	}

	return legalMoves;
    }
}
