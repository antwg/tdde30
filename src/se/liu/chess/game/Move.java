package se.liu.chess.game;

import se.liu.chess.pieces.Piece;
import se.liu.chess.pieces.PieceType;

import java.awt.*;
import java.util.Set;

public class Move
{
    private Point originSquare;
    private Point targetSquare;
    private Piece movingPiece;
    private boolean harmless;
    private boolean castling;
    private boolean pawnDoubleStep;
    //private boolean promoting;
    private boolean enPassant;

    public Move(final Point originSquare, final Point targetSquare, final Piece movingPiece, Set<MoveCharacteristics> moveCharacteristics)
    {
	this.originSquare = originSquare;
	this.targetSquare = targetSquare;
	this.movingPiece = movingPiece;

	this.harmless = moveCharacteristics.contains(MoveCharacteristics.HARMLESS);
	this.castling = moveCharacteristics.contains(MoveCharacteristics.CASTLING);
	this.pawnDoubleStep = moveCharacteristics.contains(MoveCharacteristics.DOUBLESTEP);
	//this.promoting = moveCharacteristics.contains(MoveCharacteristics.PROMOTING);
	this.enPassant = moveCharacteristics.contains(MoveCharacteristics.ENPASSANT);
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
	//return promoting;
    }

    public boolean isEnPassant() {
	return enPassant;
    }
}
