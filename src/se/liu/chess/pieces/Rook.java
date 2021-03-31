package se.liu.chess.pieces;

import se.liu.chess.game.Board;
import se.liu.chess.game.TeamColor;

import java.util.List;

/**
 *
 */
public class Rook extends AbstractPiece
{
    public Rook(final TeamColor color) {
	super(color);
    }

    @Override public List<Piece> getMoves(Board board) {
	return null;
    }
}
