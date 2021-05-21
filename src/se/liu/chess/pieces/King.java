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

    // ----------------------------------------------------- Public Methods ----------------------------------------------------------------

    @Override public Set<Move> getMoves(final Board board, final int x, final int y) {
	Set<Move> possibleMoves = new HashSet<>();

	possibleMoves.addAll(getPointMoves(board, x, y, kingMoves));

	possibleMoves.addAll(getCastlingMoves(board));

	possibleMoves = limitMovesToSafeSquares(board, possibleMoves);

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

    @Override public void performSpecialMove(final Move move, Point enPassantTarget, final Board board) {
	Player activePlayer = board.getActivePlayer();
        activePlayer.setQueenSideCastleAvailable(false);
	activePlayer.setKingSideCastleAvailable(false);
    }

    // ----------------------------------------------------- Private Methods ---------------------------------------------------------------

    private Set<Move> limitMovesToSafeSquares(final Board board, final Set<Move> initialMoves) {
	Set<Move> possibleMoves = new HashSet<>();

	for (Move move : initialMoves) {
	    // check if not protected
	    if (board.getPiece(move.getTargetSquare()) == null &&
		!board.getInactivePlayer().getAttackedSquares().contains(move.getTargetSquare())) {
		possibleMoves.add(move);
	    } else if (board.getPiece(move.getTargetSquare()) != null &&
		       !board.isPieceProtected(move.getTargetSquare())) {
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

	Set<MoveCharacteristics> moveCharacteristics = EnumSet.noneOf(MoveCharacteristics.class);
	moveCharacteristics.add(MoveCharacteristics.HARMLESS);
	moveCharacteristics.add(MoveCharacteristics.CASTLING);

	if (canCastleQueenSide(board)) {
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

    //TODO fix hardcoded ugliness, duplicate code and bad naming
    private boolean canCastleQueenSide(Board board) {
	Player opponent = board.getOpponentPlayer(owner);

	if (!owner.canCastleQueenSide()) {
	    return false;
	}

	final int homeRank = owner.getHomeRank();
	final int columnOne = 1, columnTwo = 2, columnThree = 3;
	if (board.getPiece(columnOne, homeRank) == null &&
	    board.getPiece(columnTwo, homeRank) == null &&
	    board.getPiece(columnThree, homeRank) == null &&
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

	final int homeRank = owner.getHomeRank();
	final int columnFive = 5, columnSix = 2;
	if (board.getPiece(columnFive, homeRank) == null &&
	    board.getPiece(columnSix, homeRank) == null &&
	    !opponent.getAttackedSquares().contains(new Point(4, homeRank)) &&
	    !opponent.getAttackedSquares().contains(new Point(5, homeRank)) &&
	    !opponent.getAttackedSquares().contains(new Point(6, homeRank))) {
	    return true;
	}
	return false;
    }
}
