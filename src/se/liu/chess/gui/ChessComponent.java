package se.liu.chess.gui;


import se.liu.chess.game.Board;

import javax.swing.*;
import java.awt.*;

public class ChessComponent extends JComponent
{
    private Board board;
    private int width;
    private int height;
    private final static int SQUARESIZE = 80;

    public ChessComponent(final Board board) {
	this.board = board;
	this.width = SQUARESIZE * board.getWidth();
	this.height = SQUARESIZE * board.getHeight();
    }

    @Override public Dimension getPreferredSize() {
	return new Dimension(width, height);
    }

    @Override protected void paintComponent(final Graphics g) {
	super.paintComponent(g);
	final Graphics2D g2d = (Graphics2D) g;

	for (int col = 0; col < board.getWidth(); col++) {
	    for (int row = 0; row < board.getHeight(); row++) {
		if ((col + row) % 2 == 0) {
		    g2d.setColor(Color.BLACK);
		    g2d.fillRect(col * (SQUARESIZE), row * (SQUARESIZE), SQUARESIZE, SQUARESIZE);
		}
	    }
	}
    }

}
