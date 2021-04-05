package se.liu.chess.pieces;

import se.liu.chess.game.Board;
import se.liu.chess.game.TeamColor;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class King extends AbstractPiece
{
    public King(final TeamColor color) {
	super(color);
    }

    @Override public PieceType getType() {
	return PieceType.KING;
    }

    @Override public List<Point> getMoves(Board board, int x, int y) {
	List<Point> list = new ArrayList<>();
	Piece piece = null;

	if (x < 7){
	    piece = board.getPiece(x + 1, y);
	    if(piece == null || piece.getColor() != color){
	    	list.add(new Point(x + 1, y));
	    }
	}
	if (x > 0){
	    piece = board.getPiece(x - 1, y);
	    if (piece == null || piece.getColor() != color){
	    list.add(new Point(x - 1, y));
	    }
	}
	if (y < 7){
	    piece = board.getPiece(x, y + 1);
	    if (piece == null || piece.getColor() != color){
		list.add(new Point(x, y + 1));
	    }
	}
	if (y > 0) {
	    piece = board.getPiece(x, y - 1);
	    if (piece == null || piece.getColor() != color) {
		list.add(new Point(x, y - 1));
	    }
	}

	return list;
    }

    @Override public String toString() {
	if (getColor() == TeamColor.WHITE) {
	    return "K";
	}
	return "k";
    }
}
