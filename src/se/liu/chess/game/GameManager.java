package se.liu.chess.game;

import se.liu.chess.gui.BoardViewer;

/**
 * Manages a game of Chess. It creates all needed objects and runs the game.
 *
 * @param board The board handles the game state.
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
        System.out.println(gm.board.boardToFEN());

        BoardViewer boardViewer = new BoardViewer(gm.board);
        boardViewer.show();

    }
}
