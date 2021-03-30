package se.liu.chess.game;

import se.liu.chess.pieces.Piece;

public class Board
{
    Square[][] squares;
    int width;
    int height;

    public Board(final int width, final int height) {
	this.width = width;
	this.height = height;
	this.squares = new Square[width][height];

	for (int y = 0; y < height; y++) {
	    for (int x = 0; x < width; x++) {
		squares[y][x] = new Square(x, y);
	    }
	}
    }

    // ---------------------------------------------------- Getters/Setters ----------------------------------------------------------------

    public int getWidth() {
	return width;
    }

    public int getHeight() {
	return height;
    }

    public Square getSquare(int x, int y) {
	return squares[y][x];
    }

    public Piece getPiece(int x, int y) {
        return getSquare(x, y).getPiece();
    }

    public void setPiece(int x, int y, Piece piece) {
	getSquare(x, y).setPiece(piece);
    }

    // ----------------------------------------------------- Public Methods ----------------------------------------------------------------


    public void movePiece(int x1, int y1, int x2, int y2) {
	getSquare(x2, y2).setPiece(getPiece(x1, y1));
	getSquare(x1, y1).setPiece(null);
    }

    public void printBoard() {
	for (int y = 0; y < getWidth(); y++) {
	    for (int x = 0; x < getHeight(); x++) {
		System.out.print(getPiece(x, y) + " ");
	    }
	    System.out.println("");
	}
    }

    public static void main(String[] args) {
        Board board = new Board(8, 8);

	board.printBoard();
    }
}
