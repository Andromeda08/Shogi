package io.shogi.pieces;

import io.shogi.core.Board;
import io.shogi.core.Field;
import io.shogi.core.Piece;
import io.shogi.core.PieceType;

/**
 * Király
 */
public class King extends Piece {
    /**
     * Király constructor
     * @param owner Tulajdonos
     */
    public King(int owner) {
        super(owner);
        setSymbol("K");
        setType(PieceType.OSHO);
    }

    /**
     * King movement code
     * @param current A mező, ahol az egység tartózkodik.
     * @param target A mező, ahova szeretnénk lépni.
     * @param board A játéktábla.
     * @return Tud-e lépni az egység.
     */
    @Override
    public boolean canMove(Field current, Field target, Board board) {
        if (target.getPiece() != null)
            if (current.getPiece().getOwner() == target.getPiece().getOwner())
                return false;

        if ((Math.abs(current.col() - target.col()) <= 1) && (Math.abs(current.row() - target.row()) <= 1))
            return true;

        return false;
    }

    // This piece doesn't get promoted
    public void promote() {
        return;
    }
}
