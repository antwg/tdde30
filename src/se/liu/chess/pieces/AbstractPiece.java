package se.liu.chess.pieces;

import se.liu.chess.game.Player;

public abstract class AbstractPiece implements Piece
{
    Player owner;
    boolean hasMoved = false;

    protected AbstractPiece(final Player owner) {
	this.owner = owner;
    }

    protected  AbstractPiece() {
        this.owner = null;
    }
}
