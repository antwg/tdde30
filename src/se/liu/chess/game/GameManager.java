package se.liu.chess.game;

import se.liu.chess.gui.GameViewer;
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

    /**
     * Creates a GameManager.
     * Also creates a new Board object.
     */
    public GameManager() {
        this.board = new Board(8, 8, this);
    }

    // ----------------------------------------------------- Public Methods ----------------------------------------------------------------

    /**
     * Creates a new Board and replaces the old one.
     */
    public void createNewGame() {
        this.board = new Board(8, 8, this);
    }


    // ----------------------------------------------------- Private Methods ---------------------------------------------------------------

    private final Action countDown = new AbstractAction() {

        @Override public void actionPerformed(final ActionEvent e) {
            Player activePlayer = board.getActivePlayer();

            if (!board.isGameOver()) {
                if (activePlayer.getTimeLeft() <= 0) {
                    board.setGameOver(true);
                    board.displayGameOver(GameOverCauses.TIME);
                }
                else {
                    activePlayer.setTimeLeft(activePlayer.getTimeLeft() - 1);
                    timeComponent.repaint();
                }
            }
        }
    };

    // ------------------------------------------------------- Main Method -----------------------------------------------------------------

    public static void main(String[] args) {
        GameManager gm = new GameManager();
        gm.createNewGame();

        gm.board.resetBoard();
        System.out.println(gm.board.getFenConverter().convertBoardToFEN());

        gm.timeComponent = new TimeComponent(gm.board, 180, 512);
        GameViewer gameViewer = new GameViewer(gm.board, gm.timeComponent);
        gameViewer.show();
        gm.board.updateAvailableMoves(gm.board.getActivePlayer());

        final Timer clockTimer = new Timer(CLOCK_DELAY, gm.countDown);
        clockTimer.setCoalesce(true);
        clockTimer.start();
    }
}
