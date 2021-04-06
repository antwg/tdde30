package se.liu.chess.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * BoardViewer creates a JFrame and adds a given ChessComponent to said JFrame.
 * It also handles all needed settings for the JFrame.
 */

public class BoardViewer
{
    private JFrame frame;
    private ChessComponent chessComponent;
    private JMenuBar menuBar;

    public BoardViewer(ChessComponent chessComponent) {
	this.frame = new JFrame("Chess");
	this.chessComponent = chessComponent;
	this.menuBar = new JMenuBar();
    }

    // ----------------------------------------------------- Public Methods ----------------------------------------------------------------

    public void show(){
	frame.setLayout(new BorderLayout());
	frame.add(chessComponent, BorderLayout.CENTER);
	setUpMenu();
	frame.add(menuBar, BorderLayout.NORTH);
	frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	frame.pack();
	frame.setVisible(true);
    }

    public void setUpMenu(){
	JMenu game = new JMenu("Game");
	final JMenuItem restart = new JMenuItem("Restart");
	game.add(restart);
	menuBar.add(game);
	restart.addActionListener(new MenuAction());
    }

    private class MenuAction extends AbstractAction {

	@Override public void actionPerformed(final ActionEvent e) {
	    chessComponent.getBoard().resetBoard();
	    chessComponent.repaint();
	}
    }
}
