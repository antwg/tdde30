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

    protected VectorMovePiece(final Player owner, final Point position) {
	super(owner, position);
    }

    /**
     * Returns all possible moves along all given movement vectors.
     * This includes moves that would put the moving player's own king in check.
     * @param board
     * @param x
     * @param y
     * @param moveVectors
     * @return
     */
    protected Set<Move> getVectorMoves(final Board board, final int x, final int y, Point[] moveVectors) {
	Set<Move> legalMoves = new HashSet<>();

	Set<MoveCharacteristics> moveCharacteristics = EnumSet.noneOf(MoveCharacteristics.class);

	for (Point moveVector : moveVectors) {
	    int combinedX = x + moveVector.x;
	    int combinedY = y + moveVector.y;

	    while (board.isValidTile(combinedX, combinedY)) {

		Piece pieceOnSquare = board.getPiece(combinedX, combinedY);

		if (pieceOnSquare == null ||
		    pieceOnSquare.getColor() != getColor() && pieceOnSquare.getType().equals(PieceType.KING)) {
		    Move moveToAdd = new Move(new Point(x, y), new Point(combinedX, combinedY),
					    this, moveCharacteristics);
		    legalMoves.add(moveToAdd);
		} else if (pieceOnSquare.getColor() == this.getColor()) {
		    break;
		} else if (pieceOnSquare.getColor() != this.getColor()) {
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
