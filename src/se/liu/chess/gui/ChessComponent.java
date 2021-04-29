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
    private Point currentlyPressed = null;
    private final static int SQUARE_SIZE = 72, IMG_SIZE = 64, OFFSET = (SQUARE_SIZE - IMG_SIZE) / 2;
    private final static Color ODD_COLOR = Color.DARK_GRAY, EVEN_COLOR = Color.WHITE, SELECTED_COLOR = new Color(0, 0, 255, 100);
    // Inspection doesn't like names (ex pieceb), the reason for having a lower case b is that thats how it's written in FEN
    private ImageIcon pieceB = loadIMG("BishopWhite"), pieceb = loadIMG("BishopBlack"), pieceK = loadIMG("KingWhite"),
	    	      piecek = loadIMG("KingBlack"), pieceN = loadIMG("KnightWhite"), piecen = loadIMG("KnightBlack"),
		      pieceP = loadIMG("PawnWhite"), piecep = loadIMG("PawnBlack"), pieceQ = loadIMG("QueenWhite"),
	    	      pieceq = loadIMG("QueenBlack"), pieceR = loadIMG("RookWhite"), piecer = loadIMG("RookBlack");

    public ChessComponent(final Board board) {
	this.board = board;
	this.width = SQUARE_SIZE * board.getWidth();
	this.height = SQUARE_SIZE * board.getHeight();

	this.addMouseListener(new MouseAdapter(){
	    @Override public void mousePressed(final MouseEvent e) {
		Point point = new Point(Math.floorDiv(e.getX(), SQUARE_SIZE), Math.floorDiv(e.getY(), SQUARE_SIZE)); // Finds what point on board
		board.getMoves(point.x, point.y);//pressedSquare(point);
		currentlyPressed = point;
		// getMoves() behöver inte kallas på här, kalla på perform move
		// board.performMove(move);
		repaint();
	    }
	});
    }

    // ----------------------------------------------------- Public Methods ----------------------------------------------------------------

    public Board getBoard() {
	return board;
    }

    @Override public Dimension getPreferredSize() {
	return new Dimension(width, height);
    }

    // ----------------------------------------------------- Protected Methods -------------------------------------------------------------


    @Override protected void paintComponent(final Graphics g) {
        Point currentlyPressed = this.currentlyPressed;//board.getCurrentlyPressed();
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
		if (currentlyPressed != null && getCurrentlyPressedMoves(currentlyPressed.x, currentlyPressed.y).contains(new Point(col, row))){
		    color2 = SELECTED_COLOR;
		}
		// If valid move
		//if (currentlyPressed != null && board.getActivePlayer().getAvailableMoves().contains(move)){//board.getValidMoves(currentlyPressed.x, currentlyPressed.y).contains(new Point(col, row))){
		//    color2 = SELECTED_COLOR;
		//}
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

    //private Move getChosenMove(Point point){
    //    Set<Move> allMoves = board.getMoves(point.x, point.y);
    //
    //}

    private ImageIcon loadIMG(String name){
	return new ImageIcon(ClassLoader.getSystemResource("images/" + name + ".png"));
    }

    private Set<Point> getCurrentlyPressedMoves(int x, int y){
        Set<Point> pointSet = new HashSet<>();
	for (Move move: board.getActivePlayer().getAvailableMoves()){
	    if (move.getOriginSquare().equals(new Point(x, y))){
		pointSet.add(move.getTargetSquare());
	    }
	}
	return pointSet;
    }

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
