package io.shogi.pieces;

import io.shogi.core.Board;
import io.shogi.core.Field;
import io.shogi.core.Piece;

public class King extends Piece {
    public King(int owner) {
        super(owner);
        setSymbol("K");
        setType("King");
    }

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
