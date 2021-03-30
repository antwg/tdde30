package se.liu.chess.gui;

import se.liu.chess.game.Board;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Creates a window and paints ChessComponent.
 *
 * @param frame The frame in which everything else i placed in.
 * @param chessComponent Displays the board and pieces.
 * @param board The board handles the game state.
 */

public class BoardViewer
{
    private JFrame frame;
    private ChessComponent chessComponent;
    private Board board;

    public BoardViewer(Board board) {
        this.board = board;
	this.frame = new JFrame("Chess");
	this.chessComponent = new ChessComponent(board);
    }

    // ----------------------------------------------------- Public Methods ----------------------------------------------------------------

    public void show(){
	frame.setLayout(new BorderLayout());
	frame.add(chessComponent, BorderLayout.CENTER);
	frame.pack();
	frame.setVisible(true);
    }

}
