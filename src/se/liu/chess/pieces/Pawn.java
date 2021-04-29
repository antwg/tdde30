package se.liu.chess.pieces;

import se.liu.chess.game.Board;
import se.liu.chess.game.Move;
import se.liu.chess.game.MoveCharacteristics;
import se.liu.chess.game.Player;
import se.liu.chess.game.TeamColor;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

/**
 * The Pawn class extends AbstractPiece.
 * Overrides getMoves, getType and toString.
 * Has three types of moves: normal, attacking and first move and a special case for en passant
 */
public class Pawn extends AbstractPiece {

    private Point[] attackingMoves = {new Point(1,1), new Point(-1, 1)};

    public Pawn(final Player owner, final Point position) {
	super(owner, position);
    }

    @Override public PieceType getType() {
	return PieceType.PAWN;
    }

    /*
    @Override public Set<Point> getMoves(Board board, int x, int y) {
	Set<Point> legalMoves = new HashSet<>();

	// Normal move
	int normalMoveY = y - 1;
	if (this.getColor() == TeamColor.BLACK){
	    normalMoveY = y + 1;
	}
	if (isValidMoveToEmptyPiece(board, x, normalMoveY)) {
	    legalMoves.add(new Point(x, normalMoveY));
	}

	// Attacking moves
	for (Point move: attackingMoves) {
	    int combinedAttackX = x - move.x;
	    int combinedAttackY = y - move.y;
	    if (this.getColor() == TeamColor.BLACK) {
		combinedAttackX = x + move.x;
		combinedAttackY = y + move.y;
	    }
	    if (board.isValidTile(combinedAttackX, combinedAttackY)) {
	        Piece piece = board.getPiece(combinedAttackX, combinedAttackY);
		if (new Point(combinedAttackX, combinedAttackY).equals(board.getEnPassantTarget()) || (piece != null && piece.getColor() != this.getColor())) {
		    legalMoves.add(new Point(combinedAttackX, combinedAttackY));
		}
	    }
	}

	// 2 steps first move
	if (!hasMoved){
	    final int twoSteps = 2;
	    int twoStepY = y - twoSteps;
	    if (this.getColor() == TeamColor.BLACK) {
		twoStepY = y + twoSteps;
	    }
	    if (isValidMoveToEmptyPiece(board, x, twoStepY)){
	        legalMoves.add(new Point(x, twoStepY));
	    }
	}
	return legalMoves;
    }
    */

    @Override public Set<Move> getMoves(final Board board, final int x, final int y) {
        Set<Move> s = new HashSet<>();
        Set<MoveCharacteristics> m = new HashSet<>();
        m.add(MoveCharacteristics.HARMLESS);
        s.add(new Move(new Point(x,y), new Point(x+1, y+1), this, m));
	return s;
    }

    @Override public String toString() {
	if (this.getColor() == TeamColor.WHITE) {
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
