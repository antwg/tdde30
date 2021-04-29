package se.liu.chess.pieces;

import se.liu.chess.game.Board;
import se.liu.chess.game.Move;
import se.liu.chess.game.Player;
import se.liu.chess.game.TeamColor;

import java.awt.*;
import java.util.Set;

/**
 * The Queen class extends VectorMovePiece and in turn AbstractPiece and Piece.
 * Overrides getMoves, getType and toString.
 * Can move both diagonally and in cardinal directions.
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

    public Queen(final Player owner, final Point position) {
	super(owner, position);
    }

    @Override public PieceType getType() {
	return PieceType.QUEEN;
    }

    @Override public Set<Move> getMoves(Board board, int x, int y) {
	Set<Move> possibleMoves = getVectorMoves(board, x, y, queenMoveDirections);

	// Limit moves

	possibleMoves = limitMovesToThreatSquares(board, possibleMoves);

	possibleMoves = limitMovesToPinSquares(board, possibleMoves);

	return possibleMoves;
    }

    @Override public String toString() {
	if (getColor() == TeamColor.WHITE) {
	    return "Q";
	}
	return "q";
    }
}
