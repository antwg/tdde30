package se.liu.chess.gui;

import se.liu.chess.game.Board;
import se.liu.chess.game.TeamColor;
import se.liu.chess.pieces.Piece;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * An extension of JComponent. Given a Board object, ChessComponent will be able to paint the board itself as well as the pieces on the board.
 * ChessComponent will loop over all all points in board and paint relevant data.
 * The graphics for the pieces are saved in resources.
 * The board argument will take any Board object and paint it.
 */

public class ChessComponent extends JComponent {
    private Board board;
    private int width, height;
    private Point currentlyPressed = null;
    private final static int SQUARE_SIZE = 72, IMGSIZE = 64, OFFSET = (SQUARE_SIZE - IMGSIZE) / 2;
    private final static Color ODD_COLOR = Color.DARK_GRAY, EVEN_COLOR = Color.WHITE, SELECTED_COLOR = Color.RED;
    private ImageIcon pieceB = loadIMG("BishopWhite"), pieceb = loadIMG("BishopBlack"), pieceK = loadIMG("KingWhite"),
	    	      piecek = loadIMG("KingBlack"), pieceN = loadIMG("KnightWhite"), piecen = loadIMG("KnightBlack"),
		      pieceP = loadIMG("PawnWhite"), piecep = loadIMG("PawnBlack"), pieceQ = loadIMG("QueenWhite"),
	    	      pieceq = loadIMG("QueenBlack"), pieceR = loadIMG("RookWhite"), piecer = loadIMG("RookBlack");

    // Constructor
    public ChessComponent(final Board board) {
	this.board = board;
	this.width = SQUARE_SIZE * board.getWidth();
	this.height = SQUARE_SIZE * board.getHeight();

	this.addMouseListener(new MouseAdapter(){
	    @Override public void mousePressed(final MouseEvent e) {
		pressedSquare(e.getX(), e.getY());
	    }
	});
    }

    // ----------------------------------------------------- Public/Protected Methods ------------------------------------------------------

    public Point pressedSquare(int x, int y){ // TODO Lägga på lämpligt ställe
	Point point = new Point(Math.floorDiv(x, SQUARE_SIZE), Math.floorDiv(y, SQUARE_SIZE)); // Finds what point in board
	final Point lastPressed = currentlyPressed;
	this.currentlyPressed = point;
	if(lastPressed != null && board.getPiece(lastPressed) != null){
		board.movePiece(lastPressed, currentlyPressed);
		currentlyPressed = null;
	}
	repaint();
	return point;
    }

    @Override public Dimension getPreferredSize() {
	return new Dimension(width, height);
    }

    @Override protected void paintComponent(final Graphics g) {
	super.paintComponent(g);
	final Graphics2D g2d = (Graphics2D) g;
	g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	// For all coordinates in board
	for (int col = 0; col < board.getWidth(); col++) {
	    for (int row = 0; row < board.getHeight(); row++) {

		// Determine color of square
		Color color = ODD_COLOR;
		if ((col + row) % 2 == 0) {
		    color = EVEN_COLOR;
		}
		if (currentlyPressed != null && currentlyPressed.equals(new Point(col, row))){
		    color =  SELECTED_COLOR;
		}
		// Paint square
		g2d.setColor(color);
		g2d.fillRect(col * SQUARE_SIZE, row * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);

		// Paint piece if exists
		if (!board.isEmpty(col, row)) {
		    Piece piece = board.getPiece(col, row);
		    ImageIcon imageIcon = getImageForPiece(piece, piece.getColor());
		    imageIcon.paintIcon(this, g, col * SQUARE_SIZE + OFFSET, row * SQUARE_SIZE + OFFSET);
	    }
	}
    }
}

    // ------------------------------------------------ Private Methods --------------------------------------------------------------------



    private ImageIcon loadIMG(String name){
	return new ImageIcon(ClassLoader.getSystemResource("images/" + name + ".png"));
    }

    private ImageIcon getImageForPiece(Piece piece, TeamColor color){
	ImageIcon image = null;
	switch (piece.toString()){
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
