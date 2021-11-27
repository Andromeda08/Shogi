package io.shogi.pieces;

import io.shogi.core.Board;
import io.shogi.core.Field;
import io.shogi.core.Piece;

public class Knight extends Piece {
    public Knight(int owner) {
        super(owner);
        setSymbol("N");
        setType("Knight");
    }

    @Override
    public boolean canMove(Field current, Field target, Board board) {
        // Narikei movement = Tokin movement
        if (promoted) {
            if (Math.abs(current.row() - target.row()) <= 1 &&
                    Math.abs(current.col() - target.col()) <= 1) {
                // Don't allow backwards diagonal movement
                if (owner == 1) {
                    if (current.row() - target.row() == 1 &&
                            current.col() != target.col()) {
                        return false;
                    }
                }
                else {
                    if (current.row() - target.row() == -1 &&
                            current.col() != target.col()) {
                        return false;
                    }
                }
                if(target.getPiece() != null) {
                    if (current.getPiece().getOwner() == target.getPiece().getOwner()) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }

        // Normal movement
        if (owner == 2) {
            if ((current.row() - target.row() != 2) || Math.abs(current.col() - target.col()) != 1)
                return false;
        }
        if (owner == 1) {
            if ((current.row() - target.row() != -2) || Math.abs(current.col() - target.col()) != 1)
                return false;
        }
        if (target.getPiece() != null && current.getPiece().getOwner() == target.getPiece().getOwner()) {
            return false;
        }

        return true;
    }
}
