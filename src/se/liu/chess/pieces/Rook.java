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
    private Point[] allMoveDirections = {new Point(1, 0),
	    				 new Point(0, 1),
	    				 new Point(-1, 0),
	    				 new Point(0, -1)};

    public Rook(final TeamColor color) {
	super(color);
    }

    @Override public PieceType getType() {
	return PieceType.ROOK;
    }

    @Override public Set<Point> getMoves(Board board, int x, int y) {
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

    @Override public String toString() {
	if (getColor() == TeamColor.WHITE) {
	    return "R";
	}
	return "r";
    }
}
