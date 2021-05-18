package se.liu.chess.game;

/**
 * An enum containing all the ways a game can end.
 * Checkmate means that the king is in check and can't get unchecked by any move.
 * Stalemate is a tie.
 * Time is when one player runs out of time.
 */
public enum GameOverCauses
{
    CHECKMATE, STALEMATE, TIME
    // -- FIFTYMOVE, THREEFOLDREPETITION --
}
