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
public class Queen extends AbstractPiece
{
    public Queen(final TeamColor color) {
	super(color);
    }

    @Override public PieceType getType() {
	return PieceType.QUEEN;
    }

    @Override public Set<Point> getMoves(Board board, int x, int y) {
	Set<Point> list = new HashSet<>();
	return list;
    }

    @Override public String toString() {
	if (getColor() == TeamColor.WHITE) {
	    return "Q";
	}
	return "q";
    }
}
