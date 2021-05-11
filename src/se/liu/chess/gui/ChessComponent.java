package se.liu.chess.gui;

import se.liu.chess.game.Board;
import se.liu.chess.game.Move;
import se.liu.chess.pieces.Piece;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;


/**
 * An extension of JComponent. Given a Board object, ChessComponent will be able to paint the board itself as well as the pieces on the board.
 * ChessComponent will loop over all all points in board and paint relevant data.
 * The graphics for the pieces are saved in resources.
 * The board argument will take any Board object and paint it.
 * Listens for mouse input.
 */

public class ChessComponent extends JComponent {
    private Board board;
    private int width, height;
    private Point currentlyPressed = null, lastPressed = null;
    private final static int SQUARE_SIZE = 72, IMG_SIZE = 64, OFFSET = (SQUARE_SIZE - IMG_SIZE) / 2;
    private final static Color ODD_COLOR = Color.DARK_GRAY, EVEN_COLOR = Color.WHITE, SELECTED_COLOR = new Color(0, 0, 255, 100);
    // Inspection doesn't like names (ex pieceb), the reason for having a lower case b is that thats how it's written in FEN
    //TODO replace ex. pieceb with bishopBlack?
    private ImageIcon pieceB = loadIMG("BishopWhite"), pieceb = loadIMG("BishopBlack"), pieceK = loadIMG("KingWhite"),
	    	      piecek = loadIMG("KingBlack"), pieceN = loadIMG("KnightWhite"), piecen = loadIMG("KnightBlack"),
		      pieceP = loadIMG("PawnWhite"), piecep = loadIMG("PawnBlack"), pieceQ = loadIMG("QueenWhite"),
	    	      pieceq = loadIMG("QueenBlack"), pieceR = loadIMG("RookWhite"), piecer = loadIMG("RookBlack");

    /**
     * Creates a chessComponent and a mouseListener.
     *
     * @param board A Board object
     */
    public ChessComponent(final Board board) {
	this.board = board;
	this.width = SQUARE_SIZE * board.getWidth();
	this.height = SQUARE_SIZE * board.getHeight();

	this.addMouseListener(new MouseAdapter(){
	    @Override public void mousePressed(final MouseEvent e) {
	        lastPressed = currentlyPressed;
		currentlyPressed = new Point(Math.floorDiv(e.getX(), SQUARE_SIZE), Math.floorDiv(e.getY(), SQUARE_SIZE)); // Finds what point on board
		Move move = getMadeMove();
		if (move != null) {
		    board.performMove(move);
		}
		repaint();
	    }
	});
    }

    // ----------------------------------------------------- Public Methods ----------------------------------------------------------------

    /**
     * Getter for Board.
     *
     * @return Board
     */
    public Board getBoard() {
	return board;
    }

    /**
     * Returns the preferred size of component.
     *
     * @return Dimension of preferred size
     */
    @Override public Dimension getPreferredSize() {
	return new Dimension(width, height);
    }

    // ----------------------------------------------------- Protected Methods -------------------------------------------------------------

    /**
     * Paints a board including the Pieces
     *
     * @param g Graphics object
     */
    @Override protected void paintComponent(final Graphics g) {
	super.paintComponent(g);
	final Graphics2D g2d = (Graphics2D) g;
	g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	// For all coordinates in board
	for (int col = 0; col < board.getWidth(); col++) {
	    for (int row = 0; row < board.getHeight(); row++) {

		// Determine color of square
		Color color = ODD_COLOR;
		Color color2 = new Color(0, 0, 0, 0);

		// Normal board colouring
		if ((col + row) % 2 == 0) {
		    color = EVEN_COLOR;
		}
		// If piece selected
		if (currentlyPressed != null && currentlyPressed.equals(new Point(col, row))){
		    color2 = SELECTED_COLOR;
		}
		// If valid move
		if (currentlyPressed != null && getTargetPointsFromMoveSet(currentlyPressed.x, currentlyPressed.y).contains(new Point(col, row))){
		    color2 = SELECTED_COLOR;
		}
		// Paint square
		g2d.setColor(color);
		g2d.fillRect(col * SQUARE_SIZE, row * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
		g2d.setColor(color2);
		g2d.fillRect(col * SQUARE_SIZE, row * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);

		// Paint piece if exists (not null)
		if (!board.isEmpty(col, row)) {
		    Piece piece = board.getPiece(col, row);
		    ImageIcon imageIcon = getImageForPiece(piece);
		    imageIcon.paintIcon(this, g, col * SQUARE_SIZE + OFFSET, row * SQUARE_SIZE + OFFSET);
	    }
	}
    }
}

    // ------------------------------------------------ Private Methods --------------------------------------------------------------------

    /**
     * Creates an ImageIcon ny loading a file "name" from resources.
     *
     * @param name The part of the name specific to that IMG e.g "BishopBlack" in "images/BishopBlack.png"
     * @return ImageIcon
     */
    private ImageIcon loadIMG(String name){
	return new ImageIcon(ClassLoader.getSystemResource("images/" + name + ".png"));
    }

    /**
     * Returns all the moves the active player can do for a given coordinate.
     *
     * @param x The x value of the coordinate
     * @param y The y value of the coordinate
     * @return A set of Moves
     */
    private Set<Move> getMovesForCoordinate(int x, int y){
        Set<Move> moves = new HashSet<>();
	for (Move move: board.getActivePlayer().getAvailableMoves()){
	    if (move.getOriginSquare().equals(new Point(x, y))){
		moves.add(move);
	    }
	}
	return moves;
    }

    /**
     * Given a coordinate, will return a set of Points containing the targetSquare
     * for all possible moves from the coordinate.
     *
     * @param x The x value of the coordinate
     * @param y The y value of the coordinate
     * @return A set of targetSquares
     */
    private Set<Point> getTargetPointsFromMoveSet(int x, int y){
	Set<Point> points = new HashSet<>();
	for (Move move: getMovesForCoordinate(x, y)){
		points.add(move.getTargetSquare());
	}
	return points;
    }

    /**
     * Returns the move that was made last click, or null if no move was just made.
     *
     * @return A move or null
     */
    private Move getMadeMove(){
        if (lastPressed != null){
            for (Move move : getMovesForCoordinate(lastPressed.x, lastPressed.y)){
                Point targetSquare = move.getTargetSquare();
                if (targetSquare.x == currentlyPressed.x && targetSquare.y == currentlyPressed.y){
		    return move;
		}
	    }
	}
        return null;
    }

    /**
     * Gets the ImageIcon for a given Piece object.
     *
     * @param piece A piece object
     * @return An imageIcon
     */
    private ImageIcon getImageForPiece(Piece piece){
	ImageIcon image = null;
	switch (piece.toString()){ // Useful to keep as string, both because "/" can't be used in enum and the main purpose is to convert from/to string
	    case "B":
		image = pieceB;
		break;
	    case "b":
		image = pieceb;
		break;
	    case "K":
		image = pieceK;
		break;
	    case "k":
		image = piecek;
		break;
	    case "N":
		image = pieceN;
		break;
	    case "n":
		image = piecen;
		break;
	    case "P":
		image = pieceP;
		break;
	    case "p":
		image = piecep;
		break;
	    case "Q":
		image = pieceQ;
		break;
	    case "q":
		image = pieceq;
		break;
	    case "R":
		image = pieceR;
		break;
	    case "r":
		image = piecer;
		break;
	}
	return image;
    }
}
