package se.liu.chess.pieces;

import se.liu.chess.game.Board;
import se.liu.chess.game.Move;
import se.liu.chess.game.Player;
import se.liu.chess.game.TeamColor;

import java.awt.*;
import java.util.Set;


/**
 * The Knight class extends PointMovePiece and in turn AbstractPiece and Piece.
 * Overrides getMoves, getType and toString.
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

    public Knight(final Player owner, final Point position) {
	super(owner, position);
    }

    @Override public Set<Move> getMoves(final Board board, final int x, final int y) {
	Set<Move> possibleMoves = getPointMoves(board, x, y, knightMoves);

	// Limit moves

	possibleMoves = limitMovesToThreatSquares(board, possibleMoves);

	possibleMoves = limitMovesToPinSquares(board, possibleMoves);

	return possibleMoves;
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
