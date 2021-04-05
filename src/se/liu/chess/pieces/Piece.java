package se.liu.chess.pieces;

import se.liu.chess.game.Board;
import se.liu.chess.game.TeamColor;

import java.awt.*;
import java.util.List;

/**
 *
 */
public interface Piece
{
    public List<Point> getMoves(Board board, int x, int y);

    public TeamColor getColor();

    public PieceType getType();

    public String toString();
}
