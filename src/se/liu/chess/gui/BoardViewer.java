package se.liu.chess.gui;

import javax.swing.*;
import java.awt.*;

/**
 * BoardViewer creates a JFrame and adds a given ChessComponent to said JFrame.
 * It also handles all needed settings for the JFrame.
 */

public class BoardViewer
{
    private JFrame frame;
    private ChessComponent chessComponent;

    // Constructor
    public BoardViewer(ChessComponent chessComponent) {
	this.frame = new JFrame("Chess");
	this.chessComponent = chessComponent;
    }

    // ----------------------------------------------------- Public Methods ----------------------------------------------------------------

    public void show(){
	frame.setLayout(new BorderLayout());
	frame.add(chessComponent, BorderLayout.CENTER);
	frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	frame.pack();
	frame.setVisible(true);
    }

}
