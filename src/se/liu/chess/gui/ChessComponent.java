package se.liu.chess.gui;


import se.liu.chess.game.Board;

import javax.swing.*;
import java.awt.*;

/**
 * Class for painting the board.
 *
 * @param board The board handles the game state.
 * @param witdh The width of the component.
 * @param height The height of the component.
 * @param SQUARESIZE The size of a square(on the board).
 */

public class ChessComponent extends JComponent
{
    private Board board;
    private int width;
    private int height;
    private final static int SQUARE_SIZE = 80;

    public ChessComponent(final Board board) {
	this.board = board;
	this.width = SQUARE_SIZE * board.getWidth();
	this.height = SQUARE_SIZE * board.getHeight();
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
		    g2d.fillRect(col * (SQUARE_SIZE), row * (SQUARE_SIZE), SQUARE_SIZE, SQUARE_SIZE);
		}
	    }
	}
    }

}
