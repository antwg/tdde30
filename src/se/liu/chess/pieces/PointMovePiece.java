package se.liu.chess.pieces;

import se.liu.chess.game.Board;
import se.liu.chess.game.TeamColor;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public abstract class PointMovePiece extends AbstractPiece{

    protected Point[] allMoves = null;

    protected PointMovePiece(final TeamColor color) {
	super(color);
    }

    @Override public Set<Point> getMoves(final Board board, final int x, final int y) {
	Set<Point> legalMoves = new HashSet<>();

	for (Point move: allMoves) {
	    Point temp = new Point(x + move.x, y + move.y);
	    if (board.isValidTile(temp.x, temp.y) && (board.getPiece(temp.x, temp.y) == null || board.getPiece(temp.x, temp.y).getColor() != color)){
		legalMoves.add(temp);
	    }
	}
	return legalMoves;
    }
}
