package se.liu.chess.game;

import java.awt.*;

/**
 * Creates a board which handles all game logic and saves the position of pieces. Has methods for moving pieces.
 *
 * @param squares The board itself, saves the location of all pieces.
 * @param width The width of the board.
 * @param height The height of the board.
 */

public class Board
{
    private Piece[][] pieces;
    private int width;
    private int height;

    public Board(final int width, final int height) {
	this.width = width;
	this.height = height;
	this.pieces = new Piece[width][height];

	for (int y = 0; y < height; y++) {
	    for (int x = 0; x < width; x++) {
		pieces[y][x] = new Piece(x, y);
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

    public Piece getPiece(int x, int y) {
        return pieces[y][x];
    }

    public Piece getPiece(Point coordinate) {
	return getPiece(coordinate.x, coordinate.y);
    }

    public void setPiece(int x, int y, Piece piece) {
	pieces[y][x] = piece;
    }

    public void setPiece(Point coordinate, Piece piece) {
	setPiece(coordinate.x, coordinate.y, piece);
    }

    // ----------------------------------------------------- Public Methods ----------------------------------------------------------------


    public void movePiece(Point p1, Point p2) {
	setPiece(p2, getPiece(p1));
	setPiece(p1, null);
    }

    public void printBoard() {
	for (int y = 0; y < getWidth(); y++) {
	    for (int x = 0; x < getHeight(); x++) {
		System.out.print(getPiece(x, y) + " ");
	    }
	    System.out.println("");
	}
    }

    /**
     * https://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation
     * @return
     */

    /*
    public String boardStateToFEN() {
	StringBuilder builder = new StringBuilder();

	// 1. Piece placement
	for (int y = 0; y < height; y++) {
	    int emptySquaresInARow = 0;

	    for (int x = 0; x < width; x++) {
		switch (getPiece(x, y).getColor()) {
		    case :

		    default:
		        emptySquaresInARow++;
		}
	    }
	    builder.append("/");
	}
	builder.append(" ");
	// 2. Active color
	builder.append(" ");
	// 3. Castling availability
	builder.append(" ");
	// 4. En passant availability
	builder.append(" ");
	// 5. Halfmove clock
	builder.append(" ");
	// 6. Fullmove number
	builder.append(" ");

	return
    } */

    public static void main(String[] args) {
        Board board = new Board(8, 8);

	board.printBoard();
    }
}
