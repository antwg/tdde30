package se.liu.chess.gui;

import se.liu.chess.game.Board;
import se.liu.chess.game.TeamColor;
import se.liu.chess.pieces.Piece;
import se.liu.chess.pieces.PieceType;

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

    public void pressedSquare(int x, int y){
	Point point = new Point(Math.floorDiv(x, SQUARE_SIZE), Math.floorDiv(y, SQUARE_SIZE)); // Finds what point on board
	Point lastPressed = currentlyPressed;
	this.currentlyPressed = point;

	// Stop from moving empty pieces (null)
	if(lastPressed != null && board.getPiece(lastPressed) != null){

	    // Get legal moves
	    if (getValidMoves(lastPressed.x, lastPressed.y).contains(currentlyPressed)) {
		board.movePiece(lastPressed, currentlyPressed);
	    }
	    tryToKillEnPassant();
	    setEnPassant(lastPressed);
	    this.currentlyPressed = null;
	}
	repaint();
    }

    @Override public Dimension getPreferredSize() {
	return new Dimension(width, height);
    }

    public Set<Point> getValidMoves(int x, int y){
	Piece selectedPiece = board.getPiece(x, y);
	Set<Point> moveSet = new HashSet<>();

	if (selectedPiece != null) {
	    moveSet = selectedPiece.getMoves(board, x, y);
	}
	return moveSet;
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
		Color color2 = new Color(0, 0, 0, 0);

		// Normal case
		if ((col + row) % 2 == 0) {
		    color = EVEN_COLOR;
		}
		// If selected
		if (currentlyPressed != null && currentlyPressed.equals(new Point(col, row))){
		    color2 = SELECTED_COLOR;
		}
		// If valid move
		if (currentlyPressed != null && getValidMoves(currentlyPressed.x, currentlyPressed.y).contains(new Point(col, row))){
		    color2 = SELECTED_COLOR;
		}
		// Paint square
		g2d.setColor(color);
		g2d.fillRect(col * SQUARE_SIZE, row * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
		g2d.setColor(color2);
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

    private void setEnPassant(Point lastPressed) {
	if (board.getPiece(currentlyPressed).getType() == PieceType.PAWN
	    && Math.abs(currentlyPressed.y - lastPressed.y) == 2){
	    board.setEnPassantTarget(lastPressed.x, (currentlyPressed.y + lastPressed.y) / 2);
	}
	else {
	    board.setEnPassantTarget(null);
	}
	board.getPiece(currentlyPressed).setHasMoved(true);
    }

    private void tryToKillEnPassant() {
	if (board.getEnPassantTarget() != null && board.getPiece(board.getEnPassantTarget()) != null){
	    Point ep = board.getEnPassantTarget();
	    int tempY = ep.y + 1;
	    if (board.getPiece(ep).getColor() == TeamColor.BLACK) {
		 tempY = ep.y - 1;
	    }
	    board.setPiece(ep.x, ep.y + 1, null);
	}
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
