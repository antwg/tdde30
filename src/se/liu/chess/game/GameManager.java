package se.liu.chess.game;

import se.liu.chess.gui.GameViewer;
import se.liu.chess.gui.TimeComponent;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.logging.Logger;

/**
 * Manages a game of Chess. It creates a Board, TimeComponent, gameViewer and a Timer.
 * Is responsible for creating a complete game of Chess and keeps track of the players time.
 */

public class GameManager {
    private Board board;
    private TimeComponent timeComponent = null;
    private static final int CLOCK_DELAY = 1000;

    /**
     * Creates a GameManager.
     * Also creates a new Board object.
     */
    public GameManager() {
        this.board = new Board();
    }

    // ----------------------------------------------------- Public Methods ----------------------------------------------------------------

    /**
     * Creates and starts a new game.
     */
    public void createNewGame() {
        this.board.resetBoard();

        this.timeComponent = new TimeComponent(this.board, 180, 512);
        GameViewer gameViewer = new GameViewer(this.board, this.timeComponent);
        gameViewer.show();
        this.board.updateAvailableMoves(this.board.getActivePlayer());

        final Timer clockTimer = new Timer(CLOCK_DELAY, this.countDown);
        clockTimer.setCoalesce(true);
        clockTimer.start();
    }


    // ----------------------------------------------------- Private Methods ---------------------------------------------------------------

    private final Action countDown = new AbstractAction() {

        @Override public void actionPerformed(final ActionEvent e) {
            Player activePlayer = board.getActivePlayer();

            if (!board.isGameOver()) {
                if (activePlayer.isOutOfTime()) {
                    board.setGameOver(true);
                    board.getChessComponent().displayGameOver(GameOverCauses.TIME);     // (komplettering) (kommentar 3) kallar på metoden i chessComponent istället för direkt i board
                }
                else {
                    activePlayer.countDown();
                    timeComponent.repaint();
                }
            }
        }
    };

    // ------------------------------------------------------- Main Method -----------------------------------------------------------------

    public static void main(String[] args) {
        GameLogger.setUpLogger();
        GameManager gm = new GameManager();
        gm.createNewGame();
    }
}
