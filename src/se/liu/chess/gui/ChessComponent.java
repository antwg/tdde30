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
    private final static int SQUARE_SIZE = 64, IMG_SIZE = 60, OFFSET = (SQUARE_SIZE - IMG_SIZE) / 2;
    private final static Color ODD_TILE_COLOR = Color.DARK_GRAY, EVEN_TILE_COLOR = Color.WHITE,
	                       HIGHLIGHT_COLOR = new Color(180, 190, 90, 100), TRANSPARENT = new Color(0, 0, 0, 0);
    private ImageIcon bishopWhite = loadIMG("BishopWhite"), bishopBlack = loadIMG("BishopBlack"),
	    	      kingWhite = loadIMG("KingWhite"), kingBlack = loadIMG("KingBlack"),
	    	      knightWhite = loadIMG("KnightWhite"), knightBlack = loadIMG("KnightBlack"),
		      pawnWhite = loadIMG("PawnWhite"), pawnBlack = loadIMG("PawnBlack"),
	    	      queenWhite = loadIMG("QueenWhite"), queenBlack = loadIMG("QueenBlack"),
		      rookWhite = loadIMG("RookWhite"), rookBlack = loadIMG("RookBlack");

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

	for (int col = 0; col < board.getWidth(); col++) {
	    for (int row = 0; row < board.getHeight(); row++) {

		Color tileColor = ODD_TILE_COLOR;
		Color highlightColor = TRANSPARENT;

		if (isEvenTile(col, row)) tileColor = EVEN_TILE_COLOR;

		if (isSelectedPiece(col, row)) highlightColor = HIGHLIGHT_COLOR;

		if (isValidMove(col, row)) highlightColor = HIGHLIGHT_COLOR;

		g2d.setColor(tileColor);
		g2d.fillRect(col * SQUARE_SIZE, row * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);

		g2d.setColor(highlightColor);
		g2d.fillRect(col * SQUARE_SIZE, row * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);

		// Paint pieces
		if (!board.isEmpty(col, row)) {
		    ImageIcon pieceImage = getImageForPiece(board.getPiece(col, row));
		    pieceImage.paintIcon(this, g, col * SQUARE_SIZE + OFFSET, row * SQUARE_SIZE + OFFSET);
	    }
	}
    }
}

    // ------------------------------------------------ Private Methods --------------------------------------------------------------------

    private boolean isEvenTile(final int col, final int row){
        return (col + row) % 2 == 0;
    }

    private boolean isSelectedPiece(final int col, final int row){
        return currentlyPressed != null && currentlyPressed.equals(new Point(col, row));
    }

    private boolean isValidMove(final int col, final int row){
	return currentlyPressed != null && getTargetPointsFromMoveSet(currentlyPressed.x, currentlyPressed.y).contains(new Point(col, row));
    }

    private ImageIcon loadIMG(String name){
	return new ImageIcon(ClassLoader.getSystemResource("images/" + name + ".png"));
    }

    private Set<Move> getMovesForCoordinate(int x, int y){
        Set<Move> moves = new HashSet<>();
	for (Move move: board.getMoves()){
	    if (move.getOriginSquare().equals(new Point(x, y))){
		moves.add(move);
	    }
	}
	return moves;
    }

    private Set<Point> getTargetPointsFromMoveSet(int x, int y){
	Set<Point> points = new HashSet<>();
	for (Move move: getMovesForCoordinate(x, y)){
		points.add(move.getTargetSquare());
	}
	return points;
    }

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

    private ImageIcon getImageForPiece(Piece piece){
	ImageIcon image = null;
	switch (piece.toString()){ // (Inspection) Useful to keep as string because the main purpose is to convert from/to string
	    case "B":
		image = bishopWhite;
		break;
	    case "b":
		image = bishopBlack;
		break;
	    case "K":
		image = kingWhite;
		break;
	    case "k":
		image = kingBlack;
		break;
	    case "N":
		image = knightWhite;
		break;
	    case "n":
		image = knightBlack;
		break;
	    case "P":
		image = pawnWhite;
		break;
	    case "p":
		image = pawnBlack;
		break;
	    case "Q":
		image = queenWhite;
		break;
	    case "q":
		image = queenBlack;
		break;
	    case "R":
		image = rookWhite;
		break;
	    case "r":
		image = rookBlack;
		break;
	}
	return image;
    }
}
