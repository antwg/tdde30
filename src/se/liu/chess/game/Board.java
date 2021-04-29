package se.liu.chess.game;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import se.liu.chess.pieces.Piece;
import se.liu.chess.pieces.PieceType;
import se.liu.chess.pieces.Rook;
import se.liu.chess.pieces.Knight;
import se.liu.chess.pieces.Bishop;
import se.liu.chess.pieces.Queen;

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

    private int activePlayerIndex = 0;

    private FenConverter fenConverter;

    private Point enPassantTarget = null;
    private List<Point> threatenedSquares = null;

    private Point currentlyPressed = null;

    //TODO implement halfmoveClock
    private int halfmoveClock = 0;  // Used for 50 move rule
    private int fullmoveNumber = 1; // It's called halfmove and fullmove as one word

    Point[] vectorThreatDirections = { new Point(1, 1),
	    			       new Point(1, -1),
	    			       new Point(-1, 1),
	    			       new Point(-1, -1),
	    			       new Point(1, 0),
	    			       new Point(0, 1),
	    			       new Point(-1, 0),
	    			       new Point(0, -1) };

    Point[] knightThreatDirections = { new Point(1, 2),
	    			       new Point(2, 1),
	    			       new Point(1, -2),
	    			       new Point(2, -1),
	    			       new Point(-1, 2),
	    			       new Point(-2, 1),
	    			       new Point(-1, -2),
	    			       new Point(-2, -1) };

    Point[] pawnThreatDirections = { new Point(-1, -1),	// Pawn threats for white, can be inverted to get pawn threats for black.
	    			     new Point(1, -1) };

    List<Set<Point>> allDirectThreats = new ArrayList<>();
    List<Set<Point>> allPins = new ArrayList<>();

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

    public Player getInactivePlayer() {
	if (activePlayerIndex == 0) {
	    return blackPlayer;
	} else {
	    return whitePlayer;
	}
    }

    public Player getOpponentPlayer(Player friendlyPlayer) {
	if (friendlyPlayer == whitePlayer) {
	    return blackPlayer;
	} else {
	    return whitePlayer;
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

    public Set<Move> getMoves(int x,int y) {
        Set<Move> moves = new HashSet<>();

	for (Move move : getActivePlayer().getAvailableMoves()) {
	    if (move.getOriginSquare().x == x && move.getOriginSquare().y == y) {
	        moves.add(move);
	    }
	}

	return moves;
    }

    /*
    public Set<Move> getPossibleMoves(TeamColor teamColor) {
	if (teamColor == TeamColor.WHITE) {
	    return whitePossibleMoves;
	}
	return blackPossibleMoves;
    }
     */

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

    /**
     * Moves the piece on the origin square of the move to the target square
     * and performs promotion. (not yet implemented)
     * Updates castling availability and en passant square.
     * @param move
     */
    public void performMove(Move move){
        // These cases are mutually exclusive
	if (move.isCastling()) {
	    castle(move);
	} else if (getPiece(move.getOriginSquare()).getType() == PieceType.ROOK &&
		   move.getOriginSquare().y == 7 * activePlayerIndex) { //TODO replace with ex. activePlayerBackrank ?
	    disableCastlingOnMoveSide(move);
	} else if (move.isPawnDoubleStep()) {
	    setEnPassantTargetSquare(move); // TODO rename similarly named functions?
	} else {
	    setEnPassantTarget(null);
	}

	if (move.isHarmless()) {
	    System.out.println("Piece captured on " + move.getTargetSquare());
	}

	movePiece(move.getOriginSquare(), move.getTargetSquare());

	if (move.isPromoting()) {
	    promote(move);
	}

	passTurn();
    }

    private void promote(final Move move) {
	//TODO implement
	String[] options = {"Queen", "Rook", "Bishop", "Knight"};
	int choice = JOptionPane.showOptionDialog(null, "Choose upgrade", "", JOptionPane.DEFAULT_OPTION,
						  JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

	final int queen = 0, rook = 1, bishop = 2, knight = 3;

	switch(choice){
	    case queen:
		setPiece(move.getTargetSquare(), new Queen(getActivePlayer(), move.getTargetSquare()));
		break;
	    case rook:
		setPiece(move.getTargetSquare(), new Rook(getActivePlayer(), move.getTargetSquare()));
		break;
	    case bishop:
		setPiece(move.getTargetSquare(), new Bishop(getActivePlayer(), move.getTargetSquare()));
		break;
	    case knight:
		setPiece(move.getTargetSquare(), new Knight(getActivePlayer(), move.getTargetSquare()));
		break;
	    default:
		System.out.println("Error in testForUpgrade");
	}
    }

    private void setEnPassantTargetSquare(final Move move) {
	if (activePlayerIndex == 0) {
	    setEnPassantTarget(move.getOriginSquare().x, 2);
	} else {
	    setEnPassantTarget(move.getOriginSquare().x, 5);
	}
    }

    private void disableCastlingOnMoveSide(final Move move) {
	if (move.getOriginSquare().x == 0) {
	    getActivePlayer().setQueensideCastleAvailable(false);
	} else if (move.getOriginSquare().x == 7) {
	    getActivePlayer().setKingsideCastleAvailable(false);
	}
    }

    /**
     * Removes castling availability and moves the rooks for castling.
     * @param move
     */
    private void castle(final Move move) {
	getActivePlayer().setKingsideCastleAvailable(false);
	getActivePlayer().setQueensideCastleAvailable(false);
	//TODO split up into castle methods
	if (move.getTargetSquare().x == 2) {
	    if (getActivePlayer().getColor() == TeamColor.WHITE) {
		movePiece(new Point(0, 7), new Point(3, 7));
	    } else {
		movePiece(new Point(0, 0), new Point(3, 0));
	    }
	} else if (move.getTargetSquare().x == 6) {
	    if (getActivePlayer().getColor() == TeamColor.WHITE) {
		movePiece(new Point(7, 7), new Point(5, 7));
	    } else {
		movePiece(new Point(7, 0), new Point(5, 0));
	    }

	} else {
	    System.out.println("CODE SHOULD NOT GET HERE! (method castle() in Board)");
	    // TODO throw exception
	}
    }

    /**
     * Replaces the piece on the target square with the piece on the origin square
     * and sets the origin square to empty.
     * @param originSquare
     * @param targetSquare
     */
    public void movePiece(Point originSquare, Point targetSquare) {
        Piece pieceToMove = getPiece(originSquare);
	setPiece(targetSquare, pieceToMove);
	setPiece(originSquare, null);

	pieceToMove.setHasMoved(true);
	pieceToMove.setPosition(targetSquare);
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

    /*
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

    public Set<Move> getValidMoves(int x, int y){
	Piece selectedPiece = getPiece(x, y);
	Set<Move> moves = new HashSet<>();

	if (selectedPiece != null &&
	    selectedPiece.getColor() == getActivePlayer().getColor()) {
	    moves = selectedPiece.getMoves(this, x, y);
	}
	return moves;
    }

     */

    /**
     * Returns a list of threats.
     * @return
     */
    public List<Set<Point>> getAllDirectThreats() {
	return allDirectThreats;
    }

    public List<Set<Point>> getAllPins() {
	return allPins;
    }


    //TODO improve function, remove king field from player?
    public boolean isInCheck(Player player) {
        /*
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
	*/

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

    /*
    private boolean hasLegalMoves(Player player) {
	return !getPossibleMoves(player.getColor()).isEmpty();
    }

    */

    /*
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

     */

    private void passTurn() { // Pass is used as a verb (inspection)
	// Update active player
	int nextActivePlayerIndex = (activePlayerIndex + 1) % 2;
	activePlayerIndex = nextActivePlayerIndex;

	if (nextActivePlayerIndex == 0) {
	    fullmoveNumber++;
	}

	updateAvailableMoves(getInactivePlayer());

	updateThreats(getActivePlayer());

	updateAvailableMoves(getActivePlayer());

	//TODO remove debug prints when done
	System.out.println("Turn number: " + fullmoveNumber + "	Active player: " + getActivePlayer().getColor());
//	System.out.println("White threatens: " + getWhitePossibleMoves());
//	System.out.println("Black threatens: " + getBlackPossibleMoves());
	System.out.println("White in check: " + isInCheck(getPlayer(TeamColor.WHITE)));
	System.out.println("Black in check: " + isInCheck(getPlayer(TeamColor.BLACK)));
    }

    //TODO split into smaller functions
    private void updateThreats(final Player player) {
        Point kingPos = player.getKing().getPosition();

        allDirectThreats = new ArrayList<>();
        allPins = new ArrayList<>();

        // Check vector threats. These can be blocked by friendly pieces. (queen, bishop, rook)
	for (Point moveVector : vectorThreatDirections) {
	    updateVectorThreats(kingPos, moveVector);
	}

	// Check point threats. These are unblockable. (knight and pawn)
	for (Point movePoint : knightThreatDirections) {
	    updatePointThreats(kingPos, movePoint, PieceType.KNIGHT);
	}

	// Point moves are for white pieces by default. Can be inverted along the
	// y-axis to get threats for black.
	for (Point movePoint : pawnThreatDirections) {
	    int inversionFactor = 1 - 2 * activePlayerIndex;

	    updatePointThreats(kingPos, new Point(movePoint.x, movePoint.y * inversionFactor), PieceType.PAWN);
	}
    }

    private void updatePointThreats(final Point kingPos, final Point movePoint, final PieceType pieceType) {
	int combinedX = kingPos.x + movePoint.x;
	int combinedY = kingPos.y + movePoint.y;

	if (!isValidTile(combinedX, combinedY)) {
	    return;
	}

	Piece piece = getPiece(combinedX, combinedY);

	if (piece != null && piece.getOwner().equals(getInactivePlayer()) &&
	    piece.getType() == pieceType) {
	    Set<Point> threat = new HashSet<>();
	    threat.add(new Point(combinedX, combinedY));
	    allDirectThreats.add(threat);
	}
    }

    private void updateVectorThreats(final Point kingPos, final Point moveVector) {
	Set<Point> threat = new HashSet<>();

	boolean checkingDiagonal = (moveVector.x != 0 && moveVector.y != 0);
	boolean isDirectThreat = true;
	boolean isPin = false;

	int combinedX = kingPos.x + moveVector.x;
	int combinedY = kingPos.y + moveVector.y;

	while (true) {
	    if (!isValidTile(combinedX, combinedY)) {
		break;
	    }

	    Piece piece = getPiece(combinedX, combinedY);

	    if (piece == null) {
		threat.add(new Point(combinedX, combinedY));
	    } else if (piece.getOwner().equals(getActivePlayer())) {
		// Friendly piece encountered

		if (isPin) {
		    isPin = false;
		    break;
		} else {
		    isPin = true;
		    isDirectThreat = false;
		    threat.add(new Point(combinedX, combinedY));
		}

	    } else if (piece.getOwner().equals(getInactivePlayer())) {
		// Hostile piece encountered

		// The queen, bishop and rook captures along a vector.
		if (piece.getType() != PieceType.QUEEN &&
		    !(checkingDiagonal && piece.getType() == PieceType.BISHOP) &&
		    !(!checkingDiagonal && piece.getType() == PieceType.ROOK)) {
		    isDirectThreat = false;
		    isPin = false;
		}
		break;

	    } else {
		System.out.println("Code should not get here! (method updateThreats() in Board)");
	    }
	    combinedX += moveVector.x;
	    combinedY += moveVector.y;
	}

	if (isDirectThreat) {
	    allDirectThreats.add(threat);
	} else if (isPin) {
	    allPins.add(threat);
	}
    }

    public void updateAvailableMoves(final Player player) {
        Set<Move> availableMoves = new HashSet<>();

	for (int y = 0; y < height; y++) {
	    for (int x = 0; x < width; x++) {
	        Piece currentPiece = getPiece(x, y);

		if (currentPiece == null || !currentPiece.getOwner().equals(player)) {
		    continue;
		}

		Set<Move> currentPieceMoves = currentPiece.getMoves(this, x, y);

		availableMoves.addAll(currentPieceMoves);
	    }
	}

	player.setAvailableMoves(availableMoves);
    }

    /*
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
     */

    private void clearBoard() {
	for (int y = 0; y < height; y++) {
	    for (int x = 0; x < width; x++) {
		pieces[y][x] = null;
	    }
	}
    }


    /**
     * Updates whitePossibleMoves and blackPossibleMoves with the currently
     * available moves for both players. Sets threatenedSquares to squares
     * threatened by inactive player.
     */
    /*
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

    */


}

