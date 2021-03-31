package se.liu.chess.pieces;

import se.liu.chess.game.Board;
import se.liu.chess.game.TeamColor;

import java.util.List;

/**
 *
 */
public class King extends AbstractPiece
{
    public King(final TeamColor color) {
	super(color);
    }

    @Override public PieceType getType() {
	return PieceType.KING;
    }

    @Override public List<Piece> getMoves(Board board) {
	return null;
    }

    @Override public String toString() {
	if (getColor() == TeamColor.BLACK) {
	    return "K";
	}
	return "k";
    }
}
