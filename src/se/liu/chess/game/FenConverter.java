package se.liu.chess.game;

import se.liu.chess.pieces.Bishop;
import se.liu.chess.pieces.King;
import se.liu.chess.pieces.Knight;
import se.liu.chess.pieces.Pawn;
import se.liu.chess.pieces.Piece;
import se.liu.chess.pieces.Queen;
import se.liu.chess.pieces.Rook;

import java.awt.*;

public class FenConverter {
    private Board board;
    private int height;
    private int width;

    public FenConverter(final Board board) {
	this.board = board;
	this.height = board.getHeight();
	this.width = board.getWidth();
    }

    //                                                   --- Convert to FEN ---

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

    private void convertPiecesToFEN(StringBuilder builder) {
	for (int y = 0; y < height; y++) {
	    int emptySquaresInARow = 0; //(Inspection) variable used in both for-loop and if-statement below
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
	if (board.getPlayer(TeamColor.WHITE).canCastleQueenside()) { // It's written as one word
	    builder.append("Q");
	}
	if (board.getPlayer(TeamColor.BLACK).canCastleKingside()) {
	    builder.append("k");
	}
	if (board.getPlayer(TeamColor.BLACK).canCastleQueenside()) {
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
	builder.append(board.getHalfmoveClock());
	builder.append(" ");

	// 6. Fullmove number
	builder.append(board.getFullmoveNumber());
	builder.append(" ");
    }

    //                                                   --- Convert from FEN ---

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

    private void placePiecesFromFEN(String piecePositions){
	int x = 0;
	int y = 0;
	for (int i = 0; i < piecePositions.length(); i++) {
	    char curr = piecePositions.charAt(i);

	    //TODO replace with piece construction method? Also duplicate code
	    switch (curr) { // Useful to keep as string, both because "/" can't be used in enum and the main purpose is to convert from/to string
		case '/':
		    y++;
		    x = 0;
		    break;
		case 'r':
		    board.setPiece(x, y, new Rook(board.getPlayer(TeamColor.BLACK), new Point(x, y)));
		    x++;
		    break;
		case 'R':
		    board.setPiece(x, y, new Rook(board.getPlayer(TeamColor.WHITE), new Point(x, y)));
		    x++;
		    break;
		case 'n':
		    board.setPiece(x, y, new Knight(board.getPlayer(TeamColor.BLACK), new Point(x, y)));
		    x++;
		    break;
		case 'N':
		    board.setPiece(x, y, new Knight(board.getPlayer(TeamColor.WHITE), new Point(x, y)));
		    x++;
		    break;
		case 'b':
		    board.setPiece(x, y, new Bishop(board.getPlayer(TeamColor.BLACK), new Point(x, y)));
		    x++;
		    break;
		case 'B':
		    board.setPiece(x, y, new Bishop(board.getPlayer(TeamColor.WHITE), new Point(x, y)));
		    x++;
		    break;
		case 'k':
		    Piece blackKing = new King(board.getPlayer(TeamColor.BLACK), new Point(x, y));
		    board.getPlayer(TeamColor.BLACK).setKing(blackKing);
		    board.setPiece(x, y, blackKing);
		    x++;
		    break;
		case 'K':
		    Piece whiteKing = new King(board.getPlayer(TeamColor.WHITE), new Point(x, y));
		    board.getPlayer(TeamColor.WHITE).setKing(whiteKing);

		    board.setPiece(x, y, whiteKing);
		    x++;
		    break;
		case 'q':
		    board.setPiece(x, y, new Queen(board.getPlayer(TeamColor.BLACK), new Point(x, y)));
		    x++;
		    break;
		case 'Q':
		    board.setPiece(x, y, new Queen(board.getPlayer(TeamColor.WHITE), new Point(x, y)));
		    x++;
		    break;
		case 'p':
		    board.setPiece(x, y, new Pawn(board.getPlayer(TeamColor.BLACK), new Point(x, y)));
		    x++;
		    break;
		case 'P':
		    board.setPiece(x, y, new Pawn(board.getPlayer(TeamColor.WHITE), new Point(x, y)));
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

    private void setMovesFromFEN(String halfmoveClock, String fullmoveNumber){
	// Set halfmove clock
	board.setHalfmoveClock(Integer.parseInt(halfmoveClock));

	// Set fullmove number
	board.setFullmoveNumber(Integer.parseInt(fullmoveNumber));
    }

    private String convertPositionToNotation(final Point p) {
	final String alphas = "abcdefghijklmnopqrstuvwxyz?";
	return alphas.substring(p.x, p.x + 1) + (p.y + 1);
    }
}