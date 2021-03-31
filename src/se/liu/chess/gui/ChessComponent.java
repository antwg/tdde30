package se.liu.chess.gui;


import se.liu.chess.game.Board;
import se.liu.chess.game.TeamColor;
import se.liu.chess.pieces.Piece;

import javax.swing.*;
import java.awt.*;

/**
 * Class for painting the board.
 *
 * @param board The board handles the game state.
 *
 */

public class ChessComponent extends JComponent
{
    private Board board;
    private int width;
    private int height;
    private final static int SQUARE_SIZE = 80;
    private final static Color ODD_COLOR = Color.DARK_GRAY, EVEN_COLOR = Color.WHITE; // When starting at 0
    private ImageIcon pieceB = new ImageIcon(ClassLoader.getSystemResource("images/BishopBlack.png"));
    private ImageIcon pieceb = new ImageIcon(ClassLoader.getSystemResource("images/BishopWhite.png"));
    private ImageIcon pieceK = new ImageIcon(ClassLoader.getSystemResource("images/KingBlack.png"));
    private ImageIcon piecek = new ImageIcon(ClassLoader.getSystemResource("images/KingWhite.png"));
    private ImageIcon pieceN = new ImageIcon(ClassLoader.getSystemResource("images/KnightBlack.png"));
    private ImageIcon piecen = new ImageIcon(ClassLoader.getSystemResource("images/KnightWhite.png"));
    private ImageIcon pieceP = new ImageIcon(ClassLoader.getSystemResource("images/PawnBlack.png"));
    private ImageIcon piecep = new ImageIcon(ClassLoader.getSystemResource("images/PawnWhite.png"));
    private ImageIcon pieceQ = new ImageIcon(ClassLoader.getSystemResource("images/QueenBlack.png"));
    private ImageIcon pieceq = new ImageIcon(ClassLoader.getSystemResource("images/QueenWhite.png"));
    private ImageIcon pieceR = new ImageIcon(ClassLoader.getSystemResource("images/RookBlack.png"));
    private ImageIcon piecer = new ImageIcon(ClassLoader.getSystemResource("images/RookWhite.png"));

    public ChessComponent(final Board board) {
	this.board = board;
	this.width = SQUARE_SIZE * board.getWidth();
	this.height = SQUARE_SIZE * board.getHeight();
    }

    public ImageIcon getImageForPiece(Piece piece, TeamColor color){
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

    @Override public Dimension getPreferredSize() {
	return new Dimension(width, height);
    }

    @Override protected void paintComponent(final Graphics g) {
	super.paintComponent(g);
	final Graphics2D g2d = (Graphics2D) g;
	g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			     RenderingHints.VALUE_ANTIALIAS_ON);

	for (int col = 0; col < board.getWidth(); col++) {
	    for (int row = 0; row < board.getHeight(); row++) {
		// Determine color of square
	        Color color = ODD_COLOR;
		if ((col + row) % 2 == 0) {
		    color = EVEN_COLOR;
		}
		// Set color
		g2d.setColor(color);
		g2d.fillRect(col * SQUARE_SIZE, row * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);

		if (board.getPiece(col, row) != null) {
		    getImageForPiece(board.getPiece(col, row),board.getPiece(col, row).getColor()).paintIcon(this, g, col * SQUARE_SIZE, row * SQUARE_SIZE);

		}
	    }
	}
    }
}
