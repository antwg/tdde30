package se.liu.chess.game;

import se.liu.chess.pieces.Piece;

/**
 *
 */

public class Piece
{
    private se.liu.chess.pieces.Piece piece;

    public Piece(int x, int y) {
	this.piece = null;
    }

    public se.liu.chess.pieces.Piece getPiece() {
	return piece;
    }

    public void setPiece(final se.liu.chess.pieces.Piece piece) {
	this.piece = piece;
    }
}
