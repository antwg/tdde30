package se.liu.chess.gui;

import se.liu.chess.game.Board;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * BoardViewer creates a JFrame and adds a given ChessComponent to said JFrame.
 * It also handles all needed settings for the JFrame.
 * Takes a chessComponent from a Board object and a timeComponent as an argument, also creates a new JMenuBar
 */

public class GameViewer {
    private ChessComponent chessComponent;
    private JMenuBar menuBar;
    private TimeComponent timeComponent;

    /**
     * Creates a GameViewer.
     *
     * @param board A board object
     * @param timeComponent A timeComponent
     */
    public GameViewer(Board board, TimeComponent timeComponent) {
	this.chessComponent = board.getChessComponent();
	this.menuBar = new JMenuBar();
	this.timeComponent = timeComponent;
    }

    // ----------------------------------------------------- Public Methods ----------------------------------------------------------------

    /**
     * Creates a new JFrame, adds chessComponent, timeComponent and a menuBar.
     * Then shows frame
     */
    public void show(){
        JFrame frame = new JFrame("Chess");
	frame.setLayout(new BorderLayout());
	frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

	setUpMenu();

	frame.add(chessComponent, BorderLayout.WEST);
	frame.add(menuBar, BorderLayout.NORTH);
	frame.add(timeComponent, BorderLayout.CENTER);

	frame.pack();
	frame.setLocationRelativeTo(null);
	frame.setVisible(true);
    }

    // ----------------------------------------------------- Private Methods ---------------------------------------------------------------

    /**
     * Creates a JMenu and adds a restart button
     */
    private void setUpMenu(){
	JMenu game = new JMenu("Game");
	final JMenuItem restart = new JMenuItem("Restart");
	game.add(restart);
	menuBar.add(game);
	restart.addActionListener(new MenuAction());
    }

    // ----------------------------------------------------- Private Classes ----------------------------------------------------------------


    private class MenuAction extends AbstractAction {

	@Override public void actionPerformed(final ActionEvent e) {	// (komplettering) (kommentar 1) frågar nu om bekräftelse innan omstart.
	    String message = "Confirm restart?";
	    String[] options = {"Restart", "Cancel"};

	    int choice = JOptionPane.showOptionDialog(null, message, "", JOptionPane.DEFAULT_OPTION,
						      JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

	    if (choice == 0) {
		chessComponent.getBoard().resetBoard();
		chessComponent.repaint();
	    }
	}
    }
}
