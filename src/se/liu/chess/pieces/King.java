package se.liu.chess.pieces;

import se.liu.chess.game.Board;
import se.liu.chess.game.TeamColor;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

/**
 *
 */
public class King extends AbstractPiece
{
    private Point[] allMoves = {new Point(1, 0), new Point(-1, 0), new Point(1, 1), new Point(1, -1),
	    			new Point(0, 1), new Point(0, -1), new Point(-1, 1), new Point(-1, -1) };

    public King(final TeamColor color) {
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

    @Override public PieceType getType() {
	return PieceType.KING;
    }

    @Override public String toString() {
	if (getColor() == TeamColor.WHITE) {
	    return "K";
	}
	return "k";
    }

    /*    private Point[] allMoves =
	    { new Point(1, 0), new Point(-1, 0), new Point(1, 1), new Point(1, -1), new Point(0, 1), new Point(0, -1), new Point(-1, 1),
		    new Point(-1, -1) };*/
}
