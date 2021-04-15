package se.liu.chess.pieces;

import se.liu.chess.game.Board;
import se.liu.chess.game.Player;
import se.liu.chess.game.TeamColor;

import java.awt.*;
import java.util.Set;

/**
 *
 */
public class King extends PointMovePiece {
    private Point[] kingMoves = { new Point(0, 1),
	    			  new Point(0, -1),
	    			  new Point(1, 0),
	    			  new Point(1, 1),
	    			  new Point(1, -1),
	    			  new Point(-1, 0),
	    			  new Point(-1, 1),
	    			  new Point(-1, -1) };

    public King(final Player owner) {
	super(owner);
    }

    //TODO fix hardcoded ugliness, duplicate code and bad naming
    private boolean canCastleQueenside(Board board) {
	if (!owner.canCastleQueenside() || board.isInCheck(owner)) {
	    return false;
	}

	if (owner.getColor() == TeamColor.WHITE) {
	    if (board.getPiece(1, 7) == null &&
		board.getPiece(2, 7) == null &&
		board.getPiece(3, 7) == null &&
		!board.getBlackPossibleMoves().contains(new Point(1, 7)) &&
		!board.getBlackPossibleMoves().contains(new Point(2, 7)) &&
		!board.getBlackPossibleMoves().contains(new Point(3, 7))) {
	        return true;
	    }
	} else {
	    if (board.getPiece(1, 0) == null &&
		board.getPiece(2, 0) == null &&
		board.getPiece(3, 0) == null &&
		!board.getWhitePossibleMoves().contains(new Point(1, 0)) &&
		!board.getWhitePossibleMoves().contains(new Point(2, 0)) &&
		!board.getWhitePossibleMoves().contains(new Point(3, 0))) {
		return true;
	    }
	}
	return false;
    }

    //TODO fix hardcoded ugliness, duplicate code and bad naming
    private boolean canCastleKingside(Board board) {
	if (!owner.canCastleKingside() || board.isInCheck(owner)) {
	    return false;
	}

	if (owner.getColor() == TeamColor.WHITE) {
	    if (board.getPiece(5, 7) == null &&
		board.getPiece(6, 7) == null &&
		!board.getBlackPossibleMoves().contains(new Point(5, 7)) &&
		!board.getBlackPossibleMoves().contains(new Point(6, 7))) {
		return true;
	    }
	} else {
	    if (board.getPiece(5, 0) == null &&
		board.getPiece(6, 0) == null &&
		!board.getWhitePossibleMoves().contains(new Point(5, 0)) &&
		!board.getWhitePossibleMoves().contains(new Point(6, 0))) {
		return true;
	    }
	}
	return false;
    }

    @Override public Set<Point> getMoves(final Board board, final int x, final int y) {
        allMoves = kingMoves;
        Set<Point> availableMoves = super.getMoves(board, x, y);
        // TODO fix this too
	if (canCastleQueenside(board)) {
	    if (owner.getColor() == TeamColor.WHITE) {
		availableMoves.add(new Point(2, 7));
	    } else {
	        availableMoves.add(new Point(2, 0));
	    }
	}
	if (canCastleKingside(board)) {
	    if (owner.getColor() == TeamColor.WHITE) {
		availableMoves.add(new Point(6, 7));
	    } else {
		availableMoves.add(new Point(6, 0));
	    }
	}
	return availableMoves;
    }

    @Override public PieceType getType() {
	return PieceType.KING;
    }

    @Override public String toString() {
	if (getColor() == TeamColor.WHITE) {
	    return "K";
	}
	return "k";
    }
}
