package io.shogi.pieces;

import io.shogi.core.Board;
import io.shogi.core.Field;
import io.shogi.core.Piece;

public class Bishop extends Piece {
    public Bishop(int owner) {
        super(owner);
        setSymbol("B");
        setType("Bishop");
    }

    @Override
    public boolean canMove(Field current, Field target, Board board) {
        if (promoted) {
            if ((Math.abs(current.col() - target.col()) <= 1) && (Math.abs(current.row() - target.row()) <= 1))
                return true;
        }
        if (Math.abs(current.row() - target.row()) == Math.abs(current.col() - target.col())) {
            // TODO: Holy shit fix this cause it doesn't work
            // Decide direction
            int rowDir = target.row() > current.row() ? 1 : -1;
            int colDir = target.col() > current.col() ? 1 : -1;

            for (int i = 1; i < Math.abs(target.col() - current.col()); i++) {
                if (board.getField(current.row() + i * rowDir, current.col() + i * colDir).getPiece() != null) {
                    return false;
                }
            }

            if (target.getPiece() != null) {
                if (current.getPiece().getOwner() == target.getPiece().getOwner()) {
                    return false;
                }
            }

            return true;

        }
        return false;
    }
}
