package se.liu.chess.pieces;

import se.liu.chess.game.Board;
import se.liu.chess.game.Move;
import se.liu.chess.game.Player;
import se.liu.chess.game.TeamColor;

import java.awt.*;
import java.util.Set;

/**
 * The Rook class extends VectorMovePiece and in turn AbstractPiece and Piece.
 * Overrides getMoves, getType and toString.
 * Can move in cardinal directions.
 */
public class Rook extends VectorMovePiece
{
    private Point[] rookMoveDirections = {new Point(1, 0),
	    				  new Point(0, 1),
	    				  new Point(-1, 0),
	    				  new Point(0, -1)};

    public Rook(final Player owner, final Point position) {
	super(owner, position);
    }

    // ----------------------------------------------------- Public Methods ----------------------------------------------------------------


    @Override public PieceType getType() {
	return PieceType.ROOK;
    }

    @Override public Set<Move> getMoves(Board board, int x, int y) {
	Set<Move> possibleMoves = getVectorMoves(board, x, y, rookMoveDirections);

	// Limit moves

	possibleMoves = limitMovesToThreatSquares(board, possibleMoves);

	possibleMoves = limitMovesToPinSquares(board, possibleMoves);

	return possibleMoves;
    }

    @Override public void checkMove(Move move, Point enPassantTarget, final Board board) {
        Player activePlayer = board.getActivePlayer();
	if (move.getOriginSquare().equals(activePlayer.getKingSideRookHomePosition())) {
	    activePlayer.setKingSideCastleAvailable(false);
	}
	else if(move.getOriginSquare().equals(activePlayer.getQueenSideRookHomePosition())){
	    activePlayer.setQueenSideCastleAvailable(false);
	}
    }

    @Override public String toString() {
	if (getColor() == TeamColor.WHITE) {
	    return "R";
	}
	return "r";
    }

}
