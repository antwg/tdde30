package se.liu.chess.pieces;

import se.liu.chess.game.Board;
import se.liu.chess.game.TeamColor;

import java.util.List;

import static se.liu.chess.game.TeamColor.BLACK;

/**
 *
 */
public class Bishop extends AbstractPiece
{
    public Bishop(final TeamColor color) {
	super(color);
    }

    @Override public List<Piece> getMoves(Board board) {
	return null;
    }

    @Override public PieceType getType() {
	return PieceType.BISHOP;
    }

    @Override public String toString() {
	if (getColor() == TeamColor.BLACK) {
	    return "B";
	}
	return "b";
    }
}
