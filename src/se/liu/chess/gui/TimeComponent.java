package se.liu.chess.gui;

import se.liu.chess.game.Board;

import javax.swing.*;
import java.awt.*;

public class TimeComponent extends JComponent {
    private int width;
    private int height;
    private Board board;

    public TimeComponent(Board board, final int width, final int height) {
	this.width = width;
	this.height = height;
	this.board = board;
    }

    @Override protected void paintComponent(final Graphics g) {
	super.paintComponent(g);
	final Graphics2D g2d = (Graphics2D) g;
	int yOffset = 16, xOffset = 10, doubleXOffset = 2 * xOffset, middle = height / 2 + 32, fontSize = 50;
	double blackTime = board.getBlackPlayer().getTimeLeft();
	double whiteTime = board.getWhitePlayer().getTimeLeft();

	g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	g.setColor(Color.BLACK);
	g.setFont(new Font("serif", Font.PLAIN, fontSize));
	g.drawString(Double.toString(blackTime), width / 2 - fontSize, middle - yOffset);
	g.drawString(Double.toString(whiteTime), width / 2 - fontSize, middle + 3 * yOffset);

	g.fillRect(xOffset, middle, width - doubleXOffset,3);
    }

    @Override public Dimension getPreferredSize() {
	return new Dimension(width, height);
    }
}

