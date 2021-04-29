package se.liu.chess.pieces;

import se.liu.chess.game.Board;
import se.liu.chess.game.Move;
import se.liu.chess.game.Player;
import se.liu.chess.game.TeamColor;

import java.awt.Point;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

/**
 * AbstractPiece is an abstract class that implements the Piece class.
 * AbstractPiece is extended by all PieceTypes.
 */

public abstract class AbstractPiece implements Piece
{
    protected Player owner;
    protected Point position;

    protected boolean hasMoved = false;

    protected AbstractPiece(final Player owner, final Point position) {
	this.owner = owner;
	this.position = position;
    }

    @Override public Player getOwner() {
        return owner;
    }

    @Override public TeamColor getColor() {
        return owner.getColor();
    }

    @Override public Point getPosition() {
        return position;
    }

    @Override public void setPosition(final Point position) {
        this.position = position;
    }

    @Override public boolean hasMoved() {
        return hasMoved;
    }

    @Override public void setHasMoved(final boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    protected Set<Move> limitMovesToThreatSquares(Board board, Set<Move> initialMoveSet) {
        List<Set<Point>> threats = board.getAllDirectThreats();
        if (threats.isEmpty()) {
            return initialMoveSet;
        }
        else { //TODO kan vara fel
            for (Set<Point> threat : threats){
                if (threat.isEmpty()){
                    return initialMoveSet;
                }
            }
        }

        Set<Move> restrictedMoveSet = new HashSet<>();

        if (threats.size() > 1) {
            return restrictedMoveSet;
        } else {
            for (Move move : initialMoveSet) {
                if (threats.get(0).contains(move.getTargetSquare())) {
                    restrictedMoveSet.add(move);
                }
            }
        }

        return restrictedMoveSet;
    }

    protected Set<Move> limitMovesToPinSquares(Board board, Set<Move> initialMoveSet) {
        Set<Point> pinRestrictedSquares = null;

        for (Set<Point> pin : board.getAllPins()) {
            if (pin.contains(this.position)) {
                pinRestrictedSquares = pin;
            }
        }

        if (pinRestrictedSquares == null) {
            return initialMoveSet;
        }

        Set<Move> restrictedMoveSet = new HashSet<>();

        for (Move move : initialMoveSet) {
            if (pinRestrictedSquares.contains(move.getTargetSquare())) {
                restrictedMoveSet.add(move);
            }
        }

        return restrictedMoveSet;
    }
}
