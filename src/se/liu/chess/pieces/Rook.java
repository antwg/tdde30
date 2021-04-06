package se.liu.chess.pieces;

import se.liu.chess.game.Board;
import se.liu.chess.game.TeamColor;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @Override public Set<Point> getMoves(Board board, int x, int y) {
	Set<Point> list = new HashSet<>();
	return list;
    }

    @Override public String toString() {
	if (getColor() == TeamColor.WHITE) {
	    return "R";
	}
	return "r";
    }
}
