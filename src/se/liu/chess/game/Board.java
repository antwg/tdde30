package se.liu.chess.game;

import se.liu.chess.pieces.Piece;

import java.awt.*;

import se.liu.chess.pieces.Bishop;
import se.liu.chess.pieces.King;
import se.liu.chess.pieces.Knight;
import se.liu.chess.pieces.Pawn;
import se.liu.chess.pieces.Piece;
import se.liu.chess.pieces.Queen;
import se.liu.chess.pieces.Rook;

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
		pieces[y][x] = null;
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

    public Piece getPiece(Point p) {
	return getPiece(p.x, p.y);
    }

    public void setPiece(int x, int y, Piece piece) {
	pieces[y][x] = piece;
    }

    public void setPiece(Point p, Piece piece) {
	setPiece(p.x, p.y, piece);
    }

    public boolean isEmpty(int x, int y) {
        return getPiece(x, y) == null;
    }

    public boolean isEmpty(Point p) {
	return getPiece(p.x, p.y) == null;
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

    public void resetBoard() {
        //White
	setPiece(0, 0, new Rook(TeamColor.WHITE));
	setPiece(1, 0, new Knight(TeamColor.WHITE));
	setPiece(2, 0, new Bishop(TeamColor.WHITE));
	setPiece(3, 0, new King(TeamColor.WHITE));
	setPiece(4, 0, new Queen(TeamColor.WHITE));
	setPiece(5, 0, new Bishop(TeamColor.WHITE));
	setPiece(6, 0, new Knight(TeamColor.WHITE));
	setPiece(7, 0, new Rook(TeamColor.WHITE));

	setPiece(0, 1, new Pawn(TeamColor.WHITE));
	setPiece(1, 1, new Pawn(TeamColor.WHITE));
	setPiece(2, 1, new Pawn(TeamColor.WHITE));
	setPiece(3, 1, new Pawn(TeamColor.WHITE));
	setPiece(4, 1, new Pawn(TeamColor.WHITE));
	setPiece(5, 1, new Pawn(TeamColor.WHITE));
	setPiece(6, 1, new Pawn(TeamColor.WHITE));
	setPiece(7, 1, new Pawn(TeamColor.WHITE));

	//Black
	setPiece(0, 7, new Rook(TeamColor.BLACK));
	setPiece(1, 7, new Knight(TeamColor.BLACK));
	setPiece(2, 7, new Bishop(TeamColor.BLACK));
	setPiece(3, 7, new King(TeamColor.BLACK));
	setPiece(4, 7, new Queen(TeamColor.BLACK));
	setPiece(5, 7, new Bishop(TeamColor.BLACK));
	setPiece(6, 7, new Knight(TeamColor.BLACK));
	setPiece(7, 7, new Rook(TeamColor.BLACK));

	setPiece(0, 6, new Pawn(TeamColor.BLACK));
	setPiece(1, 6, new Pawn(TeamColor.BLACK));
	setPiece(2, 6, new Pawn(TeamColor.BLACK));
	setPiece(3, 6, new Pawn(TeamColor.BLACK));
	setPiece(4, 6, new Pawn(TeamColor.BLACK));
	setPiece(5, 6, new Pawn(TeamColor.BLACK));
	setPiece(6, 6, new Pawn(TeamColor.BLACK));
	setPiece(7, 6, new Pawn(TeamColor.BLACK));
    }

    /**
     * https://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation
     * @return
     */

    public String boardStateToFEN() {
	StringBuilder builder = new StringBuilder();

	// 1. Piece placement
	for (int y = 0; y < height; y++) {
	    int emptySquaresInARow = 0;
	    for (int x = 0; x < width; x++) {
		if (isEmpty(x, y)) {
		    emptySquaresInARow++;
		} else {
		    if (emptySquaresInARow != 0) {
			builder.append(emptySquaresInARow);
			emptySquaresInARow = 0;
		    }
		    builder.append(getPiece(x, y));
		}
	    }
	    if (emptySquaresInARow != 0) {
		builder.append(emptySquaresInARow);
		emptySquaresInARow = 0;
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

	return builder.toString();
    }

    public static void main(String[] args) {
        Board board = new Board(8, 8);

	board.printBoard();
    }
}
