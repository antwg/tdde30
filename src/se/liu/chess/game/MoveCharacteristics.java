package se.liu.chess.game;

public enum MoveCharacteristics
{
    HARMLESS,       // Moves that can not capture a piece, for example castling and pawn moves forward.
    DOUBLESTEP,     // The double step a pawn can make. This allows an en passant to happen on the next turn.
    CASTLING,       // Castling. This type of move requires a rook to be moved along with the king.
    PROMOTING,      // Promotion. This type of move transforms a piece into another piece.
    ENPASSANT       // En passant. This captures a piece on a square different from the target square.
}
