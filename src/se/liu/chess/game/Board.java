package se.liu.chess.game;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

import se.liu.chess.pieces.Piece;
import se.liu.chess.pieces.Rook;
import se.liu.chess.pieces.Knight;
import se.liu.chess.pieces.Bishop;
import se.liu.chess.pieces.King;
import se.liu.chess.pieces.Queen;
import se.liu.chess.pieces.Pawn;
import javax.swing.*;

/**
 * Creates a board which handles all game logic and saves the position of pieces. Has methods for moving pieces.
 */
public class Board
{
    private Piece[][] pieces;
    private final int width;
    private final int height;

    private Player whitePlayer;
    private Player blackPlayer;

    private Set<Point> whitePossibleMoves = new HashSet<>();
    private Set<Point> blackPossibleMoves = new HashSet<>();

    private int activePlayerIndex = 0;

    private Point enPassantTarget = null;

    private Point currentlyPressed = null;

    //TODO implement halfmoveClock
    private int halfmoveClock = 0;  // Used for 50 move rule
    private int fullmoveNumber = 1; // It's called halfmove as one word

    public Board(final int width, final int height) {
	this.width = width;
	this.height = height;
	this.pieces = new Piece[width][height];

	clearBoard();

	this.whitePlayer = new Player(TeamColor.WHITE);
	this.blackPlayer = new Player(TeamColor.BLACK);

    }

    // ---------------------------------------------------- Getters/Setters ----------------------------------------------------------------

    // --- Getters ---

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

    public Player getActivePlayer() {
	if (activePlayerIndex == 0) {
	    return whitePlayer;
	} else {
	    return blackPlayer;
	}
    }

    public Point getEnPassantTarget() {
	return enPassantTarget;
    }

    public Player getPlayer(TeamColor color) {
        if (color == TeamColor.WHITE){
	    return whitePlayer;
	}
        else{
            return blackPlayer;
	}
    }

    public Set<Point> getPossibleMoves(TeamColor teamColor) {
	if (teamColor == TeamColor.WHITE) {
	    return whitePossibleMoves;
	}
	return blackPossibleMoves;
    }

    public Point getCurrentlyPressed() {
	return currentlyPressed;
    }

    public boolean isEmpty(int x, int y) {
	return getPiece(x, y) == null;
    }

    // --- Setters ---

    public void setPiece(int x, int y, Piece piece) {
	pieces[y][x] = piece;
    }

    public void setPiece(Point p, Piece piece) {
	setPiece(p.x, p.y, piece);
    }

    public void setEnPassantTarget(final Point enPassantTarget) {
	this.enPassantTarget = enPassantTarget;
    }

    public void setEnPassantTarget(int x, int y) {
	this.enPassantTarget = new Point(x, y);
    }


    // ----------------------------------------------------- Public Methods ----------------------------------------------------------------

