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

    @Override public Set<Move> getMoves(Board board, int x, int y) {
	/*
	allMoveDirections = bishopMoveDirections;
	return super.getMoves(board, x, y);
	 */
	//TODO add special conditions
	return getVectorMoves(board, x, y, bishopMoveDirections);
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
