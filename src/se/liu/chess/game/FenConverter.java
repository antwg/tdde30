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
 * Object that converts a given Board object to and from FEN
 */
public class FenConverter {
    private Board board;
    private int height;
    private int width;

    public FenConverter(final Board board) {
	this.board = board;
	this.height = board.getHeight();
	this.width = board.getWidth();
    }

    // ----------------------------------------------------- Public Methods ----------------------------------------------------------------

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

    //                                                   --- Convert to FEN ---


    private void convertPiecesToFEN(StringBuilder builder) {
	int emptySquaresInARow = 0; //(Inspection) variable used in both for-loop and if-statement below
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
	    builder.append("/"); // (Inspection) thinks it's used for file path
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
	if (board.getPlayer(TeamColor.WHITE).canCastleKingside()) { // (Inspection) Not mutually exclusive
	    builder.append("K");
	}
	if (board.getPlayer(TeamColor.WHITE).canCastleQueenSide()) { // It's written as one word
	    builder.append("Q");
	}
	if (board.getPlayer(TeamColor.BLACK).canCastleKingside()) {
	    builder.append("k");
	}
	if (board.getPlayer(TeamColor.BLACK).canCastleQueenSide()) {
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
	// 5. Halfmove clock
	builder.append(board.getHalfMoveClock());
	builder.append(" ");

	// 6. Fullmove number
	builder.append(board.getFullMoveNumber());
	builder.append(" ");
    }

    //                                                   --- Convert from FEN ---

    private void placePiecesFromFEN(String piecePositions){
	int x = 0;
	int y = 0;
	Player black = board.getPlayer(TeamColor.BLACK);
	Player white = board.getPlayer(TeamColor.WHITE);

	for (int i = 0; i < piecePositions.length(); i++) {
	    char curr = piecePositions.charAt(i);

	    //TODO replace with piece construction method? Also duplicate code
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

    private void setCastlingAvailabilityFromFEN(String str){
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
