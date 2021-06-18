package se.liu.chess.game;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

/**
 * A class containing methods to convert clicks on ChessComponent to information regarding what the clicks mean. ex if a move
 * was made and if so what move was made.
 */

public class MoveFinderGUI {
	Board board;

    public MoveFinderGUI(Board board) {
        this.board = board;
    }

    // ----------------------------------------------------- Public Methods ----------------------------------------------------------------


    public boolean isValidMove(final int col, final int row, Point currentlyPressed){
	return currentlyPressed != null && getTargetPointsFromMoveSet(currentlyPressed.x, currentlyPressed.y).contains(new Point(col, row));
    }

    public Move getMadeMove(Point currentlyPressed, Point lastPressed){
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

    // ----------------------------------------------------- Private Methods ---------------------------------------------------------------


    private Set<Point> getTargetPointsFromMoveSet(int x, int y){
	Set<Point> points = new HashSet<>();
	for (Move move: getMovesForCoordinate(x, y)){
	    points.add(move.getTargetSquare());
	}
	return points;
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
}
