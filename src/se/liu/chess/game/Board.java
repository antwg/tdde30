package se.liu.chess.game;

import se.liu.chess.pieces.Piece;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

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

    private int whiteStartTime = 300;
    private int blackStartTime = 300;
    private int whiteIncrement = 1;
    private int blackIncrement = 1;

    private Set<Point> whitePossibleMoves = new HashSet<>();
    private Set<Point> blackPossibleMoves = new HashSet<>();

    private int activePlayerIndex = 0;

    private Point enPassantTarget = null;

    //TODO implement halfmoveClock
    private int halfmoveClock = 0;        // Used for 50 move rule
    private int fullmoveNumber = 1; // It's called halfmove as one word

    public Board(final int width, final int height) {
	this.width = width;
	this.height = height;
	this.pieces = new Piece[width][height];

	clearBoard();

	this.whitePlayer = new Player(TeamColor.WHITE, whiteStartTime, whiteIncrement);
	this.blackPlayer = new Player(TeamColor.BLACK, blackStartTime, blackIncrement);

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

    public Player getWhitePlayer() {
	return whitePlayer;
    }

    public Player getBlackPlayer() {
	return blackPlayer;
    }

    public int getActivePlayerIndex() {
	return activePlayerIndex;
    }

    public Set<Point> getPossibleMoves(TeamColor teamColor) {
	if (teamColor == TeamColor.WHITE) {
	    return whitePossibleMoves;
	}
	return blackPossibleMoves;
    }

    public Set<Point> getWhitePossibleMoves() {
	return whitePossibleMoves;
    }

    public Set<Point> getBlackPossibleMoves() {
	return blackPossibleMoves;
    }

    public void setWhiteStartTime(final int whiteStartTime) {
	this.whiteStartTime = whiteStartTime;
    }

    public void setBlackStartTime(final int blackStartTime) {
	this.blackStartTime = blackStartTime;
    }

    public void setWhiteIncrement(final int whiteIncrement) {
	this.whiteIncrement = whiteIncrement;
    }

    public void setBlackIncrement(final int blackIncrement) {
	this.blackIncrement = blackIncrement;
    }

    // ----------------------------------------------------- Public Methods ----------------------------------------------------------------

    public void movePiece(Point p1, Point p2) {
	setPiece(p2, getPiece(p1));
	setPiece(p1, null);
    }

    /**
     * Returns true if given coordinates lie within the board, otherwise false.
     * @param x
     * @param y
     * @return
     */
    public boolean isValidTile(int x, int y) {
	return (0 <= x && x < width && 0 <= y && y < height);
    }

    //TODO implement function

    public boolean isInCheck(Player player) {
        TeamColor enemyColor;

	if (player.getColor() == TeamColor.WHITE) {
	    enemyColor = TeamColor.BLACK;
	} else {
	    enemyColor = TeamColor.WHITE;
	}

	for (Point threatenedSquare : getPossibleMoves(enemyColor)) {
	    Piece pieceOnSquare = getPiece(threatenedSquare);
	    if (pieceOnSquare != null && pieceOnSquare.equals(player.getKing())) {
		return true;
	    }
	}
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

    public void resetBoard() {
	clearBoard();
	getBoardFromFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
	getBlackPlayer().setTimeLeft(blackStartTime);
	getWhitePlayer().setTimeLeft(whiteStartTime);
    }

    /**
     * https://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation
     *
     * @return
     */
    public String convertBoardToFEN() {
	final StringBuilder builder = new StringBuilder();

	convertPiecesToFEN(builder);
	convertActivePlayerToFEN(builder);
	convertCastlingAbilityToFEN(builder); //TODO add castling availability
	convertEnPassantAvailabilityToFEN(builder);
	convertMoveToFEN(builder);

	return builder.toString();
    }


    public void getBoardFromFEN(final String fen) {
	// Split fen string
	String[] arrOfString = fen.split(" ");

	placePiecesFromFEN(arrOfString[0]);
	setActivePlayerFromFEN(arrOfString[1]);
	setCastlingAvailabilityFromFEN(arrOfString[2]);
	setEnPassantTargetFromFEN(arrOfString[3]);
	setMovesFromFEN(arrOfString[4], arrOfString[5]);
    }

    // ----------------------------------------------------- Private Methods ---------------------------------------------------------------


    //                                                   --- Convert to FEN ---


    private void convertPiecesToFEN(StringBuilder builder) {
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
    }

    private void convertActivePlayerToFEN(StringBuilder builder) {
	if(getActivePlayer().getColor() ==TeamColor.WHITE) {
	    builder.append("w");
	}
	else {
	    builder.append("b");
	}
	builder.append(" ");
    }

    private void convertCastlingAbilityToFEN(StringBuilder builder){
	if (whitePlayer.canCastleKingside()) {
	    builder.append("K");
	}
	if (whitePlayer.canCastleQueenside()) { // It's written as one word
	    builder.append("Q");
	}
	if (blackPlayer.canCastleKingside()) {
	    builder.append("k");
	}
	if (blackPlayer.canCastleQueenside()) {
	    builder.append("q");
	}
	builder.append(" ");
    }

    private void convertEnPassantAvailabilityToFEN(StringBuilder builder){
	if (enPassantTarget == null) {
	    builder.append("-");
	} else {
	    builder.append(convertPositionToNotation(enPassantTarget));
	}
	builder.append(" ");
    }

    private void convertMoveToFEN(StringBuilder builder){
	// 5. Halfmove clock
	builder.append(halfmoveClock);
	builder.append(" ");

	// 6. Fullmove number
	builder.append(fullmoveNumber);
	builder.append(" ");
    }

    //                                                   --- Convert from FEN ---


    private void placePiecesFromFEN(String piecePositions){
	int x = 0;
	int y = 0;
	for (int i = 0; i < piecePositions.length(); i++) {
	    char curr = piecePositions.charAt(i);

	    //TODO replace with piece construction method?
	    switch (curr) {
		case '/':
		    y++;
		    x = 0;
		    break;
		case 'r':
		    setPiece(x, y, new Rook(getBlackPlayer()));
		    x++;
		    break;
		case 'R':
		    setPiece(x, y, new Rook(getWhitePlayer()));
		    x++;
		    break;
		case 'n':
		    setPiece(x, y, new Knight(getBlackPlayer()));
		    x++;
		    break;
		case 'N':
		    setPiece(x, y, new Knight(getWhitePlayer()));
		    x++;
		    break;
		case 'b':
		    setPiece(x, y, new Bishop(getBlackPlayer()));
		    x++;
		    break;
		case 'B':
		    setPiece(x, y, new Bishop(getWhitePlayer()));
		    x++;
		    break;
		case 'k':
		    Piece blackKing = new King(getBlackPlayer());
		    getBlackPlayer().setKing(blackKing);

		    setPiece(x, y, blackKing);
		    x++;
		    break;
		case 'K':
		    Piece whiteKing = new King(getWhitePlayer());
		    getWhitePlayer().setKing(whiteKing);

		    setPiece(x, y, whiteKing);
		    x++;
		    break;
		case 'q':
		    setPiece(x, y, new Queen(getBlackPlayer()));
		    x++;
		    break;
		case 'Q':
		    setPiece(x, y, new Queen(getWhitePlayer()));
		    x++;
		    break;
		case 'p':
		    setPiece(x, y, new Pawn(getBlackPlayer()));
		    x++;
		    break;
		case 'P':
		    setPiece(x, y, new Pawn(getWhitePlayer()));
		    x++;
		    break;
		default:
		    x += Character.getNumericValue(curr);
	    }
	}
    }

    private void setActivePlayerFromFEN(String activePlayer){
	if (activePlayer.equals("b")) {
	    this.activePlayerIndex = 1;
	} else {
	    this.activePlayerIndex = 0;
	}
    }

    private void setCastlingAvailabilityFromFEN(String str){
	//TODO add functionality
    }

    private void setEnPassantTargetFromFEN(String str){
	//TODO add functionality
    }

    private void setMovesFromFEN(String halfmoveClock, String fullmoveNumber){
	// Set halfmove clock
	this.halfmoveClock = Integer.parseInt(halfmoveClock);

	// Set fullmove number
	this.fullmoveNumber = Integer.parseInt(fullmoveNumber);
    }

    private String convertPositionToNotation(final Point p) {
	final String alphas = "abcdefghijklmnopqrstuvwxyz?";
	return alphas.substring(p.x, p.x + 1) + (p.y + 1);
    }

    private void clearBoard() {
	for (int y = 0; y < height; y++) {
	    for (int x = 0; x < width; x++) {
		pieces[y][x] = null;
	    }
	}
    }

    public void updatePossibleMoves() {
        whitePossibleMoves.clear();
        blackPossibleMoves.clear();

	for (int y = 0; y < height; y++) {
	    for (int x = 0; x < width; x++) {
		Piece currPiece = getPiece(x, y);

		if (currPiece == null) {
		    continue;
		} else if (currPiece.getColor() == TeamColor.WHITE) {
		    whitePossibleMoves.addAll(currPiece.getMoves(this, x, y));
		} else {
		    blackPossibleMoves.addAll(currPiece.getMoves(this, x, y));
		}
	    }
	}
    }

    public void passTurn() {
	int nextActivePlayerIndex = (activePlayerIndex + 1) % 2;

	activePlayerIndex = nextActivePlayerIndex;

	if (nextActivePlayerIndex == 0) {
	    fullmoveNumber++;
	}

	updatePossibleMoves();

	//TODO remove debug prints when done
	System.out.println("Turn number: " + getFullmoveNumber() + "	Active player: " + getActivePlayer().getColor());
//	System.out.println("White threatens: " + getWhitePossibleMoves());
//	System.out.println("Black threatens: " + getBlackPossibleMoves());
	System.out.println("White in check: " + isInCheck(getWhitePlayer()));
	System.out.println("Black in check: " + isInCheck(getBlackPlayer()));
    }
}

