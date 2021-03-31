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

    private final static int SQUARE_SIZE = 64;
    private final static Color ODD_COLOR = Color.DARK_GRAY, EVEN_COLOR = Color.WHITE; // When starting at 0
    private ImageIcon pieceB = loadIcon("BishopBlack"), pieceb = loadIcon("BishopWhite"), pieceK = loadIcon("KingBlack"),
	    	      piecek = loadIcon("KingWhite"), pieceN = loadIcon("KnightBlack"), piecen = loadIcon("KnightWhite"),
		      pieceP = loadIcon("PawnBlack"), piecep = loadIcon("PawnWhite"), pieceQ = loadIcon("QueenBlack"),
		      pieceq = loadIcon("QueenWhite"), pieceR = loadIcon("RookBlack"), piecer = loadIcon("RookWhite");

    public ChessComponent(final Board board) {
	this.board = board;
	this.width = SQUARE_SIZE * board.getWidth();
	this.height = SQUARE_SIZE * board.getHeight();
    }

    // ----------------------------------------------------- Public/Protected Methods ------------------------------------------------------

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
		// Paint square
		g2d.setColor(color);
		g2d.fillRect(col * SQUARE_SIZE, row * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);

		// Paint piece if exists
		if (!board.isEmpty(col, row)) {
		    Piece piece = board.getPiece(col, row);
		    ImageIcon image = getImageForPiece(piece, piece.getColor());
		    image.paintIcon(this, g, col * SQUARE_SIZE, row * SQUARE_SIZE);
	    }
	}
    }
}

    // ------------------------------------------------ Private Methods --------------------------------------------------------------------



    private ImageIcon loadIcon(String name){
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
