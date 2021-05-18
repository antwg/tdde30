package se.liu.chess.pieces;

import se.liu.chess.game.Board;
import se.liu.chess.game.Move;
import se.liu.chess.game.Player;
import se.liu.chess.game.TeamColor;

import java.awt.*;
import java.util.Set;

/**
 * Piece is an interface for all different PieceTypes.
 */
public interface Piece
{
    public Set<Move> getMoves(Board board, int x, int y);

    public Player getOwner();

    public TeamColor getColor();

    public PieceType getType();

    public boolean hasMoved();

    public void setHasMoved(boolean b);

    public Point getPosition();

    public void setPosition(Point p);

    public String toString();

    public abstract void checkMove(Move move, Point enPassantTarget, final Board board);

}
