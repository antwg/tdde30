package se.liu.chess.pieces;

import se.liu.chess.game.Board;
import se.liu.chess.game.TeamColor;

import java.util.List;

/**
 *
 */
public class Queen extends AbstractPiece
{
    public Queen(final TeamColor color) {
	super(color);
    }

    @Override public PieceType getType() {
	return PieceType.QUEEN;
    }

    @Override public List<Piece> getMoves(Board board) {
	return null;
    }

    @Override public String toString() {
	if (getColor() == TeamColor.WHITE) {
	    return "Q";
	}
	return "q";
    }
}
