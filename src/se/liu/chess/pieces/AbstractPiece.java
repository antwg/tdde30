package se.liu.chess.pieces;

import se.liu.chess.game.Board;
import se.liu.chess.game.TeamColor;

import java.awt.*;

/**
 *
 */

public abstract class AbstractPiece implements Piece
{
    private TeamColor color;
    private boolean hasMoved = false;

    protected AbstractPiece(final TeamColor color) {
	this.color = color;
    }

    public TeamColor getColor() {
        return color;
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    protected boolean isLegalCoordinate(Point coordinate, Board board) {
        return true;
    }
}
