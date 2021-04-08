package se.liu.chess.game;

import se.liu.chess.gui.GameViewer;
import se.liu.chess.gui.ChessComponent;
import se.liu.chess.gui.TimeComponent;

/**
 * Manages a game of Chess. It creates all needed objects and runs the game.
 */

public class GameManager
{
    private Board board;

    public GameManager() {
        this.board = new Board(8, 8);
    }

    // ----------------------------------------------------- Public Methods ----------------------------------------------------------------

    public void createNewGame() {
        this.board = new Board(8, 8);
    }

    public static void main(String[] args) {
        // Testing

        GameManager gm = new GameManager();
        gm.createNewGame();

        gm.board.resetBoard();
        gm.board.printBoard();
        System.out.println(gm.board.convertBoardToFEN());

        GameViewer gameViewer = new GameViewer(new ChessComponent(gm.board), new TimeComponent(gm.board, 180, 512));
        gameViewer.show();

    }
}
