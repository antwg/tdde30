package se.liu.chess.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * BoardViewer creates a JFrame and adds a given ChessComponent to said JFrame.
 * It also handles all needed settings for the JFrame.
 */

public class GameViewer
{
    private JFrame frame;
    private ChessComponent chessComponent;
    private JMenuBar menuBar;
    private TimeComponent timeComponent;

    public GameViewer(ChessComponent chessComponent, TimeComponent timeComponent) {
	this.chessComponent = chessComponent;
	this.menuBar = new JMenuBar();
	this.timeComponent = timeComponent;
    }

    // ----------------------------------------------------- Public Methods ----------------------------------------------------------------

    public void show(){
        this.frame = new JFrame("Chess");
	frame.setLayout(new BorderLayout());
	frame.add(chessComponent, BorderLayout.CENTER);
	setUpMenu();
	frame.add(menuBar, BorderLayout.NORTH);
	frame.add(timeComponent, BorderLayout.EAST);
	frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	frame.pack();
	frame.setVisible(true);
    }

    // ----------------------------------------------------- Private Methods ---------------------------------------------------------------


    private void setUpMenu(){
	JMenu game = new JMenu("Game");
	final JMenuItem restart = new JMenuItem("Restart");
	game.add(restart);
	menuBar.add(game);
	restart.addActionListener(new MenuAction());
    }

    // ----------------------------------------------------- Public Classes ----------------------------------------------------------------


    private class MenuAction extends AbstractAction {

	@Override public void actionPerformed(final ActionEvent e) {
	    chessComponent.getBoard().resetBoard();
	    chessComponent.repaint();
	}
    }
}
