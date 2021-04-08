package se.liu.chess.pieces;

import se.liu.chess.game.Board;
import se.liu.chess.game.TeamColor;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

/**
 *
 */
public class Pawn extends AbstractPiece {

    private Point[] attackingMoves = {new Point(1,1), new Point(-1, 1)};

    public Pawn(final TeamColor color) {
	super(color);
    }

    @Override public PieceType getType() {
	return PieceType.PAWN;
    }

    @Override public Set<Point> getMoves(Board board, int x, int y) {
	Set<Point> legalMoves = new HashSet<>();

	// Normal move
	int normalMoveY = y - 1;
	if (color == TeamColor.BLACK){
	    normalMoveY = y + 1;
	}
	if (isValidMoveToEmptyPiece(board, x, normalMoveY)) {
	    legalMoves.add(new Point(x, normalMoveY));
	}

	// Attacking moves
	for (Point move: attackingMoves) {
	    int combinedAttackX = x - move.x;
	    int combinedAttackY = y - move.y;
	    if (color == TeamColor.BLACK) {
		combinedAttackX = x + move.x;
		combinedAttackY = y + move.y;
	    }
	    if (board.isValidTile(combinedAttackX, combinedAttackY)) {
	        Piece piece = board.getPiece(combinedAttackX, combinedAttackY);
		if (new Point(combinedAttackX, combinedAttackY).equals(board.getEnPassantTarget()) || (piece != null && piece.getColor() != color)) {
		    legalMoves.add(new Point(combinedAttackX, combinedAttackY));
		}
	    }
	}

	// 2 steps first move
	if (!hasMoved){
	    int twoStepY = y - 2;
	    if (color == TeamColor.BLACK) {
		twoStepY = y + 2;
	    }
	    if (isValidMoveToEmptyPiece(board, x, twoStepY)){
	        legalMoves.add(new Point(x, twoStepY));
	    }
	}
	return legalMoves;
    }

    @Override public String toString() {
	if (getColor() == TeamColor.WHITE) {
	    return "P";
	}
	return "p";
    }

    private boolean isValidMoveToEmptyPiece(Board board, int x, int y){
	if (board.isValidTile(x, y)) {
	    Piece piece = board.getPiece(x, y);
	    if (piece == null) {
		return true;
	    }
	}
	return false;
    }
}
