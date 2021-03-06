package se.liu.chess.game;

import se.liu.chess.pieces.Bishop;
import se.liu.chess.pieces.King;
import se.liu.chess.pieces.Knight;
import se.liu.chess.pieces.Pawn;
import se.liu.chess.pieces.Piece;
import se.liu.chess.pieces.Queen;
import se.liu.chess.pieces.Rook;

import java.awt.*;

/**
 * Object that converts a given Board object to and from FEN.
 */
public class FenConverter {
    private Board board;
    private int height, width;

    public FenConverter(final Board board) {
	this.board = board;
	this.height = board.getHeight();
	this.width = board.getWidth();
    }

    // ----------------------------------------------------- Public Methods ----------------------------------------------------------------

    /**
     * Returns a string of the board state in FEN notation.
     * For more about FEN, see this article: https://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation
     *
     * @return A string of the board state in FEN notation.
     */
    public String convertBoardToFEN() {  // (Inspection) metoden används inte än, men den skulle användas till milestone 24: "Kan spara ett spel", som inte är fullt implementerad än.
	final StringBuilder builder = new StringBuilder();

	convertPiecesToFEN(builder);
	convertActivePlayerToFEN(builder);
	convertCastlingAbilityToFEN(builder);
	convertEnPassantAvailabilityToFEN(builder);
	convertMoveToFEN(builder);

	return builder.toString();
    }


    /**
     * Changes the board of this FenConverter to match the state in the given string.
     *
     * @param fen The board state in FEN notation.
     */
    public void createBoardFromFEN(final String fen) {
	// Split fen string
	String[] arrOfString = fen.split(" ");
	final int piecePart = 0, playerPart = 1, castlingPart = 2, enPassantPart = 3, halfMovePart = 4, fullMovePart = 5;

	placePiecesFromFEN(arrOfString[piecePart]);
	setActivePlayerFromFEN(arrOfString[playerPart]);
	setCastlingAvailabilityFromFEN(arrOfString[castlingPart]);
	setEnPassantTargetFromFEN(arrOfString[enPassantPart]);
	setMovesFromFEN(arrOfString[halfMovePart], arrOfString[fullMovePart]);
    }

    // ----------------------------------------------------- Private Methods ---------------------------------------------------------------

    private void convertPiecesToFEN(StringBuilder builder) {
	int emptySquaresInARow = 0;
	for (int y = 0; y < height; y++) {
	    for (int x = 0; x < width; x++) {
		if (board.isEmpty(x, y)) {
		    emptySquaresInARow++;
		} else {
		    if (emptySquaresInARow != 0) {
			builder.append(emptySquaresInARow);
			emptySquaresInARow = 0;
		    }
		    builder.append(board.getPiece(x, y));
		}
	    }
	    if (emptySquaresInARow != 0) {
		builder.append(emptySquaresInARow);
		emptySquaresInARow = 0;
	    }
	    builder.append("/"); // --- (Inspection) thinks it's used for file path
	}
	builder.append(" ");
    }

    private void convertActivePlayerToFEN(StringBuilder builder) {
	if(board.getActivePlayer().getColor() ==TeamColor.WHITE) {
	    builder.append("w");
	}
	else {
	    builder.append("b");
	}
	builder.append(" ");
    }

    private void convertCastlingAbilityToFEN(StringBuilder builder){
	if (board.getPlayer(TeamColor.WHITE).isKingSideCastleAvailable()) { // --- (Inspection) Not mutually exclusive
	    builder.append("K");
	}
	if (board.getPlayer(TeamColor.WHITE).isQueenSideCastleAvailable()) {
	    builder.append("Q");
	}
	if (board.getPlayer(TeamColor.BLACK).isKingSideCastleAvailable()) {
	    builder.append("k");
	}
	if (board.getPlayer(TeamColor.BLACK).isQueenSideCastleAvailable()) {
	    builder.append("q");
	}
	builder.append(" ");
    }

    private void convertEnPassantAvailabilityToFEN(StringBuilder builder){
	if (board.getEnPassantTarget() == null) {
	    builder.append("-");
	} else {
	    builder.append(convertPositionToNotation(board.getEnPassantTarget()));
	}
	builder.append(" ");
    }

    private void convertMoveToFEN(StringBuilder builder){
	builder.append(board.getHalfMoveClock());
	builder.append(" ");

	builder.append(board.getFullMoveNumber());
	builder.append(" ");
    }

    private void placePiecesFromFEN(String piecePositions){
	int x = 0;
	int y = 0;
	Player black = board.getPlayer(TeamColor.BLACK);
	Player white = board.getPlayer(TeamColor.WHITE);

	for (int i = 0; i < piecePositions.length(); i++) {
	    char curr = piecePositions.charAt(i);

	    switch (curr) { // Useful to keep as string, both because "/" can't be used in enum and the main purpose is to convert from/to string
		case '/':
		    y++;
		    x = 0;
		    break;
		case 'r':
		    board.setPiece(x, y, new Rook(black, new Point(x, y)));
		    x++;
		    break;
		case 'R':
		    board.setPiece(x, y, new Rook(white, new Point(x, y)));
		    x++;
		    break;
		case 'n':
		    board.setPiece(x, y, new Knight(black, new Point(x, y)));
		    x++;
		    break;
		case 'N':
		    board.setPiece(x, y, new Knight(white, new Point(x, y)));
		    x++;
		    break;
		case 'b':
		    board.setPiece(x, y, new Bishop(black, new Point(x, y)));
		    x++;
		    break;
		case 'B':
		    board.setPiece(x, y, new Bishop(white, new Point(x, y)));
		    x++;
		    break;
		case 'k':
		    Piece blackKing = new King(black, new Point(x, y));
		    black.setKing(blackKing);
		    board.setPiece(x, y, blackKing);
		    x++;
		    break;
		case 'K':
		    Piece whiteKing = new King(white, new Point(x, y));
		    white.setKing(whiteKing);
		    board.setPiece(x, y, whiteKing);
		    x++;
		    break;
		case 'q':
		    board.setPiece(x, y, new Queen(black, new Point(x, y)));
		    x++;
		    break;
		case 'Q':
		    board.setPiece(x, y, new Queen(white, new Point(x, y)));
		    x++;
		    break;
		case 'p':
		    board.setPiece(x, y, new Pawn(black, new Point(x, y)));
		    x++;
		    break;
		case 'P':
		    board.setPiece(x, y, new Pawn(white, new Point(x, y)));
		    x++;
		    break;
		default:
		    x += Character.getNumericValue(curr);
	    }
	}
    }

    private void setActivePlayerFromFEN(String activePlayer){
	if (activePlayer.equals("b")) {
	    board.setActivePlayerIndex(1);
	} else {
	    board.setActivePlayerIndex(0);
	}
    }

    private void setCastlingAvailabilityFromFEN(String str){  // Dessa metoder hör till sparande/laddande av spel, vilket inte är färdigt än
	//TODO add functionality
    }

    private void setEnPassantTargetFromFEN(String str){
	//TODO add functionality
    }

    private void setMovesFromFEN(String halfMoveClock, String fullMoveNumber){
	// Set halfmove clock
	board.setHalfMoveClock(Integer.parseInt(halfMoveClock));

	// Set fullmove number
	board.setFullMoveNumber(Integer.parseInt(fullMoveNumber));
    }

    private String convertPositionToNotation(final Point p) {
	final String alphas = "abcdefghijklmnopqrstuvwxyz?";
	return alphas.substring(p.x, p.x + 1) + (p.y + 1);
    }
}
