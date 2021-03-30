package se.liu.chess.pieces;

import se.liu.chess.game.Board;
import se.liu.chess.game.Square;
import se.liu.chess.game.TeamColor;

import java.util.List;

/**
 *
 */
public class Knight extends AbstractPiece
{
    protected Knight(final TeamColor color) {
	super(color);
    }

    @Override public List<Square> getMoves(Board board) {
	return null;
    }
}