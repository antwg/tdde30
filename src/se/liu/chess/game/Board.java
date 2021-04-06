package se.liu.chess.game;

import se.liu.chess.pieces.Piece;

import java.awt.*;

import se.liu.chess.pieces.Rook;
import se.liu.chess.pieces.Knight;
import se.liu.chess.pieces.Bishop;
import se.liu.chess.pieces.King;
import se.liu.chess.pieces.Queen;
import se.liu.chess.pieces.Pawn;

/**
 * Creates a board which handles all game logic and saves the position of pieces. Has methods for moving pieces.
 */
public class Board
{
    private Piece[][] pieces;
    private int width;
    private int height;

    private Player whitePlayer;
    private Player blackPlayer;

    private int activePlayerIndex = 0;

    private Point enPassantTarget = null;

    //TODO implement halfmoveClock
    private int halfmoveClock = 0;	// Used for 50 move rule
    private int fullmoveNumber = 1; //TODO inspect vill separera half/full-Move ex half_move

    // Constructor
    public Board(final int width, final int height) {
	this.width = width;
	this.height = height;
	this.pieces = new Piece[width][height];

	for (int y = 0; y < height; y++) {
	    for (int x = 0; x < width; x++) {
		pieces[y][x] = null;
	    }
	}

	this.whitePlayer = new Player(TeamColor.WHITE, 300);
	this.blackPlayer = new Player(TeamColor.BLACK, 300);

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

    public Player getActivePlayer() {
	if (activePlayerIndex == 0) {
	    return whitePlayer;
	} else {
	    return blackPlayer;
	}
    }

    public int getHalfmoveClock() {
	return halfmoveClock;
    }

    public int getFullmoveNumber() {
	return fullmoveNumber;
    }

    public void setEnPassantTarget(final Point enPassantTarget) {
	this.enPassantTarget = enPassantTarget;
    }

    public void setEnPassantTarget(int x, int y) {
	this.enPassantTarget = new Point(x, y);
    }

    public Point getEnPassantTarget() {
	return enPassantTarget;
    }

    // ----------------------------------------------------- Public Methods ----------------------------------------------------------------

    public void movePiece(Point p1, Point p2) {
	setPiece(p2, getPiece(p1));
	setPiece(p1, null);
    }

    public boolean isValidTile(int x, int y) {
	return (0 <= x && x < width && 0 <= y && y < height);
    }

    public void printBoard() {
	for (int y = 0; y < getWidth(); y++) {
	    for (int x = 0; x < getHeight(); x++) {
	        Piece piece = getPiece(x, y);
		if (piece == null) {
		    System.out.print("- ");
		} else {
		    System.out.print(piece + " ");
		}
	    }
	    System.out.print("\n");
	}
    }
    //TODO implement function

    public boolean isInCheck(Player player) {
	return false;
    }
    //TODO implement function

    public boolean hasLegalMoves(Player player) {
        return true;
    }

    public boolean isGameOver(Player player) {
	if (!hasLegalMoves(player)) {
	    if (isInCheck(player)) {
	        // Checkmate detected
		System.out.println("Checkmate!");
	    } else {
	        // Stalemate detected
		System.out.println("Stalemate!");
	    }
	    return true;
	}
	return false;
    }

    public void resetBoard() { // TODO Sätt alla andra till null?
        boardFromFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
    }
    //TODO split into multiple smaller methods

    /**
     * https://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation
     * @return
     */
    public String boardToFEN() {
	final StringBuilder builder = new StringBuilder();

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
	if (getActivePlayer().getColor() == TeamColor.WHITE) {
	    builder.append("w");
	} else {
	    builder.append("b");
	}
	builder.append(" ");

	// 3. Castling availability
	//TODO add castling availability
	if (whitePlayer.canCastleKingside()) {
	    builder.append("K");
	}
	if (whitePlayer.canCastleQueenside()) { //TODO inspect vill separera queenside
	    builder.append("Q");
	}
	if (blackPlayer.canCastleKingside()) {
	    builder.append("k");
	}
	if (blackPlayer.canCastleQueenside()) { //TODO inspect vill separera queenside
	    builder.append("q");
	}
	builder.append(" ");

	// 4. En passant availability
	if (enPassantTarget == null) {
	    builder.append("-");
	} else {
	    builder.append(positionToNotation(enPassantTarget));
	}
	builder.append(" ");

	// 5. Halfmove clock
	builder.append(halfmoveClock);
	builder.append(" ");

	// 6. Fullmove number
	builder.append(fullmoveNumber);
	builder.append(" ");

	return builder.toString();
    }

    public void boardFromFEN(final String fen) {
        int x = 0;
        int y = 0;
	for (int i = 0; i < fen.length(); i++) {
	    char curr = fen.charAt(i);

	    //TODO replace with piece construction method
	    switch (curr) {
		case ' ':
		    return;
		case '/':
		    y++;
		    x = 0;
		    break;
		case 'r':
		    setPiece(x, y, new Rook(TeamColor.BLACK));
		    x++;
		    break;
		case 'R':
		    setPiece(x, y, new Rook(TeamColor.WHITE));
		    x++;
		    break;
		case 'n':
		    setPiece(x, y, new Knight(TeamColor.BLACK));
		    x++;
		    break;
		case 'N':
		    setPiece(x, y, new Knight(TeamColor.WHITE));
		    x++;
		    break;
		case 'b':
		    setPiece(x, y, new Bishop(TeamColor.BLACK));
		    x++;
		    break;
		case 'B':
		    setPiece(x, y, new Bishop(TeamColor.WHITE));
		    x++;
		    break;
		case 'k':
		    setPiece(x, y, new King(TeamColor.BLACK));
		    x++;
		    break;
		case 'K':
		    setPiece(x, y, new King(TeamColor.WHITE));
		    x++;
		    break;
		case 'q':
		    setPiece(x, y, new Queen(TeamColor.BLACK));
		    x++;
		    break;
		case 'Q':
		    setPiece(x, y, new Queen(TeamColor.WHITE));
		    x++;
		    break;
		case 'p':
		    setPiece(x, y, new Pawn(TeamColor.BLACK));
		    x++;
		    break;
		case 'P':
		    setPiece(x, y, new Pawn(TeamColor.WHITE));
		    x++;
		    break;
		default:
		    x += Character.getNumericValue(curr);
	    }
	}
    }

    private String positionToNotation(final Point p) {
	final String alphas = "abcdefghijklmnopqrstuvwxyz?";
	return alphas.substring(p.x, p.x + 1) + (p.y + 1);
    }
}
