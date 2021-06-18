package se.liu.chess.gui;

import se.liu.chess.game.Board;
import se.liu.chess.game.GameOverCauses;
import se.liu.chess.game.Move;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import static java.util.Map.entry;


/**
 * An extension of JComponent. Given a Board object, ChessComponent will be able to paint the board itself as well as the pieces on the board.
 * ChessComponent will loop over all all points in board and paint relevant data.
 * The graphics for the pieces are saved in resources.
 * The board argument will take any Board object and paint it.
 * Listens for mouse input.
 */

public class ChessComponent extends JComponent {
    private final Logger logger = Logger.getLogger(ChessComponent.class.getName());
    private Board board;
    private int width, height;
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
	setUpLogger();

	this.imageIconMap = Map.ofEntries(entry("B", loadIMG("BishopWhite")),
					  entry("b", loadIMG("BishopBlack")),
					  entry("K", loadIMG("KingWhite")),
					  entry("k", loadIMG("KingBlack")),
					  entry("N", loadIMG("KnightWhite")),
					  entry("n", loadIMG("KnightBlack")),
					  entry("P", loadIMG("PawnWhite")),
					  entry("p", loadIMG("PawnBlack")),
					  entry("Q", loadIMG("QueenWhite")),
					  entry("q", loadIMG("QueenBlack")),
					  entry("R", loadIMG("RookWhite")),
					  entry("r", loadIMG("RookBlack")));

	this.addMouseListener(new MouseAdapter(){
	    @Override public void mousePressed(final MouseEvent e) {
	        lastPressed = currentlyPressed;
		currentlyPressed = new Point(Math.floorDiv(e.getX(), SQUARE_SIZE), Math.floorDiv(e.getY(), SQUARE_SIZE)); // Finds what point on board
		Move move = getMadeMove();
		if (move != null) {
		    board.performMove(move);
		}
		repaint();
	    }
	});
    }

    private void setUpLogger() {
	try {
	    FileHandler fileText = new FileHandler("Logging.txt");
	    logger.setLevel(Level.ALL);
	    SimpleFormatter formatterText = new SimpleFormatter();
	    fileText.setFormatter(formatterText);
	    logger.addHandler(fileText);
	} catch (IOException e) {
	    // (Inspection) Can't log if log failed
	    String[] options = {"Yes", "No"};
	    int choice = JOptionPane.showOptionDialog(null, "Could not access Logging.txt, play anyway?", "",
						      JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
	    if (choice == 1) {
		System.exit(1);
	    }
	    e.printStackTrace();
	}
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


    // (komplettering) metoden har lagts till
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

    // (komplettering) metoden har flyttats hit från Board
    public int displayGameOver(GameOverCauses cause) {
	repaint();
	String message = getGameOverMessage(cause);
	String[] options = {"Restart", "Quit"};
	final int restart = 0, quit = 1;
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

		if (isValidMove(col, row)) highlightColor = HIGHLIGHT_COLOR;

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

    // (komplettering) metoden har flyttats hit från board
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

    private boolean isValidMove(final int col, final int row){
	return currentlyPressed != null && getTargetPointsFromMoveSet(currentlyPressed.x, currentlyPressed.y).contains(new Point(col, row));
    }

    private ImageIcon loadIMG(String name) {
	ImageIcon icon = null;
        try {
            icon = new ImageIcon(findURL(name));
	    }
        catch (FileNotFoundException fileNotFoundException) {
	    logger.log(Level.SEVERE, "Could not find: " + name, fileNotFoundException);
	    System.exit(1);
	}
	return icon;
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

    private Set<Move> getMovesForCoordinate(int x, int y){
        Set<Move> moves = new HashSet<>();
	for (Move move: board.getMoves()){
	    if (move.getOriginSquare().equals(new Point(x, y))){
		moves.add(move);
	    }
	}
	return moves;
    }

    private Set<Point> getTargetPointsFromMoveSet(int x, int y){
	Set<Point> points = new HashSet<>();
	for (Move move: getMovesForCoordinate(x, y)){
		points.add(move.getTargetSquare());
	}
	return points;
    }

    private Move getMadeMove(){
        if (lastPressed != null){
            for (Move move : getMovesForCoordinate(lastPressed.x, lastPressed.y)){
                Point targetSquare = move.getTargetSquare();
                if (targetSquare.x == currentlyPressed.x && targetSquare.y == currentlyPressed.y){
		    return move;
		}
	    }
	}
        return null;
    }
}
