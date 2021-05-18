package se.liu.chess.game;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import se.liu.chess.gui.ChessComponent;
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

    private GameManager gameManager;

    private Player whitePlayer;
    private Player blackPlayer;

    private int activePlayerIndex = 0;
    private boolean gameOver;

    private final FenConverter fenConverter;

    private Point enPassantTarget = null;
    private List<Point> threatenedSquares = null;
    private ChessComponent chessComponent;

    private int halfMoveClock = 0; //TODO implement
    private int fullMoveNumber = 1;

    private final Point[] vectorThreatDirections = { new Point(1, 1),
	    					     new Point(1, -1),
						     new Point(-1, 1),
						     new Point(-1, -1),
						     new Point(1, 0),
						     new Point(0, 1),
						     new Point(-1, 0),
						     new Point(0, -1) };

    private final Point[] knightThreatDirections = { new Point(1, 2),
						     new Point( 2, 1),
						     new Point(1, -2),
						     new Point(2, -1),
						     new Point(-1, 2),
						     new Point(-2, 1),
						     new Point(-1, -2),
						     new Point(-2, -1) };

    private final Point[] pawnThreatDirections = { new Point(-1, -1),	// Pawn threats for white, can be inverted to get pawn threats for black.
						   new Point(1, -1) };

    private List<Set<Point>> allDirectThreats = new ArrayList<>();
    private List<Set<Point>> allPins = new ArrayList<>();


    public Board(final int width, final int height, GameManager gameManager) {
		this.gameManager = gameManager;
        	this.width = width;
		this.height = height;
		this.pieces = new Piece[width][height];
		this.fenConverter = new FenConverter(this);
		this.chessComponent = new ChessComponent(this);
		this.whitePlayer = new Player(TeamColor.WHITE);
		this.blackPlayer = new Player(TeamColor.BLACK);

		clearBoard();
    }

    // ---------------------------------------------------- Getters/Setters ----------------------------------------------------------------

    public int getWidth() {
	return width;
    }

    public int getHeight() {
	return height;
    }

    public Piece getPiece(final int x, final int y) {
	return pieces[y][x];
    }

    public Piece getPiece(final Point p) {
	return getPiece(p.x, p.y);
    }

    public void setPiece(int x, int y, Piece piece) {
	pieces[y][x] = piece;
    }

    public void setPiece(Point p, Piece piece) {
	setPiece(p.x, p.y, piece);
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

    public Player getOpponentPlayer(final Player friendlyPlayer) {
	if (friendlyPlayer.equals(whitePlayer)) {
	    return blackPlayer;
	} else {
	    return whitePlayer;
	}
    }

    //TODO behöver den här vara public?
    public void setActivePlayerIndex(final int activePlayerIndex) {
    	this.activePlayerIndex = activePlayerIndex;
    }

    public Point getEnPassantTarget() {
	return enPassantTarget;
    }

    public void setEnPassantTarget(final Point enPassantTarget) {
	this.enPassantTarget = enPassantTarget;
    }

    public void setEnPassantTarget(int x, int y) {
	this.enPassantTarget = new Point(x, y);
    }

    public void setEnPassantTarget(Move move){
	if  (move == null) {
	    this.enPassantTarget = null;
	}
	else {
	    int enPassantRow = 2;
	    if (activePlayerIndex == 0) {
		enPassantRow = 5;
	    }
	    this.enPassantTarget = new Point(move.getOriginSquare().x, enPassantRow);
	}
    }

    public Player getPlayer(final TeamColor color) {
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

    public ChessComponent getChessComponent() {
	return chessComponent;
    }

    public boolean isEmpty(int x, int y) {
	return getPiece(x, y) == null;
    }

    public int getHalfMoveClock() {
	return halfMoveClock;
    }

    public void setHalfMoveClock(final int halfMoveClock) {
	this.halfMoveClock = halfMoveClock;
    }

    public int getFullMoveNumber() {
	return fullMoveNumber;
    }

    public void setFullMoveNumber(final int fullMoveNumber) {
	this.fullMoveNumber = fullMoveNumber;
    }

    public FenConverter getFenConverter() {
	return fenConverter;
    }

    public boolean isGameOver() {
	return gameOver;
    }

    public void setGameOver(boolean gameOver) {
    	this.gameOver = gameOver;
    }

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



    // ----------------------------------------------------- Public Methods ----------------------------------------------------------------

    /**
     * Moves the piece on the origin square of the move to the target square
     * and performs promotion. (not yet implemented)
     * Updates castling availability and en passant square.
     * @param move
     */
    public void performMove(Move move) {
        Player activePlayer = getActivePlayer();

	if (move.isCastling()) {
	    performCastling(move);
	}
	// Disable castling availability on side where rook moved
	else if (getPiece(move.getOriginSquare()).getType() == PieceType.ROOK) {
	    if (move.getOriginSquare().equals(activePlayer.getKingSideRookHomePosition())) {
		activePlayer.setKingSideCastleAvailable(false);
	    }
	    else if(move.getOriginSquare().equals(activePlayer.getQueenSideRookHomePosition())){
	        activePlayer.setQueenSideCastleAvailable(false);
	    }
	}
	// If king moved, disable castling on both sides
	else if (move.getMovingPiece().getType() == PieceType.KING) {
	    activePlayer.setQueenSideCastleAvailable(false);
	    activePlayer.setKingSideCastleAvailable(false);
	}

	Point targetSquare = move.getTargetSquare();
	Player inactivePlayer = getInactivePlayer();

	// Disable castling if rook is captured
	if (targetSquare.equals(inactivePlayer.getKingSideRookHomePosition())) {
	    inactivePlayer.setKingSideCastleAvailable(false);
	} else if (targetSquare.equals(inactivePlayer.getQueenSideRookHomePosition())) {
	    inactivePlayer.setQueenSideCastleAvailable(false);
	}

	// Capture en passant target
	if (move.getMovingPiece().getType().equals(PieceType.PAWN) && targetSquare.equals(enPassantTarget)) {
	    int captureX = targetSquare.x;
	    int captureY = targetSquare.y + inactivePlayer.getForwardDirection();

	    setPiece(captureX, captureY, null);
	}

	// Doublestep
	setEnPassantTarget(move);

	movePiece(move.getOriginSquare(), targetSquare);

	if (move.isPromoting()) {
	    promote(move);
	}
	passTurn();
    }

    /**
     * Shows an OptionDialog saying who won and why.
     * Also allows to restart or quit.
     *
     * @param cause The GameOverCause that caused the game to end.
     */
    public void displayGameOver(GameOverCauses cause){
	chessComponent.repaint();
	String message = getGameOverMessage(cause);
	String[] options = {"Restart", "Quit"};
        final int restart = 0, quit = 1;
	int choice = JOptionPane.showOptionDialog(null, message, "", JOptionPane.DEFAULT_OPTION,
						  JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
	switch (choice){
	    case restart:
	        resetBoard();
	        break;
	    case quit:
	        System.exit(0);
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

    /**
     * Returns whether a player is in check or not.
     *
     * @param player The player to check
     * @return true if in check, else false
     */
    public boolean isInCheck(Player player) {
	return !allDirectThreats.isEmpty() && player.equals(getActivePlayer());
    }

    /**
     * Resets the board.
     */
    public void resetBoard() {
	clearBoard();
	blackPlayer = new Player(TeamColor.BLACK);
	whitePlayer = new Player(TeamColor.WHITE);
	fenConverter.createBoardFromFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
	blackPlayer.resetTime();
	whitePlayer.resetTime();
	updateThreats(getActivePlayer());
	updateAvailableMoves(getActivePlayer());
	gameOver = false;

    }

    /**
     * Returns true if targetSquare is protected, else false
     *
     * @param targetSquare square with piece on
     * @return true if protected, else false
     */
    public boolean isPieceProtected(final Point targetSquare) {
	if (isDiagonallyProtected(targetSquare)) return true;

	if (isOrthogonallyProtected(targetSquare)) return true;

	if (isProtectedFromKnights(targetSquare)) return true;

	if (isProtectedFromPawns(targetSquare)) return true;

	return false;
    }


    // ----------------------------------------------------- Private Methods ---------------------------------------------------------------

    private void promote(final Move move) {
	String[] options = {"Queen", "Rook", "Bishop", "Knight"};
	int choice = JOptionPane.showOptionDialog(null, "Choose upgrade", "", JOptionPane.DEFAULT_OPTION,
						  JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

	final int queen = 0, rook = 1, bishop = 2, knight = 3;
	Point targetSquare = move.getTargetSquare();

	switch(choice){
	    case queen:
		setPiece(targetSquare, new Queen(getActivePlayer(), targetSquare));
		break;
	    case rook:
		setPiece(targetSquare, new Rook(getActivePlayer(), targetSquare));
		break;
	    case bishop:
		setPiece(targetSquare, new Bishop(getActivePlayer(), targetSquare));
		break;
	    case knight:
		setPiece(targetSquare, new Knight(getActivePlayer(), targetSquare));
		break;
	    default:
		System.out.println("Error in testForUpgrade");
	}
    }

    /**
     * Removes castling availability and moves the rooks for castling.
     * @param move
     */
    private void performCastling(final Move move) {
	getActivePlayer().setKingSideCastleAvailable(false);
	getActivePlayer().setQueenSideCastleAvailable(false);
	TeamColor activeColor = getActivePlayer().getColor();
	//TODO split up into castle methods
	if (move.getTargetSquare().x == 2) {
	    if (activeColor == TeamColor.WHITE) {
		movePiece(new Point(0, 7), new Point(3, 7));
	    } else {
		movePiece(new Point(0, 0), new Point(3, 0));
	    }
	} else if (move.getTargetSquare().x == 6) {
	    if (activeColor == TeamColor.WHITE) {
		movePiece(new Point(7, 7), new Point(5, 7));
	    } else {
		movePiece(new Point(7, 0), new Point(5, 0));
	    }

	} else {
	    System.out.println("CODE SHOULD NOT GET HERE! (method castle() in Board)");
	    // TODO throw exception?
	}
    }

    private boolean isProtectedFromPawns(final Point targetSquare) {
	final Point[] pawnAttacks = { new Point(-1, -getPiece(targetSquare).getOwner().getForwardDirection()),
				      new Point(1, -getPiece(targetSquare).getOwner().getForwardDirection()) };

	return isProtectedFromPoints(targetSquare, pawnAttacks, PieceType.PAWN);
    }

    private boolean isProtectedFromKnights(final Point targetSquare) {
	final Point[] knightAttacks = { new Point(1, 2),
					new Point( 2, 1),
					new Point(1, -2),
					new Point(2, -1),
					new Point(-1, 2),
					new Point(-2, 1),
					new Point(-1, -2),
					new Point(-2, -1) };

	return isProtectedFromPoints(targetSquare, knightAttacks, PieceType.KNIGHT);
    }

    private boolean isOrthogonallyProtected(final Point targetSquare) {
	final Point[] orthogonalVectors = { new Point(1, 0),
					    new Point(0, 1),
					    new Point(-1, 0),
					    new Point(0, -1) };

	return isProtectedFromVectors(targetSquare, orthogonalVectors, PieceType.ROOK);
    }

    private boolean isDiagonallyProtected(final Point targetSquare) {
	final Point[] diagonalVectors = { new Point(1, 1),
					  new Point(1, -1),
					  new Point(-1, 1),
					  new Point(-1, -1)};

	return isProtectedFromVectors(targetSquare, diagonalVectors, PieceType.BISHOP);
    }

    private boolean isProtectedFromPoints(final Point targetSquare, final Point[] attacks, final PieceType pieceType) {
	for (Point attack : attacks) {
	    int combinedX = targetSquare.x + attack.x;
	    int combinedY = targetSquare.y + attack.y;

	    if (!isValidTile(combinedX, combinedY)) {
		continue;
	    }

	    Piece pieceOnSquare = getPiece(combinedX, combinedY);
	    TeamColor protectionColor = getPiece(targetSquare).getColor();

	    if (pieceOnSquare != null &&
		pieceOnSquare.getColor().equals(protectionColor) &&
		pieceOnSquare.getType().equals(pieceType)) {
		return true;
	    }
	}
	return false;
    }

    private boolean isProtectedFromVectors(final Point targetSquare, final Point[] vectors, final PieceType pieceType) {
	for (Point vector : vectors) {
	    int combinedX = targetSquare.x + vector.x;
	    int combinedY = targetSquare.y + vector.y;

	    while (true) {
		if (!isValidTile(combinedX, combinedY)) {
		    break;
		}

		Piece pieceOnSquare = getPiece(combinedX, combinedY);
		TeamColor protectionColor = getPiece(targetSquare).getColor();

		if (pieceOnSquare == null) {
		    combinedX += vector.x;
		    combinedY += vector.y;
		} else if (pieceOnSquare.getColor().equals(protectionColor) &&
			   (pieceOnSquare.getType().equals(pieceType) || pieceOnSquare.getType().equals(PieceType.QUEEN))) {
		    return true;
		} else {
		    break;
		}
	    }
	}
	return false;
    }

    private String getGameOverMessage(final GameOverCauses cause) {
	StringBuilder message = new StringBuilder();
	message.append("Game Over: ");
	String activePlayer = getActivePlayer().toString();

	switch (cause){
	    case TIME:
		message.append(activePlayer).append(" ran out of time");
		break;
	    case CHECKMATE:
		message.append(activePlayer).append(" is Checkmated");
		break;
	    case STALEMATE:
		message.append(" Stalemate");
		break;
	}
	return message.toString();
    }

    private void passTurn() { // Pass is used as a verb (inspection)
	// Update active player
	int nextActivePlayerIndex = (activePlayerIndex + 1) % 2;
	activePlayerIndex = nextActivePlayerIndex;

	if (nextActivePlayerIndex == 0) {
	    fullMoveNumber++;
	}

	updateAvailableMoves(getInactivePlayer());

	updateThreats(getActivePlayer());

	updateAvailableMoves(getActivePlayer());

	detectGameOver();

	//TODO remove debug prints when done
	if (!gameOver) {
	    System.out.println("New turn! Turn number: " + fullMoveNumber + "	Active player: " + getActivePlayer().getColor());
	    //System.out.println("White threatens: " + getWhitePossibleMoves());
	    //System.out.println("Black threatens: " + getBlackPossibleMoves());
	    System.out.println("White in check: " + isInCheck(getPlayer(TeamColor.WHITE)));
	    System.out.println("Black in check: " + isInCheck(getPlayer(TeamColor.BLACK)));
	    System.out.println("Direct threats: " + allDirectThreats);
	    System.out.println("Pins: " + allPins);
	}

    }

    private void updateThreats(final Player player) {
        Point kingPos = player.getKing().getPosition();

        allDirectThreats = new ArrayList<>();
        allPins = new ArrayList<>();

        // Check vector threats. These can be blocked by friendly pieces. (queen, bishop, rook)
	for (Point moveVector : vectorThreatDirections) {
	    updateThreatsAlongVector(kingPos, moveVector);
	}

	// Check point threats. These are unblockable. (knight and pawn)
	for (Point movePoint : knightThreatDirections) {
	    updateThreatsOnPoint(kingPos, movePoint, PieceType.KNIGHT);
	}

	// Point moves are for white pieces by default. Can be inverted along the
	// y-axis to get threats for black.
	//TODO go over this
	for (Point movePoint : pawnThreatDirections) {
	    int inversionFactor = 1 - 2 * activePlayerIndex;

	    updateThreatsOnPoint(kingPos, new Point(movePoint.x, movePoint.y * inversionFactor), PieceType.PAWN);
	}
    }

    private void updateThreatsOnPoint(final Point kingPos, final Point movePoint, final PieceType pieceType) {
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

    private void updateThreatsAlongVector(final Point kingPos, final Point moveVector) {
	Set<Point> threat = new HashSet<>();

	boolean isCheckingDiagonals = (moveVector.x != 0 && moveVector.y != 0);
	boolean isDirectThreat = true;
	boolean isPin = false;

	int combinedX = kingPos.x + moveVector.x;
	int combinedY = kingPos.y + moveVector.y;

	while (true) {
	    if (!isValidTile(combinedX, combinedY)) {
		isDirectThreat = false;
		isPin = false;
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

	    } else if (!piece.getOwner().equals(getActivePlayer())) {
		// Hostile piece encountered

		// The queen, bishop and rook captures along a vector.
		if (piece.getType() != PieceType.QUEEN && !(isCheckingDiagonals && piece.getType() == PieceType.BISHOP) &&
		    !(!isCheckingDiagonals && piece.getType() == PieceType.ROOK)) {
		    isDirectThreat = false;
		    isPin = false;
		} else {
		    threat.add(new Point(combinedX, combinedY));
		}
		break;

	    } else {
		System.out.println("Code should not get here! (method updateThreats() in Board)");
	    }
	    combinedX += moveVector.x;
	    combinedY += moveVector.y;
	}

	if (isDirectThreat &&
	    !threat.isEmpty()) {
	    allDirectThreats.add(threat);
	} else if (isPin &&
		   !threat.isEmpty()) {
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

    private void detectGameOver() {
	if (getActivePlayer().getAvailableMoves().isEmpty()) {
	    if (isInCheck(getActivePlayer())) {
		// Checkmate detected
		displayGameOver(GameOverCauses.CHECKMATE);
		System.out.println("Checkmate detected!");
	    } else {
		// Stalemate detected
		displayGameOver(GameOverCauses.STALEMATE);
		System.out.println("Stalemate detected!");
	    }
	}
    }

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

