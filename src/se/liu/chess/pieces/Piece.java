package se.liu.chess.pieces;

import se.liu.chess.game.Board;
import se.liu.chess.game.Square;
import se.liu.chess.game.TeamColor;

import java.util.List;

/**
 *
 */
public interface Piece
{
    public List<Square> getMoves(Board board);

    public TeamColor getColor();
}
