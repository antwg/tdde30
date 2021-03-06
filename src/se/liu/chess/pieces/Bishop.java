package se.liu.chess.pieces;

import se.liu.chess.game.Board;
import se.liu.chess.game.Move;
import se.liu.chess.game.Player;
import se.liu.chess.game.TeamColor;

import java.awt.*;
import java.util.Set;

/**
 * The Bishop class extends VectorMovePiece and in turn AbstractPiece and Piece.
 * Overrides getMoves, getType and toString.
 * Can move diagonally.
 */
public class Bishop extends VectorMovePiece
{
    private Point[] bishopMoveDirections = {new Point(1, 1),
	    				    new Point(1, -1),
	    				    new Point(-1, 1),
	    				    new Point(-1, -1)};

    public Bishop(final Player owner, final Point position) {
	super(owner, position);
    }

    // ----------------------------------------------------- Public Methods ----------------------------------------------------------------

    @Override public Set<Move> getMoves(Board board, int x, int y) {
	Set<Move> possibleMoves = getVectorMoves(board, x, y, bishopMoveDirections);

	// Limit moves

	possibleMoves = limitMovesToThreatSquares(board, possibleMoves);

	possibleMoves = limitMovesToPinSquares(board, possibleMoves);

	return possibleMoves;
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
