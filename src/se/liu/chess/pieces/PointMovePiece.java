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
	    int tempX = x + move.x;
	    int tempY = y + move.y;

	    if (board.isValidTile(tempX, tempY)) {
		Piece piece = board.getPiece(tempX, tempY);
		if (piece == null || piece.getColor() != color) {
		    legalMoves.add(new Point(tempX, tempY));
		}
	    }
	}
	return legalMoves;
    }
}
