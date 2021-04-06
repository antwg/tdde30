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

    Point[] attackingMoves = {new Point(1,1), new Point(-1, 1)};

    public Pawn(final TeamColor color) {
	super(color);
    }

    @Override public PieceType getType() {
	return PieceType.PAWN;
    }

    @Override public Set<Point> getMoves(Board board, int x, int y) {
	Set<Point> legalMoves = new HashSet<>();

	// Normal move
	int tempY1 = y - 1;
	if (color == TeamColor.BLACK){
	    tempY1 = y + 1;
	}
	if (validMoveToNullPiece(board, x, tempY1)) {
	    legalMoves.add(new Point(x, tempY1));
	}

	// Attacking moves
	for (Point move: attackingMoves) {
	    int tempX = x - move.x;
	    int tempY2 = y - move.y;
	    if (color == TeamColor.BLACK) {
		tempX = x + move.x;
		tempY2 = y + move.y;
	    }
	    if (board.isValidTile(tempX, tempY2)) {
	        Piece piece = board.getPiece(tempX, tempY2);
		if (new Point(tempX, tempY2).equals(board.getEnPassantTarget()) || (piece != null && piece.getColor() != color)) {
		    legalMoves.add(new Point(tempX, tempY2));
		}
	    }
	}

	// 2 steps first move
	if (!hasMoved){
	    int tempY3 = y - 2;
	    if (color == TeamColor.BLACK) {
		tempY3 = y + 2;
	    }
	    if (validMoveToNullPiece(board, x, tempY3)){
	        legalMoves.add(new Point(x, tempY3));
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

    private boolean validMoveToNullPiece(Board board, int x, int y){
	if (board.isValidTile(x, y)) {
	    Piece piece = board.getPiece(x, y);
	    if (piece == null) {
		return true;
	    }
	}
	return false;
    }
}
