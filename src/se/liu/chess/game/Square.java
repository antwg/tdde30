package se.liu.chess.game;

import se.liu.chess.pieces.Piece;

public class Square
{
    Piece piece;

    public Square(int x, int y) {
	this.piece = null;
    }

    public Piece getPiece() {
	return piece;
    }

    public void setPiece(final Piece piece) {
	this.piece = piece;
    }
}
