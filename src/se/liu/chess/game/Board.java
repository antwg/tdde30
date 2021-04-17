package se.liu.chess.game;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

import se.liu.chess.pieces.Piece;
import se.liu.chess.pieces.Rook;
import se.liu.chess.pieces.Knight;
import se.liu.chess.pieces.Bishop;
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

    private FenConverter fenConverter;

    private Point enPassantTarget = null;

    private Point currentlyPressed = null;

    //TODO implement halfmoveClock
    private int halfmoveClock = 0;  // Used for 50 move rule
    private int fullmoveNumber = 1; // It's called halfmove as one word

    public Board(final int width, final int height) {
	this.width = width;
	this.height = height;
	this.pieces = new Piece[width][height];
	this.fenConverter = new FenConverter(this);

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

    public int getHalfmoveClock() {
	return halfmoveClock;
    }

    public int getFullmoveNumber() {
	return fullmoveNumber;
    }

    public FenConverter getFenConverter() {
	return fenConverter;
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

    public void setActivePlayerIndex(final int activePlayerIndex) {
	this.activePlayerIndex = activePlayerIndex;
    }

    public void setHalfmoveClock(final int halfmoveClock) {
	this.halfmoveClock = halfmoveClock;
    }

    public void setFullmoveNumber(final int fullmoveNumber) {
	this.fullmoveNumber = fullmoveNumber;
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
	fenConverter.createBoardFromFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
	blackPlayer.resetTime();
	whitePlayer.resetTime();
    }


    // ----------------------------------------------------- Private Methods ---------------------------------------------------------------

    private boolean hasLegalMoves(Player player) {
	return true;
    }

    private void testForUpgrade(){
	if (getPiece(currentlyPressed) != null && getPiece(currentlyPressed) instanceof Pawn){ // Seems excessive to use polymorphism here
	    final int topRow = 0, bottomRow = 7;					       // since we're only interested in Pawn.
	    										       // Creating a method for all Pieces to check
	    if (currentlyPressed.y == topRow || currentlyPressed.y == bottomRow) {	       // if it's a pawn seems inefficient
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
	if (getPiece(currentlyPressed) != null &&
	    getPiece(currentlyPressed) instanceof Pawn &&			             // Seems excessive to use polymorphism here
	    Math.abs(currentlyPressed.y - lastPressed.y) == doubleMove){		     // since we're only interested in pawn
	    setEnPassantTarget(lastPressed.x, (currentlyPressed.y + lastPressed.y) / 2);  // Creating a method for all Pieces to check
	}										     // if it's a pawn seems inefficient
	else {
	    setEnPassantTarget(null);
	}
    }

    private void tryToKillEnPassant() {
	if (enPassantTarget != null && getPiece(enPassantTarget) != null){
	    int previousY = enPassantTarget.y + 1;

	    if (getPiece(enPassantTarget).getColor() == TeamColor.BLACK) {
		previousY = enPassantTarget.y - 1;
	    }
	    setPiece(enPassantTarget.x, previousY, null);
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

    private void clearBoard() {
	for (int y = 0; y < height; y++) {
	    for (int x = 0; x < width; x++) {
		pieces[y][x] = null;
	    }
	}
    }

    private void updatePossibleMoves() {
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

