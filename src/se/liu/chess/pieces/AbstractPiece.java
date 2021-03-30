package se.liu.chess.pieces;

import se.liu.chess.game.Board;
import se.liu.chess.game.Player;

import java.awt.*;

/**
 *
 */

public abstract class AbstractPiece implements Piece
{
    private Player owner;
    private boolean hasMoved = false;

    protected AbstractPiece(final Player owner) {
	this.owner = owner;
    }

    protected AbstractPiece() {
        this.owner = null;
    }

    protected boolean isLegalCoordinate(Point coordinate, Board board) {
        return true;
    }
}