    public void movePiece(Point p1, Point p2) {
        Piece pieceToMove = getPiece(p1);
	setPiece(p2, pieceToMove);
	setPiece(p1, null);
	if (!getActivePlayer().canCastleQueenside() && !getActivePlayer().canCastleKingside()) {
	    return;
	}

//	if (pieceToMove.getType() == PieceType.KING) {
//	    getActivePlayer().setKingsideCastleAvailable(false);
//	    getActivePlayer().setQueensideCastleAvailable(false);
//	} else if (pieceToMove.getType() == PieceType.ROOK) {
//	    if () {
//	    }
//	}
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

    public void pressedSquare(Point point){
	Point lastPressed = currentlyPressed;
	this.currentlyPressed = point;

	// Stop from moving empty pieces (null)
	if(lastPressed != null && getPiece(lastPressed) != null){

	    // Get legal moves
	    if (getValidMoves(lastPressed.x, lastPressed.y).contains(currentlyPressed)) {
		movePiece(lastPressed, currentlyPressed);
		getActivePlayer().increaseTimeByIncrement();
		getPiece(currentlyPressed).setHasMoved(true);
		testForUpgrade();
		passTurn();
	    }
	    tryToKillEnPassant();
	    setEnPassant(lastPressed);

	    this.currentlyPressed = null;
	}
    }

    public Set<Point> getValidMoves(int x, int y){
	Piece selectedPiece = getPiece(x, y);
	Set<Point> moves = new HashSet<>();

	if (selectedPiece != null &&
	    selectedPiece.getColor() == getActivePlayer().getColor()) {
	    moves = selectedPiece.getMoves(this, x, y);
	}
	return moves;
    }


    //TODO improve function, remove king field from player?
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

    public void resetBoard() {
	clearBoard();
	createBoardFromFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
	blackPlayer.resetTime();
	whitePlayer.resetTime();
    }


    // ----------------------------------------------------- Private Methods ---------------------------------------------------------------

    private boolean hasLegalMoves(Player player) {
	return true;
    }

    private void testForUpgrade(){
	if (getPiece(currentlyPressed) != null && getPiece(currentlyPressed) instanceof Pawn){ // Seems excessive to use polymorphism here
	    final int topRow = 0;								       // since we're only interested in Pawn.
	    final int bottomRow = 7;								       // Creating a method for all Pieces to check
	    if(currentlyPressed.y == topRow || currentlyPressed.y == bottomRow){	       // if it's a pawn seems inefficient
		String[] options = {"Queen", "Rook", "Bishop", "Knight"};
		int choice = JOptionPane.showOptionDialog(null, "Choose upgrade", "", JOptionPane.DEFAULT_OPTION,
							  JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
		final int queen = 0, rook = 1, bishop = 2, knight = 3;
		switch(choice){
		    case queen:
			setPiece(currentlyPressed, new Queen(getActivePlayer()));
			break;
		    case rook:
			setPiece(currentlyPressed, new Rook(getActivePlayer()));
			break;
		    case bishop:
			setPiece(currentlyPressed, new Bishop(getActivePlayer()));
			break;
		    case knight:
			setPiece(currentlyPressed, new Knight(getActivePlayer()));
			break;
		    default:
			System.out.println("Error in testForUpgrade");
		}
	    }
	}
    }

    private void setEnPassant(Point lastPressed) {
        final int doubleMove = 2;
	if (getPiece(currentlyPressed) != null && getPiece(currentlyPressed) instanceof Pawn // Seems excessive to use polymorphism here
	    && Math.abs(currentlyPressed.y - lastPressed.y) == doubleMove){		     // since we're only interested in pawn
	    setEnPassantTarget(lastPressed.x, (currentlyPressed.y + lastPressed.y) / 2);  // Creating a method for all Pieces to check
	}										     // if it's a pawn seems inefficient
	else {
	    setEnPassantTarget(null);
	}
    }

    private void tryToKillEnPassant() {
	if (enPassantTarget != null && getPiece(enPassantTarget) != null){
	    Point ep = enPassantTarget;
	    int previousY = ep.y + 1;
	    if (getPiece(ep).getColor() == TeamColor.BLACK) {
		previousY = ep.y - 1;
	    }
	    setPiece(ep.x, previousY, null);
	}
    }

    private void passTurn() { // Pass is used as a verb (inspection)
	int nextActivePlayerIndex = (activePlayerIndex + 1) % 2;

	activePlayerIndex = nextActivePlayerIndex;

	if (nextActivePlayerIndex == 0) {
	    fullmoveNumber++;
	}

	updatePossibleMoves();

	//TODO remove debug prints when done
	System.out.println("Turn number: " + fullmoveNumber + "	Active player: " + getActivePlayer().getColor());
//	System.out.println("White threatens: " + getWhitePossibleMoves());
//	System.out.println("Black threatens: " + getBlackPossibleMoves());
	System.out.println("White in check: " + isInCheck(getPlayer(TeamColor.WHITE)));
	System.out.println("Black in check: " + isInCheck(getPlayer(TeamColor.BLACK)));
    }

    private boolean isGameOver(Player player) {
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
	    builder.append("/"); // (Inspection) thinks it's used for file path
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
	if (whitePlayer.canCastleKingside()) { // (Inspection) Not mutually exclusive
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

	    //TODO replace with piece construction method?
	    switch (curr) { // Useful to keep as string, both because "/" can't be used in enum and the main purpose is to convert from/to string
		case '/':
		    y++;
		    x = 0;
		    break;
		case 'r':
		    setPiece(x, y, new Rook(blackPlayer));
		    x++;
		    break;
		case 'R':
		    setPiece(x, y, new Rook(whitePlayer));
		    x++;
		    break;
		case 'n':
		    setPiece(x, y, new Knight(blackPlayer));
		    x++;
		    break;
		case 'N':
		    setPiece(x, y, new Knight(whitePlayer));
		    x++;
		    break;
		case 'b':
		    setPiece(x, y, new Bishop(blackPlayer));
		    x++;
		    break;
		case 'B':
		    setPiece(x, y, new Bishop(whitePlayer));
		    x++;
		    break;
		case 'k':
		    Piece blackKing = new King(blackPlayer);
		    getPlayer(TeamColor.BLACK).setKing(blackKing);
		    setPiece(x, y, blackKing);
		    x++;
		    break;
		case 'K':
		    Piece whiteKing = new King(whitePlayer);
		    getPlayer(TeamColor.WHITE).setKing(whiteKing);

		    setPiece(x, y, whiteKing);
		    x++;
		    break;
		case 'q':
		    setPiece(x, y, new Queen(blackPlayer));
		    x++;
		    break;
		case 'Q':
		    setPiece(x, y, new Queen(whitePlayer));
		    x++;
		    break;
		case 'p':
		    setPiece(x, y, new Pawn(blackPlayer));
		    x++;
		    break;
		case 'P':
		    setPiece(x, y, new Pawn(whitePlayer));
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

		if (currPiece != null) {
		    if (currPiece.getColor() == TeamColor.WHITE) {
			whitePossibleMoves.addAll(currPiece.getMoves(this, x, y));
		    } else {
			blackPossibleMoves.addAll(currPiece.getMoves(this, x, y));
		    }
		}
	    }
	}
    }
}

