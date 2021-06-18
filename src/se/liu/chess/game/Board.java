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

/**
 * Creates a board which handles all game logic and saves the position of pieces. Has methods for moving pieces and checking for checkmate.
 * Creates FenCOnverter, Players and Chesscomponent.
 */
public class Board
{
    private Piece[][] pieces;
    private static final int WIDTH = 8, HEIGHT = 8;

    private Player whitePlayer, blackPlayer;

    private int activePlayerIndex = 0;
    private boolean gameOver;

    private final FenConverter fenConverter;

    private Point enPassantTarget = null;
    private final ChessComponent chessComponent;

    private int halfMoveClock = 0, fullMoveNumber = 1;

    private final Point[] vectorThreatDirections = { new Point(1, 1),
	    					     new Point(1, -1),
						     new Point(-1, 1),
						     new Point(-1, -1),
						     new Point(1, 0),
						     new Point(0, 1),
						     new Point(-1, 0),
						     new Point(0, -1) };

    private final Point[] pawnThreatDirections = { new Point(-1, -1),
	    					   new Point(1, -1) };

    private List<Set<Point>> allDirectThreats = new ArrayList<>(), allPins = new ArrayList<>();

    // (komplettering) (kommentar 4) gjorde KNIGHT_ATTACKS statisk.
    public final static Point[] KNIGHT_ATTACKS = { new Point(1, 2),
	    new Point( 2, 1),
	    new Point(1, -2),
	    new Point(2, -1),
	    new Point(-1, 2),
	    new Point(-2, 1),
	    new Point(-1, -2),
	    new Point(-2, -1) };

    // (komplettering) (kommentar 4) gjorde ORTHOGONAL_VECTORS statisk.
    public final static Point[] ORTHOGONAL_VECTORS = { new Point(1, 0),
	    new Point(0, 1),
	    new Point(-1, 0),
	    new Point(0, -1) };

    // (komplettering) (kommentar 4) gjorde DIAGONAL_VECTORS statisk.
    public final static Point[] DIAGONAL_VECTORS = { new Point(1, 1),
						    new Point(1, -1),
						    new Point(-1, 1),
						    new Point(-1, -1)};

    public Board() {
		this.pieces = new Piece[WIDTH][HEIGHT];
		this.fenConverter = new FenConverter(this);
		this.chessComponent = new ChessComponent(this);
		this.whitePlayer = new Player(TeamColor.WHITE);
		this.blackPlayer = new Player(TeamColor.BLACK);

		clearBoard();
    }

    // ----------------------------------------------------- Public Methods ----------------------------------------------------------------

    /**
     * Returns a set of all legal moves that are possible to make at the current game state.
     * @return
     */
    public Set<Move> getMoves() {
        return getActivePlayer().getAvailableMoves();
    }

    /**
     * Moves the piece on the origin square of the move to the target square
     * and performs promotion. (not yet implemented)
     * Updates castling availability and en passant square.
     * @param move
     */
    public void performMove(Move move) {
	if (move.isCastling()) {
	    performCastling(move);
	}

	move.getMovingPiece().performSpecialMove(move, enPassantTarget, this);

	if (move.isPawnDoubleStep()) {
	    setEnPassantTarget(move);
	} else {
	    setEnPassantTarget(null);
	}

	movePiece(move.getOriginSquare(), move.getTargetSquare());

	if (move.isPromoting()) {
	    promote(move);
	}
	passTurn();
    }

    /**
     * Returns true if given coordinates lie within the board, otherwise false.
     * @param x
     * @param y
     * @return
     */
    public boolean isValidTile(int x, int y) {
	return (0 <= x && x < WIDTH && 0 <= y && y < HEIGHT);
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
	enPassantTarget = null;
	fenConverter.createBoardFromFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
	blackPlayer.resetTime();
	whitePlayer.resetTime();
	gameOver = false;
	enPassantTarget = null;

	updateThreats(getActivePlayer());
	updateAvailableMoves(getActivePlayer());
    }

    /**
     * Returns true if targetSquare is protected, else false
     *
     * @param targetSquare square with piece on
     * @return true if protected, else false
     */
    public boolean isSquareProtectedByPlayer(final Point targetSquare, final Player protectingPlayer) {
	if (isDiagonallyProtected(targetSquare, protectingPlayer) ||
	    isOrthogonallyProtected(targetSquare, protectingPlayer) ||
	    isProtectedFromKnights(targetSquare, protectingPlayer) ||
	    isProtectedFromPawns(targetSquare, protectingPlayer))
	{
	    return true;
	}
	return false;
    }

