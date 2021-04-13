package se.liu.chess.game;

import se.liu.chess.gui.GameViewer;
import se.liu.chess.gui.ChessComponent;
import se.liu.chess.gui.TimeComponent;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Manages a game of Chess. It creates all needed objects and runs the game.
 */

public class GameManager
{
    private Board board;
    private TimeComponent timeComponent = null;
    private static final int CLOCK_DELAY = 1000;

    public GameManager() {
        this.board = new Board(8, 8);
    }

    // ----------------------------------------------------- Public Methods ----------------------------------------------------------------

    public void createNewGame() {
        this.board = new Board(8, 8);
    }

    final Action countDown = new AbstractAction() {

        @Override public void actionPerformed(final ActionEvent e) {
            board.getActivePlayer().setTimeLeft(board.getActivePlayer().getTimeLeft() - 1);
            timeComponent.repaint();
        }
    };


    public static void main(String[] args) {
        // Testing

        GameManager gm = new GameManager();
        gm.createNewGame();

        gm.board.resetBoard();
        System.out.println(gm.board.convertBoardToFEN());

        gm.timeComponent = new TimeComponent(gm.board, 180, 512);
        GameViewer gameViewer = new GameViewer(new ChessComponent(gm.board), gm.timeComponent);
        gameViewer.show();

        final Timer clockTimer = new Timer(CLOCK_DELAY, gm.countDown);
        clockTimer.setCoalesce(true);
        clockTimer.start();
    }
}
