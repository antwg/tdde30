package se.liu.chess.gui;

import se.liu.chess.game.Board;

import javax.swing.*;
import java.awt.*;

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

    public void show(){
	frame.setLayout(new BorderLayout());
	frame.add(chessComponent, BorderLayout.CENTER);
	frame.pack();
	frame.setVisible(true);
    }

}
