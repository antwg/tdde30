package se.liu.chess.pieces;

import se.liu.chess.game.Board;
import se.liu.chess.game.Player;
import se.liu.chess.game.TeamColor;

import java.awt.*;

/**
 *
 */

public abstract class AbstractPiece implements Piece
{
    protected Player owner;
    protected boolean hasMoved = false;

    protected AbstractPiece(final Player owner) {
	this.owner = owner;
    }

    public TeamColor getColor() {
        return owner.getColor();
    }

    public Player getOwner() {
        return owner;
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    public void setHasMoved(final boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    protected boolean isLegalCoordinate(Point coordinate, Board board) {
        return true;
    }
}
