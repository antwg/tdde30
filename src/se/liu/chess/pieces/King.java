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
	    if (!board.isSquareProtectedByPlayer(move.getTargetSquare(), board.getOpponentPlayer(owner))) {
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
	    final int homeRank = owner.getHomeRank();
	    final int fileC = 2, fileE = 4;

	    Point originSquare = new Point(fileE, homeRank);
	    Point targetSquare = new Point(fileC, homeRank);

	    Move moveToAdd = new Move(originSquare, targetSquare, this, moveCharacteristics);
	    possibleMoves.add(moveToAdd);
	}

	if (canCastleKingside(board)) {
	    final int homeRank = owner.getHomeRank();
	    final int fileE = 4, fileG = 6;

	    Point originSquare = new Point(fileE, homeRank);
	    Point targetSquare = new Point(fileG, homeRank);

	    Move moveToAdd = new Move(originSquare, targetSquare, this, moveCharacteristics);
	    possibleMoves.add(moveToAdd);
	}
	return possibleMoves;
    }
    
    private boolean canCastleQueenSide(Board board) {
	if (!owner.isQueenSideCastleAvailable()) {
	    return false;
	}

	final int homeRank = owner.getHomeRank();
	final int fileB = 1, fileC = 2, fileD = 3, fileE = 4;

	return (board.getPiece(fileB, homeRank) == null &&
		board.getPiece(fileC, homeRank) == null &&
		board.getPiece(fileD, homeRank) == null &&
		squaresAreNotThreatened(board, homeRank, fileC, fileD));
    }

    private boolean canCastleKingside(Board board) {
	if (!owner.isKingSideCastleAvailable()) {
	    return false;
	}

	final int homeRank = owner.getHomeRank();
	final int fileE = 4, fileF = 5, fileG = 6;

	return (board.getPiece(fileF, homeRank) == null && board.getPiece(fileG, homeRank) == null &&
		squaresAreNotThreatened(board, homeRank, fileF, fileG));
    }

    private boolean squaresAreNotThreatened(final Board board, final int homeRank, final int fileOne, final int fileTwo) {
        final int fileE = 4;
	Player opponent = board.getOpponentPlayer(owner);

	return (!opponent.getAttackedSquares().contains(new Point(fileE, homeRank)) &&
	       !opponent.getAttackedSquares().contains(new Point(fileOne, homeRank)) &&
	       !opponent.getAttackedSquares().contains(new Point(fileTwo, homeRank)));
    }
}
