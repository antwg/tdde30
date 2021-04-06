package se.liu.chess.pieces;

import se.liu.chess.game.Board;
import se.liu.chess.game.TeamColor;

import java.awt.*;

/**
 *
 */

public abstract class AbstractPiece implements Piece
{
    protected TeamColor color;
    protected boolean hasMoved = false;

    protected AbstractPiece(final TeamColor color) {
	this.color = color;
    }

    public TeamColor getColor() {
        return color;
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
