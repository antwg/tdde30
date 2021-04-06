package se.liu.chess.pieces;

import se.liu.chess.game.Board;
import se.liu.chess.game.TeamColor;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public abstract class VectorMovePiece extends AbstractPiece{

    protected Point[] allMoveDirections = null;

    protected VectorMovePiece(final TeamColor color) {
	super(color);
    }

    @Override public Set<Point> getMoves(final Board board, final int x, final int y) {
	Set<Point> legalMoves = new HashSet<>();

	for (Point direction : allMoveDirections) {
	    int tempX = x + direction.x;
	    int tempY = y + direction.y;

	    while (true) {
		if (!board.isValidTile(tempX, tempY)) {
		    break;
		} else if (board.getPiece(tempX, tempY) == null) {
		    legalMoves.add(new Point(tempX, tempY));
		} else if (board.getPiece(tempX, tempY).getColor() == this.getColor()) {
		    break;
		} else if (board.getPiece(tempX, tempY).getColor() != this.getColor()) {
		    legalMoves.add(new Point(tempX, tempY));
		    break;
		} else {
		    System.out.println("Code should not get here");
		}
		tempX += direction.x;
		tempY += direction.y;
	    }
	}

	return legalMoves;
    }
}