    public void updateAvailableMoves(final Player player) {
	Set<Move> availableMoves = new HashSet<>();

	for (int y = 0; y < HEIGHT; y++) {
	    for (int x = 0; x < WIDTH; x++) {
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

    // ----------------------------------------------------- Private Methods ---------------------------------------------------------------

    /**
     * Replaces the piece on the target square with the piece on the origin square
     * and sets the origin square to empty.
     * @param originSquare
     * @param targetSquare
     */
    private void movePiece(Point originSquare, Point targetSquare) {
	Piece pieceToMove = getPiece(originSquare);
	setPiece(targetSquare, pieceToMove);
	setPiece(originSquare, null);

	pieceToMove.setPosition(targetSquare);
    }

    private void promote(final Move move) {
	final int queen = 0, rook = 1, bishop = 2, knight = 3;
	Point targetSquare = move.getTargetSquare();

	int choice = chessComponent.selectPromotion();	// (komplettering) (kommentar 3) chessComponent visar nu JOptionPane istället för Board.

	switch(choice){
	    case rook:
		setPiece(targetSquare, new Rook(getActivePlayer(), targetSquare));
		break;
	    case bishop:
		setPiece(targetSquare, new Bishop(getActivePlayer(), targetSquare));
		break;
	    case knight:
		setPiece(targetSquare, new Knight(getActivePlayer(), targetSquare));
		break;
	    case queen:
	    default:
		setPiece(targetSquare, new Queen(getActivePlayer(), targetSquare));
	}
    }

    /**
     * Removes castling availability and moves the rooks for castling.
     * @param move
     */
    private void performCastling(final Move move) {
	getActivePlayer().setKingSideCastleAvailable(false);
	getActivePlayer().setQueenSideCastleAvailable(false);

	final int kingXCastlingQueenSide = 2, kingXCastlingKingSide = 6;

	if (move.getTargetSquare().x == kingXCastlingQueenSide) {
	    final int rookOriginXQueenSide = 0, rookTargetXQueenSide = 3;

	    moveCastlingRook(rookOriginXQueenSide, rookTargetXQueenSide);
	}
	else if (move.getTargetSquare().x == kingXCastlingKingSide) {
	    final int rookOriginXKingSide = 7, rookTargetXKingSide = 5;

	    moveCastlingRook(rookOriginXKingSide, rookTargetXKingSide);
	}
    }

    private void moveCastlingRook(final int originX, final int targetX) {
	int rank = getActivePlayer().getHomeRank();	// (komplettering) (kommentar 2)

	movePiece(new Point(originX, rank), new Point(targetX, rank));
    }

    private boolean isProtectedFromPawns(final Point targetSquare, final Player protectingPlayer) {
	final Point[] pawnAttacks = { new Point(-1, -protectingPlayer.getForwardDirection()),
				      new Point(1, -protectingPlayer.getForwardDirection()) };

	return isProtectedFromPoints(targetSquare, protectingPlayer, pawnAttacks, PieceType.PAWN);
    }

    private boolean isProtectedFromKnights(final Point targetSquare, final Player protectingPlayer) {
	// (komplettering) (kommentar 4) gjorde KNIGHT_ATTACKS statisk.

	return isProtectedFromPoints(targetSquare, protectingPlayer, KNIGHT_ATTACKS, PieceType.KNIGHT);
    }

    private boolean isOrthogonallyProtected(final Point targetSquare, final Player protectingPlayer) {
	// (komplettering) (kommentar 4) gjorde ORTHOGONAL_VECTORS statisk.

	return isProtectedFromVectors(targetSquare, protectingPlayer, ORTHOGONAL_VECTORS, PieceType.ROOK);
    }

    private boolean isDiagonallyProtected(final Point targetSquare, final Player protectingPlayer) {
	// (komplettering) (kommentar 4) gjorde DIAGONAL_VECTORS statisk.

	return isProtectedFromVectors(targetSquare, protectingPlayer, DIAGONAL_VECTORS, PieceType.BISHOP);
    }

    private boolean isProtectedFromPoints(final Point targetSquare, final Player protectingPlayer, final Point[] attacks, final PieceType pieceType) {
	for (Point attack : attacks) {
	    int combinedX = targetSquare.x + attack.x;
	    int combinedY = targetSquare.y + attack.y;

	    if (!isValidTile(combinedX, combinedY)) {
		continue;
	    }

	    Piece pieceOnSquare = getPiece(combinedX, combinedY);
	    TeamColor protectingColor = protectingPlayer.getColor();

	    if (pieceOnSquare != null &&
		pieceOnSquare.getColor().equals(protectingColor) &&
		pieceOnSquare.getType().equals(pieceType)) {
		return true;
	    }
	}
	return false;
    }

    private boolean isProtectedFromVectors(final Point targetSquare, final Player protectingPlayer, final Point[] vectors, final PieceType pieceType) {
	for (Point vector : vectors) {
	    int combinedX = targetSquare.x + vector.x;
	    int combinedY = targetSquare.y + vector.y;

	    while (isValidTile(combinedX, combinedY)) {

		Piece pieceOnSquare = getPiece(combinedX, combinedY);
		TeamColor protectingColor = protectingPlayer.getColor();

		if (pieceOnSquare == null ||
		    pieceOnSquare.getType().equals(PieceType.KING) && !pieceOnSquare.getColor().equals(protectingColor)) {
		    combinedX += vector.x;
		    combinedY += vector.y;
		} else if (pieceOnSquare.getColor().equals(protectingColor) &&
			   (pieceOnSquare.getType().equals(pieceType) || pieceOnSquare.getType().equals(PieceType.QUEEN))) {
		    return true;
		} else {
		    break;
		}
	    }
	}
	return false;
    }

    private void passTurn() { // --- Pass is used as a verb (inspection)
        getActivePlayer().increaseTimeByIncrement();

	// Update active player
	int nextActivePlayerIndex = (activePlayerIndex + 1) % 2;
	activePlayerIndex = nextActivePlayerIndex;

	if (nextActivePlayerIndex == 0) {
	    fullMoveNumber++;
	}

	updateThreats(getActivePlayer());

	updateAvailableMoves(getInactivePlayer());

	updateAvailableMoves(getActivePlayer());

	detectGameOver();
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
	for (Point movePoint : Knight.getKnightMoves()) {
	    updateThreatsOnPoint(kingPos, movePoint, PieceType.KNIGHT);
	}

	// Point moves are for white pieces by default. Can be inverted along the
	// y-axis to get threats for black.
	for (Point movePoint : pawnThreatDirections) {
	    int forwardDirection = getInactivePlayer().getForwardDirection();

	    updateThreatsOnPoint(kingPos, new Point(movePoint.x, movePoint.y * forwardDirection), PieceType.PAWN);
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
	    piece.getType().equals(pieceType)) {
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

	    } else {
		// Hostile piece encountered

		// The queen, bishop and rook captures along a vector.
		if (!(piece.getType().equals(PieceType.QUEEN)) && !(isCheckingDiagonals && piece.getType().equals(PieceType.BISHOP)) &&
		    !(!isCheckingDiagonals && piece.getType().equals(PieceType.ROOK))) {
		    isDirectThreat = false;
		    isPin = false;
		} else {
		    threat.add(new Point(combinedX, combinedY));
		}
		break;

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

    private void detectGameOver() {
	if (getActivePlayer().getAvailableMoves().isEmpty()) {
	    GameOverCauses cause;
	    if (isInCheck(getActivePlayer())) {
		cause = GameOverCauses.CHECKMATE;
	    } else {
		cause = GameOverCauses.STALEMATE;
	    }
	    gameOver = true;

	    // (komplettering) (kommentar 3) kallar på executeGameOver() istället för displayGameOver().
	    executeGameOver(cause);
	}
    }

    // (komplettering) (kommentar 3) ny metod som låter GUI:n visa game over-meddelandet istället.
    /**
     * Displays a game over message and asks the player if they want to restart.
     *
     * @param cause The cause for game over to be displayed in the message.
     */
    public void executeGameOver(GameOverCauses cause) {
	final int restart = 0, quit = 1;
	int choice = chessComponent.displayGameOverMessage(cause);

	switch (choice){
	    case restart:
		resetBoard();
		break;
	    case quit:
		System.exit(0);
	}
    }

    private void clearBoard() {
	for (int y = 0; y < HEIGHT; y++) {
	    for (int x = 0; x < WIDTH; x++) {
		pieces[y][x] = null;
	    }
	}
    }

    // ---------------------------------------------------- Getters/Setters ----------------------------------------------------------------

    public int getWidth() {
	return WIDTH;
    }

    public int getHeight() {
	return HEIGHT;
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

    public void setActivePlayerIndex(final int activePlayerIndex) {
	this.activePlayerIndex = activePlayerIndex;
    }

    public Point getEnPassantTarget() {
	return enPassantTarget;
    }

    public void setEnPassantTarget(Move move){
	if (move == null) {
	    this.enPassantTarget = null;
	}
	else {
	    this.enPassantTarget = new Point(move.getOriginSquare().x, getActivePlayer().getEnPassantRow());
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

    public boolean isGameOver() {
	return gameOver;
    }

    public void setGameOver(boolean gameOver) {
	this.gameOver = gameOver;
    }

    public List<Set<Point>> getAllDirectThreats() {
	return allDirectThreats;
    }

    public List<Set<Point>> getAllPins() {
	return allPins;
    }
}

