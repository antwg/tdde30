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
 * PointMovePiece is an abstract class that extends AbstractPiece. It's used by PieceTypes that can move to specfic
 * points on the board in relation to itself rather than all moves in a direction. (see VectorMovePiece)
 */
public abstract class PointMovePiece extends AbstractPiece{

    protected Point[] allMoves = null;

    /**
     *
     * @param owner
     * @param position
     */
    protected PointMovePiece(final Player owner, final Point position) {
	super(owner, position);
    }

    protected Set<Move> getPointMoves(final Board board, final int x, final int y, Point[] moveVectors) {
        Set<Move> legalMoves = new HashSet<>();

	Set<MoveCharacteristics> moveCharacteristics = EnumSet.noneOf(MoveCharacteristics.class);

	for (Point move: moveVectors) {
	    int combinedX = x + move.x;
	    int combinedY = y + move.y;

	    if (board.isValidTile(combinedX, combinedY)) {
		Piece piece = board.getPiece(combinedX, combinedY);
		if (piece == null || piece.getColor() != this.getColor()) {
		    Move moveToAdd = new Move(new Point(x, y), new Point(combinedX, combinedY),
					      this, moveCharacteristics);
		    legalMoves.add(moveToAdd);
		}
	    }
	}

	return legalMoves;
    }
}
