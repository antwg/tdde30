package se.liu.chess.game;

import se.liu.chess.pieces.Piece;

/**
 * Creates a board which handles all game logic and saves the position of pieces. Has methods for moving pieces.
 *
 * @param squares The board itself, saves the location of all pieces.
 * @param width The width of the board.
 * @param height The height of the board.
 */

public class Board
{
    private Square[][] squares;
    private int width;
    private int height;

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
		switch (getPiece(x, y)) {
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
