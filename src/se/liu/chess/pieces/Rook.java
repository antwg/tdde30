package se.liu.chess.pieces;

import se.liu.chess.game.Board;
import se.liu.chess.game.TeamColor;

import java.awt.*;
import java.util.List;

/**
 *
 */
public class Rook extends AbstractPiece
{
    public Rook(final TeamColor color) {
	super(color);
    }

    @Override public PieceType getType() {
	return PieceType.ROOK;
    }

    @Override public List<Point> getMoves(Board board, int x, int y) {
	return null;
    }

    @Override public String toString() {
	if (getColor() == TeamColor.WHITE) {
	    return "R";
	}
	return "r";
    }
}
