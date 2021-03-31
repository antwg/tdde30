package se.liu.chess.pieces;

import se.liu.chess.game.Board;
import se.liu.chess.game.TeamColor;

import java.util.List;

/**
 *
 */
public interface Piece
{
    public List<se.liu.chess.game.Piece> getMoves(Board board);

    public TeamColor getColor();
}
