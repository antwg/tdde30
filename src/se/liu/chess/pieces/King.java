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

	if (board.getPiece(1, owner.getHomeRank()) == null &&
	    board.getPiece(2, owner.getHomeRank()) == null &&
	    board.getPiece(3, owner.getHomeRank()) == null &&
	    !opponent.getAttackedSquares().contains(new Point(2, owner.getHomeRank())) &&
	    !opponent.getAttackedSquares().contains(new Point(3, owner.getHomeRank())) &&
	    !opponent.getAttackedSquares().contains(new Point(4, owner.getHomeRank()))) {
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

    private Set<Move> limitMovesToSafeSquares(final Board board, final Set<Move> initialMoveSet) {
	Set<Move> possibleMoves = new HashSet<>();

	for (Move move : initialMoveSet) {
	    if (!board.getInactivePlayer().getAttackedSquares().contains(move.getTargetSquare())) { //TODO breaks if king can move first move
		possibleMoves.add(move);
	    }
	}

	return possibleMoves;
    }

    private Set<Move> getCastlingMoves(final Board board) {
	Set<Move> possibleMoves = new HashSet<>();

	if (board.isInCheck(owner)) {
	    return possibleMoves;
	}

	Set<MoveCharacteristics> moveCharacteristics = new HashSet<>();
	moveCharacteristics.add(MoveCharacteristics.HARMLESS);
	moveCharacteristics.add(MoveCharacteristics.CASTLING);

	if (canCastleQueenside(board)) {
	    if (owner.getColor() == TeamColor.WHITE) {
		Move moveToAdd = new Move(new Point(4, 7), new Point(2, 7),
					  this, moveCharacteristics);
		possibleMoves.add(moveToAdd);
	    } else {
		Move moveToAdd = new Move(new Point(4, 0), new Point(2, 0),
					  this, moveCharacteristics);
		possibleMoves.add(moveToAdd);
	    }
	}
	if (canCastleKingside(board)) {
	    if (owner.getColor() == TeamColor.WHITE) {
		Move moveToAdd = new Move(new Point(4, 7), new Point(6, 7),
					  this, moveCharacteristics);
		possibleMoves.add(moveToAdd);
	    } else {
		Move moveToAdd = new Move(new Point(4, 0), new Point(6, 0),
					  this, moveCharacteristics);
		possibleMoves.add(moveToAdd);
	    }
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
