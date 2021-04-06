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
public class Pawn extends AbstractPiece
{
    public Pawn(final TeamColor color) {
	super(color);
    }

    @Override public PieceType getType() {
	return PieceType.PAWN;
    }

    @Override public Set<Point> getMoves(Board board, int x, int y) {
	List<Point> list = new ArrayList<>();
	return new HashSet<>();
    }

    @Override public String toString() {
	if (getColor() == TeamColor.WHITE) {
	    return "P";
	}
	return "p";
    }
}
