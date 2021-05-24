package se.liu.chess.pieces;

import se.liu.chess.game.Board;
import se.liu.chess.game.Move;
import se.liu.chess.game.MoveCharacteristics;
import se.liu.chess.game.Player;
import se.liu.chess.game.TeamColor;

import java.awt.*;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

/**
 * The Pawn class extends AbstractPiece.
 * Overrides getMoves, getType and toString.
 * Has three types of moves: normal, attacking and first move and a special case for en passant
 */
public class Pawn extends AbstractPiece {

    private Point[] attackingMoves = {new Point(1, owner.getForwardDirection()), new Point(-1, owner.getForwardDirection())};

    public Pawn(final Player owner, final Point position) {
	super(owner, position);

    }

    // ----------------------------------------------------- Public Methods ----------------------------------------------------------------

    @Override public PieceType getType() {
	return PieceType.PAWN;
    }

    @Override public Set<Move> getMoves(final Board board, final int x, final int y) {
	Set<Move> possibleMoves = new HashSet<>();
	Set<MoveCharacteristics> moveCharacteristics = EnumSet.noneOf(MoveCharacteristics.class);

	addAttackingMoves(board, x, y, possibleMoves, moveCharacteristics);

	addHarmlessMoves(board, x, y, possibleMoves, moveCharacteristics);

	// Limit moves

	possibleMoves = limitMovesToThreatSquares(board, possibleMoves);

	possibleMoves = limitMovesToPinSquares(board, possibleMoves);

	return possibleMoves;
    }

    @Override public String toString() {
	if (this.getColor() == TeamColor.WHITE) {
	    return "P";
	}
	return "p";
    }

    @Override public void performSpecialMove(final Move move, Point enPassantTarget, final Board board) {
	Point targetSquare = move.getTargetSquare();
	if (targetSquare.equals(enPassantTarget)){
	    int captureX = targetSquare.x;
	    int captureY = targetSquare.y + board.getInactivePlayer().getForwardDirection();

	    board.setPiece(captureX, captureY, null);
	}
    }

    // ----------------------------------------------------- Private Methods ---------------------------------------------------------------

    private void addHarmlessMoves(final Board board, final int x, final int y, final Set<Move> possibleMoves,
				  final Set<MoveCharacteristics> moveCharacteristics) {

        int yForward = y + owner.getForwardDirection();
	int yDoubleForward = y + 2 * owner.getForwardDirection();

	if (board.isValidTile(x, yForward) &&
	    board.getPiece(x, yForward) == null) {
	    moveCharacteristics.add(MoveCharacteristics.HARMLESS);

	    Move moveToAdd = new Move(new Point(x, y), new Point(x, yForward),
				      this, moveCharacteristics);
	    possibleMoves.add(moveToAdd);

	    if (y == owner.getPawnRank() &&
		board.isValidTile(x, yDoubleForward) &&
		board.getPiece(x, yDoubleForward) == null) {
		moveCharacteristics.add(MoveCharacteristics.DOUBLE_STEP);

		moveToAdd = new Move(new Point(x, y), new Point(x, yDoubleForward),
				     this, moveCharacteristics);
		possibleMoves.add(moveToAdd);
	    }
	}
    }

    private void addAttackingMoves(final Board board, final int x, final int y, final Set<Move> possibleMoves,
				   final Set<MoveCharacteristics> moveCharacteristics) {
	for (Point move: attackingMoves) {
	    int combinedX = x + move.x;
	    int combinedY = y + move.y;

	    if (board.isValidTile(combinedX, combinedY)) {
		Piece piece = board.getPiece(combinedX, combinedY);
		Point enPassantTarget = board.getEnPassantTarget();
		if ((enPassantTarget != null && enPassantTarget.x == combinedX && enPassantTarget.y == combinedY) ||
		    (piece != null && piece.getColor() != this.getColor())) {
		    Move moveToAdd = new Move(new Point(x, y), new Point(combinedX, combinedY),
					      this, moveCharacteristics);
		    possibleMoves.add(moveToAdd);
		}
	    }
	}
    }
}
