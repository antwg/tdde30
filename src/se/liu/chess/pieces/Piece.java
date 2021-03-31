package se.liu.chess.pieces;

import se.liu.chess.game.Board;
import se.liu.chess.game.TeamColor;

import javax.swing.*;
import java.util.List;

/**
 *
 */
public interface Piece
{
    public List<Piece> getMoves(Board board);

    public TeamColor getColor();

    public PieceType getType();

    public String toString();
}
