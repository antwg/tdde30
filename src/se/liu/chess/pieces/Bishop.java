package se.liu.chess.pieces;

import se.liu.chess.game.Board;
import se.liu.chess.game.TeamColor;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

import static se.liu.chess.game.TeamColor.BLACK;

/**
 *
 */
public class Bishop extends AbstractPiece
{
    public Bishop(final TeamColor color) {
	super(color);
    }

    @Override public Set<Point> getMoves(Board board, int x, int y) {
	Set<Point> list = new HashSet<>();
	return list;
    }

    @Override public PieceType getType() {
	return PieceType.BISHOP;
    }

    @Override public String toString() {
	if (getColor() == TeamColor.WHITE) {
	    return "B";
	}
	return "b";
    }
}
