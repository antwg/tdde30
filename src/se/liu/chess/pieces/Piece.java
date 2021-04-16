package se.liu.chess.pieces;

import se.liu.chess.game.Board;
import se.liu.chess.game.Player;
import se.liu.chess.game.TeamColor;

import java.awt.*;
import java.util.Set;

/**
 * Piece is an interface for all different PieceTypes.
 */
public interface Piece
{
    public Set<Point> getMoves(Board board, int x, int y);

    public TeamColor getColor();

    public Player getOwner();

    public PieceType getType();

    public String toString();

    void setHasMoved(boolean b);

    boolean hasMoved();

}
