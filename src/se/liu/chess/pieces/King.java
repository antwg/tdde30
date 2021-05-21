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
	    Point p1 = new Point(4, 0);
	    Point p2 = new Point(2, 0);

	    if (owner.getColor() == TeamColor.WHITE) {
		p1 = new Point(4, 7);
		p2 = new Point(2, 7);
	    }
	    Move moveToAdd = new Move(p1, p2, this, moveCharacteristics);
	    possibleMoves.add(moveToAdd);
	}

	if (canCastleKingside(board)) {
	    Point p1 = new Point(4, 0);
	    Point p2 = new Point(6, 0);

	    if (owner.getColor() == TeamColor.WHITE){
		p1 = new Point(4, 7);
		p2 = new Point(6, 7);
	    }
	    Move moveToAdd = new Move(p1, p2, this, moveCharacteristics);
	    possibleMoves.add(moveToAdd);
	}
	return possibleMoves;
    }

    //TODO fix hardcoded ugliness, duplicate code and bad naming
    private boolean canCastleQueenSide(Board board) {
	Player opponent = board.getOpponentPlayer(owner);

	if (!owner.isQueenSideCastleAvailable()) {
	    return false;
	}

	final int homeRank = owner.getHomeRank();
	final int fileB = 1, fileC = 2, fileD = 3, fileE = 4;

	if (board.getPiece(fileB, homeRank) == null &&
	    board.getPiece(fileC, homeRank) == null &&
	    board.getPiece(fileD, homeRank) == null &&
	    !opponent.getAttackedSquares().contains(new Point(fileC, homeRank)) &&
	    !opponent.getAttackedSquares().contains(new Point(fileD, homeRank)) &&
	    !opponent.getAttackedSquares().contains(new Point(fileE, homeRank)))
	{
	    return true;
	}
	return false;
    }

    //TODO fix hardcoded ugliness, duplicate code and bad naming
    private boolean canCastleKingside(Board board) {
	Player opponent = board.getOpponentPlayer(owner);

	if (!owner.isKingSideCastleAvailable()) {
	    return false;
	}

	final int homeRank = owner.getHomeRank();
	final int fileE = 4, fileF = 5, fileG = 6;

	if (board.getPiece(fileF, homeRank) == null &&
	    board.getPiece(fileG, homeRank) == null &&
	    !opponent.getAttackedSquares().contains(new Point(fileE, homeRank)) &&
	    !opponent.getAttackedSquares().contains(new Point(fileF, homeRank)) &&
	    !opponent.getAttackedSquares().contains(new Point(fileG, homeRank)))
	{
	    return true;
	}
	return false;
    }
}
