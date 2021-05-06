package se.liu.chess.pieces;

import se.liu.chess.game.Board;
import se.liu.chess.game.Move;
import se.liu.chess.game.MoveCharacteristics;
import se.liu.chess.game.Player;
import se.liu.chess.game.TeamColor;

import java.awt.*;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

/**
 * The King class extends PointMovePiece and in turn AbstractPiece and Piece.
 * Overrides getMoves, getType and toString. Also has methods related to castling.
 * Overrides superclass GetMoves method to filter special cases.
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

    public King(final Player owner, final Point position) {
	super(owner, position);
    }

    //TODO fix hardcoded ugliness, duplicate code and bad naming
    private boolean canCastleQueenside(Board board) {
        Player opponent = board.getOpponentPlayer(owner);

	if (!owner.canCastleQueenside()) {
	    return false;
	}

	int homeRank = owner.getHomeRank();
	if (board.getPiece(1, homeRank) == null &&
	    board.getPiece(2, homeRank) == null &&
	    board.getPiece(3, homeRank) == null &&
	    !opponent.getAttackedSquares().contains(new Point(2, homeRank)) &&
	    !opponent.getAttackedSquares().contains(new Point(3, homeRank)) &&
	    !opponent.getAttackedSquares().contains(new Point(4, homeRank))) {
	    return true;
	}
	return false;
    }

    //TODO fix hardcoded ugliness, duplicate code and bad naming
    private boolean canCastleKingside(Board board) {
	Player opponent = board.getOpponentPlayer(owner);

	if (!owner.canCastleKingside()) {
	    return false;
	}

	if (board.getPiece(5, owner.getHomeRank()) == null &&
	    board.getPiece(6, owner.getHomeRank()) == null &&
	    !opponent.getAttackedSquares().contains(new Point(4, owner.getHomeRank())) &&
	    !opponent.getAttackedSquares().contains(new Point(5, owner.getHomeRank())) &&
	    !opponent.getAttackedSquares().contains(new Point(6, owner.getHomeRank()))) {
	    return true;
	}
	return false;
    }

    /*
    @Override public Set<Point> getMoves(final Board board, final int x, final int y) {
        allMoves = kingMoves;
        Set<Point> availableMoves = super.getMoves(board, x, y);
        // TODO fix this too
	if (!board.isInCheck(owner)) {
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
	}

	return availableMoves;
    }
    */

    @Override public Set<Move> getMoves(final Board board, final int x, final int y) {
	Set<Move> possibleMoves = new HashSet<>();

	possibleMoves.addAll(getPointMoves(board, x, y, kingMoves));

	possibleMoves.addAll(getCastlingMoves(board));

	possibleMoves = limitMovesToSafeSquares(board, possibleMoves);

	return possibleMoves;
    }

    // TODO not working properly
    private Set<Move> limitMovesToSafeSquares(final Board board, final Set<Move> initialMoveSet) {
	Set<Move> possibleMoves = new HashSet<>();

	for (Move move : initialMoveSet) {
	    // check if not protected
	    if (board.getPiece(move.getTargetSquare()) == null &&
		!board.getInactivePlayer().getAttackedSquares().contains(move.getTargetSquare())) {
		possibleMoves.add(move);
	    } else if (!isPieceProtected(board, move.getTargetSquare())) {
		possibleMoves.add(move);
	    }
	}

	return possibleMoves;
    }

    private boolean isPieceProtected(final Board board, final Point targetSquare) {
	return false;
    }

    private Set<Move> getCastlingMoves(final Board board) {
	Set<Move> possibleMoves = new HashSet<>();

	if (board.isInCheck(owner)) {
	    return possibleMoves;
	}

	Set<MoveCharacteristics> moveCharacteristics = EnumSet.noneOf(MoveCharacteristics.class);
	moveCharacteristics.add(MoveCharacteristics.HARMLESS);
	moveCharacteristics.add(MoveCharacteristics.CASTLING);

	if (canCastleQueenside(board)) {
		Point p1 = new Point(4, 0);
		Point p2 = new Point(2, 0);

		if (owner.getColor() == TeamColor.WHITE) {
		    p1 = new Point(4, 7);
		    p2 = new Point(2, 7);
		}
		Move moveToAdd = new Move(p1, p2, this, moveCharacteristics);
		possibleMoves.add(moveToAdd);
	}
	    /*if (owner.getColor() == TeamColor.WHITE) {
		Move moveToAdd = new Move(new Point(4, 7), new Point(2, 7),
					  this, moveCharacteristics);
		possibleMoves.add(moveToAdd);
	    } else {
		Move moveToAdd = new Move(new Point(4, 0), new Point(2, 0),
					  this, moveCharacteristics);
		possibleMoves.add(moveToAdd);
	    }*/

	if (canCastleKingside(board)) {
	    Point p1 = new Point(4, 0);
	    Point p2 = new Point(6, 0);

	    if (owner.getColor() == TeamColor.WHITE){
	        p1 = new Point(4, 7);
	        p2 = new Point(6, 7);
	    }
	    Move moveToAdd = new Move(p1, p2, this, moveCharacteristics);
	    possibleMoves.add(moveToAdd);

	    /*if (owner.getColor() == TeamColor.WHITE) {
		Move moveToAdd = new Move(new Point(4, 7), new Point(6, 7),
					  this, moveCharacteristics);
		possibleMoves.add(moveToAdd);
	    } else {
		Move moveToAdd = new Move(new Point(4, 0), new Point(6, 0),
					  this, moveCharacteristics);
		possibleMoves.add(moveToAdd);
	    }*/
	}
	return possibleMoves;
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
