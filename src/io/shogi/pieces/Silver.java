package io.shogi.pieces;

import io.shogi.core.Board;
import io.shogi.core.Field;
import io.shogi.core.Piece;

public class Silver extends Piece {
    public Silver(int owner) {
        super(owner);
        setSymbol("S");
        setType("Silver");
    }

    @Override
    public boolean canMove(Field current, Field target, Board board) {
        if (promoted) {
            if (Math.abs(current.row() - target.row()) <= 1 && Math.abs(current.col() - target.col()) <= 1) {

                if (owner == 1) {
                    if (current.row() - target.row() == 1 && current.col() != target.col())
                        return false;
                }

                if (owner == 2) {
                    if (current.row() - target.row() == -1 && current.col() != target.col())
                        return false;
                }

                if (target.getPiece() != null && current.getPiece().getOwner() == target.getPiece().getOwner()) {
                    return false;
                }

                return true;
            }

            return false;
        }

        if ((Math.abs(current.row() - target.row())) <= 1 && (Math.abs(current.col() - target.col())) <= 1) {

            if (owner == 1) {
                if (current.row() - target.row() == 1)
                    if (current.col() == target.col())
                        return false;
            }

            if (owner == 2) {
                if (current.row() - target.row() == -1)
                    if (current.col() == target.col())
                        return false;
            }

            if (current.row() == target.row()) {
                return false;
            }

            if (target.getPiece() != null && current.getPiece().getOwner() == target.getPiece().getOwner()) {
                return false;
            }

            return true;
        }
        return false;
    }
}
