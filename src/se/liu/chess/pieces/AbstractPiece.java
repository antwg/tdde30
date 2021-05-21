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

public abstract class AbstractPiece implements Piece {
    protected Player owner;
    protected Point position;

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

    @Override public void performSpecialMove(final Move move, final Point enPassantTarget, final Board board) {
        // Medvetet lämnad tom då vissa subklasser inte ska göra ingenting när denna metod kallas.
    }

    protected Set<Move> limitMovesToThreatSquares(Board board, Set<Move> initialMoves) {
        List<Set<Point>> threats = board.getAllDirectThreats();
        if (threats.isEmpty()) {
            return initialMoves;
        }
        else {
            for (Set<Point> threat : threats){
                if (threat.isEmpty()){
                    return initialMoves;
                }
            }
        }

        Set<Move> restrictedMoves = new HashSet<>();

        if (threats.size() > 1) {
            return restrictedMoves;
        } else {
            for (Move move : initialMoves) {
                if (threats.get(0).contains(move.getTargetSquare())) {
                    restrictedMoves.add(move);
                }
            }
        }

        return restrictedMoves;
    }

    protected Set<Move> limitMovesToPinSquares(Board board, Set<Move> initialMoves) {
        Set<Point> pinRestrictedSquares = null;

        for (Set<Point> pin : board.getAllPins()) {
            if (pin.contains(this.position)) {
                pinRestrictedSquares = pin;
            }
        }

        if (pinRestrictedSquares == null) {
            return initialMoves;
        }

        Set<Move> restrictedMoves = new HashSet<>();

        for (Move move : initialMoves) {
            if (pinRestrictedSquares.contains(move.getTargetSquare())) {
                restrictedMoves.add(move);
            }
        }

        return restrictedMoves;
    }
}
