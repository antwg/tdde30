package se.liu.chess.game;

import se.liu.chess.pieces.Piece;
import se.liu.chess.pieces.PieceType;

import java.awt.*;
import java.util.Objects;
import java.util.Set;

/**
 * Class used to move Piece on Board.
 * Specifies what kind of movement a move is eg. attacking, harmless etc.
 * Contains information about origin and target.
 */

public class Move
{
    private Point originSquare;
    private Point targetSquare;
    private Piece movingPiece;
    private boolean harmless;
    private boolean castling;
    private boolean pawnDoubleStep;

    public Move(final Point originSquare, final Point targetSquare, final Piece movingPiece, Set<MoveCharacteristics> moveCharacteristics)
    {
	this.originSquare = originSquare;
	this.targetSquare = targetSquare;
	this.movingPiece = movingPiece;

	this.harmless = moveCharacteristics.contains(MoveCharacteristics.HARMLESS);
	this.castling = moveCharacteristics.contains(MoveCharacteristics.CASTLING);
	this.pawnDoubleStep = moveCharacteristics.contains(MoveCharacteristics.DOUBLE_STEP);
    }

    public Point getOriginSquare() {
	return originSquare;
    }

    public Point getTargetSquare() {
	return targetSquare;
    }

    public Piece getMovingPiece() {
	return movingPiece;
    }

    public boolean isHarmless() {
	return harmless;
    }

    public boolean isCastling() {
	return castling;
    }

    public boolean isPawnDoubleStep() {
	return pawnDoubleStep;
    }

    public boolean isPromoting() {
        return (targetSquare.y == movingPiece.getOwner().getPromotionRank()) &&
	       (movingPiece.getType() == PieceType.PAWN);
    }

    @Override public int hashCode() {
	return Objects.hash(originSquare, targetSquare, movingPiece);
    }

    @Override public boolean equals(final Object obj) {
	if (obj == this)
	    return true;

	if (!(obj instanceof Move))
	    return false;

	Move moveObj = (Move) obj;


	return (hasSameBasics(moveObj) && hasSameCharacteristics(moveObj));
    }

    private boolean hasSameBasics(final Move moveObj) {
	return originSquare.equals(moveObj.originSquare) && (targetSquare.equals(moveObj.targetSquare)) &&
	       (movingPiece.equals(moveObj.movingPiece));
    }

    private boolean hasSameCharacteristics(final Move moveObj) {
	return (harmless == moveObj.harmless) && (castling == moveObj.castling) && (pawnDoubleStep == moveObj.pawnDoubleStep);
    }

}
