package se.liu.chess.gui;

import se.liu.chess.game.Board;
import se.liu.chess.game.GameOverCauses;
import se.liu.chess.game.Move;
import se.liu.chess.game.MoveFinderGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.Map.entry;


/**
 * An extension of JComponent. Given a Board object, ChessComponent will be able to paint the board itself as well as the pieces on the board.
 * ChessComponent will loop over all all points in board and paint relevant data.
 * The graphics for the pieces are saved in resources.
 * The board argument will take any Board object and paint it.
 * Listens for mouse input.
 */

public class ChessComponent extends JComponent {
    private Board board;
    private int width, height;
    private MoveFinderGUI moveFinderGUI;
    private ImageLoader imageLoader;
    private Point currentlyPressed = null, lastPressed = null;
    private final static int SQUARE_SIZE = 64, IMG_SIZE = 60, OFFSET = (SQUARE_SIZE - IMG_SIZE) / 2;
    private final static Color ODD_TILE_COLOR = Color.DARK_GRAY, EVEN_TILE_COLOR = Color.WHITE,
	                       HIGHLIGHT_COLOR = new Color(180, 190, 90, 100), TRANSPARENT = new Color(0, 0, 0, 0);
    private final Map<String, ImageIcon> imageIconMap;

    /**
     * Creates a chessComponent and a mouseListener.
     *
     * @param board A Board object
     */
    public ChessComponent(final Board board){
	this.board = board;
	this.width = SQUARE_SIZE * board.getWidth();
	this.height = SQUARE_SIZE * board.getHeight();
	this.moveFinderGUI = new MoveFinderGUI(board);
	this.imageLoader = new ImageLoader();

	this.imageIconMap = Map.ofEntries(entry("B", imageLoader.loadIMG("BishopWhite")),
					  entry("b", imageLoader.loadIMG("BishopBlack")),
					  entry("K", imageLoader.loadIMG("KingWhite")),
					  entry("k", imageLoader.loadIMG("KingBlack")),
					  entry("N", imageLoader.loadIMG("KnightWhite")),
					  entry("n", imageLoader.loadIMG("KnightBlack")),
					  entry("P", imageLoader.loadIMG("PawnWhite")),
					  entry("p", imageLoader.loadIMG("PawnBlack")),
					  entry("Q", imageLoader.loadIMG("QueenWhite")),
					  entry("q", imageLoader.loadIMG("QueenBlack")),
					  entry("R", imageLoader.loadIMG("RookWhite")),
					  entry("r", imageLoader.loadIMG("RookBlack")));

	this.addMouseListener(new MouseAdapter(){
	    @Override public void mousePressed(final MouseEvent e) {
	        lastPressed = currentlyPressed;
		currentlyPressed = new Point(Math.floorDiv(e.getX(), SQUARE_SIZE), Math.floorDiv(e.getY(), SQUARE_SIZE)); // Finds what point on board
		Move move = moveFinderGUI.getMadeMove(currentlyPressed, lastPressed);
		if (move != null) {
		    board.performMove(move);
		}
		repaint(); // (Komplettering) Needs to be updated every click to show available moves (Kommentar 9)
	    }
	});
    }

    // ----------------------------------------------------- Public Methods ----------------------------------------------------------------

    /**
     * Getter for Board.
     *
     * @return Board
     */
    public Board getBoard() {
	return board;
    }

    /**
     * Returns the preferred size of component.
     *
     * @return Dimension of preferred size
     */
    @Override public Dimension getPreferredSize() {
	return new Dimension(width, height);
    }


    // (komplettering) (kommentar 3) metoden har lagts till
    /**
     * Asks the player to select a piece type to promote to and returns an
     * integer based on which piece the player selected to promote.
     *
     * @return 1 = Queen, 2 = Rook, 3 = Bishop, 4 = Knight
     */
    public int selectPromotion() {
	String[] options = {"Queen", "Rook", "Bishop", "Knight"};
	int choice = JOptionPane.showOptionDialog(null, "Choose upgrade", "", JOptionPane.DEFAULT_OPTION,
						  JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

	return choice;
    }

    // (komplettering) (kommentar 3) metoden har flyttats hit från Board
    public int displayGameOverMessage(GameOverCauses cause) {
	String message = getGameOverMessage(cause);
	String[] options = {"Restart", "Quit"};

	int choice = JOptionPane.showOptionDialog(null, message, "", JOptionPane.DEFAULT_OPTION,
						  JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

	return choice;
    }

    // ----------------------------------------------------- Protected Methods -------------------------------------------------------------

    /**
     * Paints a board including the Pieces
     *
     * @param g Graphics object
     */
    @Override protected void paintComponent(final Graphics g) {
	super.paintComponent(g);
	final Graphics2D g2d = (Graphics2D) g;
	g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	for (int col = 0; col < board.getWidth(); col++) {
	    for (int row = 0; row < board.getHeight(); row++) {

		Color tileColor = ODD_TILE_COLOR;
		Color highlightColor = TRANSPARENT;

		if (isEvenTile(col, row)) tileColor = EVEN_TILE_COLOR;

		if (isSelectedPiece(col, row)) highlightColor = HIGHLIGHT_COLOR;

		// (Komplettering) moveFinderGUI is now its own class (Kommentar 7)
		if (moveFinderGUI.isValidMove(col, row, currentlyPressed)) highlightColor = HIGHLIGHT_COLOR;

		g2d.setColor(tileColor);
		g2d.fillRect(col * SQUARE_SIZE, row * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);

		g2d.setColor(highlightColor);
		g2d.fillRect(col * SQUARE_SIZE, row * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);

		// Paint pieces
		if (!board.isEmpty(col, row)) {
		    ImageIcon pieceImage = imageIconMap.get(board.getPiece(col, row).toString());
		    pieceImage.paintIcon(this, g, col * SQUARE_SIZE + OFFSET, row * SQUARE_SIZE + OFFSET);
	    }
	}
    }
}

    // ------------------------------------------------ Private Methods --------------------------------------------------------------------

    // (komplettering) (kommentar 3) metoden har flyttats hit från board
    private String getGameOverMessage(final GameOverCauses cause) {
	StringBuilder message = new StringBuilder();
	message.append("Game Over: ");
	String activePlayer = board.getActivePlayer().toString();

	switch (cause){
	    case TIME:
		message.append(activePlayer).append(" ran out of time");
		break;
	    case CHECKMATE:
		message.append(activePlayer).append(" is Checkmated");
		break;
	    case STALEMATE:
		message.append(" Stalemate");
		break;
	}
	return message.toString();
    }

    private boolean isEvenTile(final int col, final int row){
        return (col + row) % 2 == 0;
    }

    private boolean isSelectedPiece(final int col, final int row){
        return currentlyPressed != null && currentlyPressed.equals(new Point(col, row));
    }

    private URL findURL(String name) throws FileNotFoundException {
        URL url = ClassLoader.getSystemResource("images/" + name + ".png");
	if (url == null) {
	    throw new FileNotFoundException("Could not load file: " + name + ".png");
	}
	else {
	    return url;
	}
    }
}
