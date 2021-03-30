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
    private TeamColor color = null;

    private boolean hasMoved = false;

    protected AbstractPiece(final TeamColor color) {
	this.color = color;
    }

//    protected AbstractPiece() {
//    }

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
