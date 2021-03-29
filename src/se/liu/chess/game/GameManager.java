package se.liu.chess.game;

public class GameManager
{
    Board board = null;

    public void createNewGame() {
        board = new Board(8, 8);
    }

    public static void main(String[] args) {
        // Testing

        GameManager gm = new GameManager();
        gm.createNewGame();

    }
}
