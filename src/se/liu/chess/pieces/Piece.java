package se.liu.chess.pieces;

import se.liu.chess.game.Board;
import se.liu.chess.game.TeamColor;

import java.awt.*;
import java.util.List;
import java.util.Set;

/**
 *
 */
public interface Piece
{
    public Set<Point> getMoves(Board board, int x, int y);

    public TeamColor getColor();

    public PieceType getType();

    public String toString();
}
